package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.domain.QueryContext;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * 基础查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/02
 */
public abstract class BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final Log log = GlobalLogFactory.get().getLog(BaseQueryTypeStrategy.class);

    public BaseQueryTypeStrategy() {
        super();
    }

    public BaseQueryTypeStrategy(QueryType QUERY_TYPE) {
        this();
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 校验参数配置和构建查询
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    <T, E> void constructQueryWrapper(QueryContext<T, E> queryContext) {

        Object value = null;
        QueryField queryField = queryContext.getQueryField();

        try {
            //检测是否分组
            if (checkIfNotInGroup(queryContext)) {
                return;
            }
            //准备参数
            prepareContext(queryContext);
            //BETWEEN和NOT_BETWEEN的查询不校验查询参数
            if (queryField.value().equals(QueryType.BETWEEN) || queryField.value().equals(QueryType.NOT_BETWEEN)) {
                buildQueryWrapper(queryContext);
            } else {
                //校验参数是否为空
                if (QueryUtil.checkValue(value)) {
                    //构建QueryWrapper
                    buildQueryWrapper(queryContext);
                } else {
                    //检查值空时是否查询
                    checkConditionType(queryContext);
                }
            }
            //检查是否排序
            checkOrder(queryContext);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            log.error("构造查询异常,类名:{},字段名:{},值:{},异常原因:{}", queryContext.getClazz().getName(), queryContext.getField().getName(), value, sw.toString());
        } finally {
            //清除已匹配的查询参数
            removeParam(queryContext);
        }
    }

    /**
     * 检测分组
     *
     * @param queryContext 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public <T, E> Boolean checkIfNotInGroup(QueryContext<T, E> queryContext) {
        return !QueryUtil.checkIfInGroup(queryContext);
    }

    /**
     * 检查排序
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void checkOrder(QueryContext<T, E> queryContext) {
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryContext);
    }

    /**
     * 准备参数
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void prepareContext(QueryContext<T, E> queryContext) {
        //获取查询值
        queryContext.setQueryParam(QueryContext.getValueFromQueryParamMap(queryContext.getField().getName()));
        //获取真实表字段名
        queryContext.setTableColumnName(TableUtil.getTableColumnName(queryContext.getClazz(), queryContext.getField()));
    }

    /**
     * 清除查询参数
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(QueryContext<T, E> queryContext) {
        Field field = queryContext.getField();
        //从线程中移除参数
        QueryContext.removeParamFromQueryParamMap(field.getName());
    }

    /**
     * 检查查询值状态
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */

    public <T, E> void checkConditionType(QueryContext<T, E> queryContext) {
        QueryField queryField = queryContext.getQueryField();
        QueryWrapper<T> queryWrapper = queryContext.getQueryWrapper();
        String tableColumnName = queryContext.getTableColumnName();

        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NULL)) {
            queryWrapper.isNull(tableColumnName);
        }
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NOT_NULL)) {
            queryWrapper.isNotNull(tableColumnName);
        }
    }


    /**
     * 检查是否排除模糊查询
     *
     * @param queryContext 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public <T, E> Boolean checkWithoutLike(QueryContext<T, E> queryContext) {
        Boolean withoutLike = QueryContext.getWithoutLike();
        if (withoutLike) {
            QueryTypeStrategy EqQueryTypeStrategy = QueryTypeStrategyManager.getQueryTypeStrategyToManager(QueryType.EQ.getCompareType());
            EqQueryTypeStrategy.buildQueryWrapper(queryContext);

        }

        return withoutLike;
    }

}

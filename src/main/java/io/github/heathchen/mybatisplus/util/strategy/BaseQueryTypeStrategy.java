package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.factory.DefaultQueryWrapperFactory;
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
        DefaultQueryWrapperFactory.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 校验参数配置和构建查询
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void constructQueryWrapper(EntityGenericDefinition<T, E> entityGenericDefinition) {

        QueryType queryType = entityGenericDefinition.getQueryType();

        try {
            //检测是否分组
            if (checkIfNotInGroup(entityGenericDefinition)) {
                return;
            }
            //准备参数
            prepareContext(entityGenericDefinition);
            //BETWEEN和NOT_BETWEEN的查询不校验查询参数
            if (queryType.equals(QueryType.BETWEEN) || queryType.equals(QueryType.NOT_BETWEEN)) {
                buildQueryWrapper(entityGenericDefinition);
            } else {
                //校验参数是否为空
                if (QueryUtil.checkValue(entityGenericDefinition.getQueryParam())) {
                    //构建QueryWrapper
                    buildQueryWrapper(entityGenericDefinition);
                } else {
                    //检查值空时是否查询
                    checkConditionType(entityGenericDefinition);
                }
            }
            //检查是否排序
            checkOrder(entityGenericDefinition);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            log.error("构造查询异常,类名:{},字段名:{},值:{},异常原因:{},构筑上下文:{}", entityGenericDefinition.getParamClass().getName(), entityGenericDefinition.getField().getName(), entityGenericDefinition.getQueryParam(), sw.toString(), entityGenericDefinition.toString());
        } finally {
            //清除已匹配的查询参数
            removeParam(entityGenericDefinition);
        }
    }

    /**
     * 检测分组
     *
     * @param entityGenericDefinition 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public <T, E> Boolean checkIfNotInGroup(EntityGenericDefinition<T, E> entityGenericDefinition) {
        return !QueryUtil.checkIfInGroup(entityGenericDefinition);
    }

    /**
     * 检查排序
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void checkOrder(EntityGenericDefinition<T, E> entityGenericDefinition) {
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(entityGenericDefinition);
    }

    /**
     * 准备参数
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void prepareContext(EntityGenericDefinition<T, E> entityGenericDefinition) {
        //获取查询值
        entityGenericDefinition.setQueryParam(EntityGenericDefinition.getValueFromQueryParamMap(entityGenericDefinition.getField().getName()));
        //获取真实表字段名
        entityGenericDefinition.setTableColumnName(TableUtil.getTableColumnName(entityGenericDefinition.getParamClass(), entityGenericDefinition.getField()));
    }

    /**
     * 清除查询参数
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(EntityGenericDefinition<T, E> entityGenericDefinition) {
        Field field = entityGenericDefinition.getField();
        //从线程中移除参数
        EntityGenericDefinition.removeParamFromQueryParamMap(field.getName());
    }

    /**
     * 检查查询值状态
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */

    public <T, E> void checkConditionType(EntityGenericDefinition<T, E> entityGenericDefinition) {
        ConditionType conditionType = entityGenericDefinition.getConditionType();
        QueryWrapper<T> queryWrapper = entityGenericDefinition.getQueryWrapper();
        String tableColumnName = entityGenericDefinition.getTableColumnName();

        if (conditionType.equals(ConditionType.TABLE_COLUMN_IS_NULL)) {
            queryWrapper.isNull(tableColumnName);
        }
        if (conditionType.equals(ConditionType.TABLE_COLUMN_IS_NOT_NULL)) {
            queryWrapper.isNotNull(tableColumnName);
        }
    }


    /**
     * 检查是否排除模糊查询
     *
     * @param entityGenericDefinition 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public <T, E> Boolean checkWithoutLike(EntityGenericDefinition<T, E> entityGenericDefinition) {
        Boolean withoutLike = EntityGenericDefinition.getWithoutLike();
        if (withoutLike) {
            QueryTypeStrategy EqQueryTypeStrategy = DefaultQueryWrapperFactory.getQueryTypeStrategyToManager(QueryType.EQ.getCompareType());
            EqQueryTypeStrategy.buildQueryWrapper(entityGenericDefinition);

        }

        return withoutLike;
    }

}

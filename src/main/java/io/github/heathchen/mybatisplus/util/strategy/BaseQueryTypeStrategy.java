package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGernericDefinition;
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
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void constructQueryWrapper(EntityGernericDefinition<T, E> entityGernericDefinition) {

        QueryType queryType = entityGernericDefinition.getQueryType();

        try {
            //检测是否分组
            if (checkIfNotInGroup(entityGernericDefinition)) {
                return;
            }
            //准备参数
            prepareContext(entityGernericDefinition);
            //BETWEEN和NOT_BETWEEN的查询不校验查询参数
            if (queryType.equals(QueryType.BETWEEN) || queryType.equals(QueryType.NOT_BETWEEN)) {
                buildQueryWrapper(entityGernericDefinition);
            } else {
                //校验参数是否为空
                if (QueryUtil.checkValue(entityGernericDefinition.getQueryParam())) {
                    //构建QueryWrapper
                    buildQueryWrapper(entityGernericDefinition);
                } else {
                    //检查值空时是否查询
                    checkConditionType(entityGernericDefinition);
                }
            }
            //检查是否排序
            checkOrder(entityGernericDefinition);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            log.error("构造查询异常,类名:{},字段名:{},值:{},异常原因:{},构筑上下文:{}", entityGernericDefinition.getParamClass().getName(), entityGernericDefinition.getField().getName(), entityGernericDefinition.getQueryParam(), sw.toString(), entityGernericDefinition.toString());
        } finally {
            //清除已匹配的查询参数
            removeParam(entityGernericDefinition);
        }
    }

    /**
     * 检测分组
     *
     * @param entityGernericDefinition 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public <T, E> Boolean checkIfNotInGroup(EntityGernericDefinition<T, E> entityGernericDefinition) {
        return !QueryUtil.checkIfInGroup(entityGernericDefinition);
    }

    /**
     * 检查排序
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void checkOrder(EntityGernericDefinition<T, E> entityGernericDefinition) {
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(entityGernericDefinition);
    }

    /**
     * 准备参数
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    public <T, E> void prepareContext(EntityGernericDefinition<T, E> entityGernericDefinition) {
        //获取查询值
        entityGernericDefinition.setQueryParam(EntityGernericDefinition.getValueFromQueryParamMap(entityGernericDefinition.getField().getName()));
        //获取真实表字段名
        entityGernericDefinition.setTableColumnName(TableUtil.getTableColumnName(entityGernericDefinition.getParamClass(), entityGernericDefinition.getField()));
    }

    /**
     * 清除查询参数
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(EntityGernericDefinition<T, E> entityGernericDefinition) {
        Field field = entityGernericDefinition.getField();
        //从线程中移除参数
        EntityGernericDefinition.removeParamFromQueryParamMap(field.getName());
    }

    /**
     * 检查查询值状态
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */

    public <T, E> void checkConditionType(EntityGernericDefinition<T, E> entityGernericDefinition) {
        ConditionType conditionType = entityGernericDefinition.getConditionType();
        QueryWrapper<T> queryWrapper = entityGernericDefinition.getQueryWrapper();
        String tableColumnName = entityGernericDefinition.getTableColumnName();

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
     * @param entityGernericDefinition 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public <T, E> Boolean checkWithoutLike(EntityGernericDefinition<T, E> entityGernericDefinition) {
        Boolean withoutLike = EntityGernericDefinition.getWithoutLike();
        if (withoutLike) {
            QueryTypeStrategy EqQueryTypeStrategy = QueryTypeStrategyManager.getQueryTypeStrategyToManager(QueryType.EQ.getCompareType());
            EqQueryTypeStrategy.buildQueryWrapper(entityGernericDefinition);

        }

        return withoutLike;
    }

}

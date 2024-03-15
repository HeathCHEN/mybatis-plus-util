package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.domain.QueryContext;

/**
 * 查询策略类接口
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public interface QueryTypeStrategy {


    /**
     * 检测分组
     *
     * @param queryContext 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    <T, E> Boolean checkIfNotInGroup(QueryContext<T, E> queryContext);

    /**
     * 构造查询
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    <T, E> void buildQueryWrapper(QueryContext<T, E> queryContext);

    /**
     * 检查字段值状态
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    <T, E> void checkConditionType(QueryContext<T, E> queryContext);

    /**
     * 清除查询参数
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    <T, E> void removeParam(QueryContext<T, E> queryContext);


    /**
     * 检查排序
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    <T, E> void checkOrder(QueryContext<T, E> queryContext);

}

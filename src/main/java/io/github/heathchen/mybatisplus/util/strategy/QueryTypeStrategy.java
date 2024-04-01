package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;

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
     * @param entityGenericDefinition 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    <T, E> Boolean checkIfNotInGroup(EntityGenericDefinition<T, E> entityGenericDefinition);

    /**
     * 构造查询
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void buildQueryWrapper(EntityGenericDefinition<T, E> entityGenericDefinition);

    /**
     * 检查字段值状态
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void checkConditionType(EntityGenericDefinition<T, E> entityGenericDefinition);

    /**
     * 清除查询参数
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void removeParam(EntityGenericDefinition<T, E> entityGenericDefinition);


    /**
     * 检查排序
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void checkOrder(EntityGenericDefinition<T, E> entityGenericDefinition);

}

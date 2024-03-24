package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.definiton.EntityGernericDefinition;

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
     * @param entityGernericDefinition 查询上下文
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    <T, E> Boolean checkIfNotInGroup(EntityGernericDefinition<T, E> entityGernericDefinition);

    /**
     * 构造查询
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void buildQueryWrapper(EntityGernericDefinition<T, E> entityGernericDefinition);

    /**
     * 检查字段值状态
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void checkConditionType(EntityGernericDefinition<T, E> entityGernericDefinition);

    /**
     * 清除查询参数
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void removeParam(EntityGernericDefinition<T, E> entityGernericDefinition);


    /**
     * 检查排序
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    <T, E> void checkOrder(EntityGernericDefinition<T, E> entityGernericDefinition);

}

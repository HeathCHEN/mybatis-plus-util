package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询策略类管理器
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/23
 */
public class QueryTypeStrategyManager {

    public static final Map<String, QueryTypeStrategy> QUERY_TYPE_STRATEGY_MAP = new HashMap<>();

    /**
     * 设置策略
     *
     * @param queryType         查询类型名称
     * @param queryTypeStrategy 查询策略类
     * @author HeathCHEN
     */
    public static void putQueryTypeStrategyToManager(String queryType, QueryTypeStrategy queryTypeStrategy) {
        QUERY_TYPE_STRATEGY_MAP.putIfAbsent(queryType, queryTypeStrategy);
    }


    /**
     * 获取策略
     *
     * @param queryType 查询类型名称
     * @return {@link QueryTypeStrategy } 查询策略类
     * @author HeathCHEN
     */
    public static QueryTypeStrategy getQueryTypeStrategyToManager(String queryType) {
        return QUERY_TYPE_STRATEGY_MAP.get(queryType);
    }


    /**
     * 获取对应策略类并执行
     *
     * @param queryField   QueryField注解
     * @param clazz        类
     * @param field        字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> void invokeQueryStrategy(QueryField queryField, Class<?> clazz, Field field, QueryWrapper<T> queryWrapper) {
        BaseQueryTypeStrategy queryTypeStrategy = (BaseQueryTypeStrategy) getQueryTypeStrategyToManager(queryField.value().getCompareType());
        if (ObjectUtil.isNotNull(queryTypeStrategy)) {
            queryTypeStrategy.constructQueryWrapper(queryField, clazz, field, queryWrapper);
        }
    }


}

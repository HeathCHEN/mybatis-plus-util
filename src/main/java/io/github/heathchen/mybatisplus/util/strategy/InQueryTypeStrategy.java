package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.QueryType;

import java.util.Collection;

/**
 * 字段 IN (v0, v1, ...) 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class InQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.IN;

    public InQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 构造查询
     *
     * @param queryField      QueryField注解
     * @param value           类
     * @param tableColumnName 字段
     * @param queryWrapper    查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQueryWrapper(QueryField queryField, Object value, String tableColumnName, QueryWrapper<T> queryWrapper) {
        if (value instanceof Collection) {
            Collection<?> values = (Collection<?>) value;
            if (CollectionUtil.isNotEmpty(values)) {
                queryWrapper.in(tableColumnName, values);
            }
        }

        if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            if (ArrayUtil.isNotEmpty(values)) {
                queryWrapper.in(tableColumnName, values);
            }
        }
    }
}

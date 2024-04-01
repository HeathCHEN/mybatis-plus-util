package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.enums.QueryType;

import java.util.Collection;

/**
 * 字段 NOT IN (value.get(0), value.get(1), ...) 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class NotInQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.NOT_IN;

    public NotInQueryTypeStrategy() {
        super(QUERY_TYPE);
    }

    /**
     * 构造查询
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGenericDefinition<T, E> entityGenericDefinition) {
        QueryWrapper<T> queryWrapper = entityGenericDefinition.getQueryWrapper();
        String tableColumnName = entityGenericDefinition.getTableColumnName();
        Object queryParam = entityGenericDefinition.getQueryParam();


        if (queryParam instanceof Collection) {
            Collection<?> values = (Collection<?>) queryParam;
            if (CollectionUtil.isNotEmpty(values)) {
                queryWrapper.notIn(tableColumnName, values);
            }
        }

        if (queryParam.getClass().isArray()) {
            Object[] values = (Object[]) queryParam;
            if (ArrayUtil.isNotEmpty(values)) {
                queryWrapper.notIn(tableColumnName, values);
            }
        }
    }
}

package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 字段 IN (v0, v1, ...) 查询策略类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class InQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.IN;

    public InQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }
    /**
     * 构造查询
     * @param queryField QueryField注解
     * @param clazz 类
     * @param field 字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQuery(QueryField queryField, Class clazz, Field field, QueryWrapper<T> queryWrapper, String[] groupIds) {
        if (!QueryUtil.checkIfInGroup(queryField, groupIds)) {
            QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
            return;
        }
        Object value = QueryParamThreadLocal.getValueFromQueryParamMap(field.getName());
        //将属性转为下划线格式
        String tableColumnName = TableUtil.getTableColumnName(clazz,field);

        if (QueryUtil.checkValue(value)) {
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
        }else {
            if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NULL)) {
                queryWrapper.isNull(tableColumnName);
            }
            if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NOT_NULL)) {
                queryWrapper.isNotNull(tableColumnName);
            }
        }
        QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField,clazz, field,tableColumnName);
    }
}

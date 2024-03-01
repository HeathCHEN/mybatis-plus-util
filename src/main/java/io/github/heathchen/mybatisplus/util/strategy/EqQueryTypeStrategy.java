package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * 等于查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class EqQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.EQ;

    public EqQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 构造查询
     *
     * @param queryField QueryField注解
     * @param clazz         类
     * @param field         字段
     * @param queryWrapper  查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQuery(QueryField queryField, Class clazz, Field field, QueryWrapper<T> queryWrapper) {
        Object value = QueryParamThreadLocal.getValueFromQueryParamMap(field.getName());

        String[] orColumns = queryField.orColumns();
        //查询属性名对应字段名
        String tableColumnName = TableUtil.getTableColumnName(clazz, field);
        if (QueryUtil.checkValue(value)) {
            if (ArrayUtil.isNotEmpty(orColumns)) {
                queryWrapper.and(tQueryWrapper -> {
                            tQueryWrapper.eq(tableColumnName, value);
                            for (String orColumn : orColumns) {
                                tQueryWrapper.or();
                                tQueryWrapper.eq(TableUtil.checkOrColumnName(orColumn), value);
                            }
                        }
                );
            } else {
                queryWrapper.eq(tableColumnName, value);
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
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);

    }
}

package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

import java.lang.reflect.Field;

/**
 * 小于等于查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class LessEqualQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.LESS_EQUAL;


    public LessEqualQueryTypeStrategy() {
        super(QUERY_TYPE);
    }

    /**
     * 检测分组
     *
     * @param queryField QueryField注解
     * @param groupIds   传入的分组Ids
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    @Override
    public Boolean checkIfNotInGroup(QueryField queryField, String[] groupIds) {
        return null;
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
        String[] orColumns = queryField.orColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.le(tableColumnName, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.le(TableUtil.checkOrColumnName(orColumn), value);
                        }

                    }
            );
        } else {
            queryWrapper.le(tableColumnName, value);
        }
    }

    /**
     * 检查字段值状态
     *
     * @param queryField      QueryField注解
     * @param queryWrapper    查询queryWrapper
     * @param tableColumnName
     * @author HeathCHEN
     */
    @Override
    public <T> void checkConditionType(QueryField queryField, QueryWrapper<T> queryWrapper, String tableColumnName) {
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NULL)) {
            queryWrapper.isNull(tableColumnName);
        }
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NOT_NULL)) {
            queryWrapper.isNotNull(tableColumnName);
        }
    }

    /**
     * @param queryField
     * @param field
     * @author HeathCHEN
     */
    @Override
    public void removeParam(QueryField queryField, Field field) {
        super.removeParam(null, field);
    }

    /**
     * 检查排序
     *
     * @param queryField      QueryField注解
     * @param clazz           类
     * @param field           字段
     * @param tableColumnName 表字段名
     * @author HeathCHEN
     */
    @Override
    public void checkOrder(QueryField queryField, Class clazz, Field field, String tableColumnName) {
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);
    }
}

package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

import java.lang.reflect.Field;

/**
 * 自定义SQL查询策略类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class SqlQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.SQL;

    public SqlQueryTypeStrategy() {
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
    public  <T> void buildQuery(QueryField queryField, Class clazz, Field field, QueryWrapper<T> queryWrapper, String[] groupIds) {
        if (!QueryUtil.checkIfInGroup(queryField, groupIds)) {
            QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
            return;
        }

        Object value = QueryParamThreadLocal.getValueFromQueryParamMap(field.getName());
        if (QueryUtil.checkValue(value)|| StrUtil.isNotBlank(queryField.sql())) {
            queryWrapper.apply(queryField.sql(), value);
        }

        QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
        //查询属性名对应字段名
        String tableColumnName = TableUtil.getTableColumnName(clazz, field);
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);
    }
}

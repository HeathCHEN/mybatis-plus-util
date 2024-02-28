package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * @param customerQuery CustomerQuery注解
     * @param clazz 类
     * @param field 字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public  <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {
        Object value = QueryParamThreadLocal.getValueFromObjectMap(field.getName());
        if (ObjectUtil.isNotNull(value)|| StrUtil.isNotBlank(customerQuery.sql())) {
            queryWrapper.apply(customerQuery.sql(), value);
        }

        QueryParamThreadLocal.removeParamFromObjectMap(field.getName());
        //查询属性名对应字段名
        String tableColumnName = TableUtil.getTableColumnName(clazz, field);
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(customerQuery, clazz, field, tableColumnName);
    }
}

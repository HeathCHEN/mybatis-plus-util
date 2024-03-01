package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * NOT BETWEEN 值1 AND 值2 查询策略类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class NotBetweenQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.NOT_BETWEEN;

    public NotBetweenQueryTypeStrategy() {
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
    public <T> void buildQuery(QueryField queryField, Class clazz, Field field, QueryWrapper<T> queryWrapper) {

        //将属性转为下划线格式
        String tableColumnName = TableUtil.getTableColumnName(clazz,field);

        Object notBetweenStartValue = QueryParamThreadLocal.getValueFromQueryParamMap(queryField.notBetweenStartVal());
        Object notBetweenEndValue = QueryParamThreadLocal.getValueFromQueryParamMap(queryField.notBetweenEndVal());

        if (QueryUtil.checkValue(notBetweenStartValue)) {
            queryWrapper.le(tableColumnName, notBetweenStartValue);
            QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.notBetweenStartVal());
        }
        if (QueryUtil.checkValue(notBetweenEndValue)) {
            queryWrapper.ge(tableColumnName, notBetweenEndValue);
            QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.notBetweenEndVal());
        }
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);
    }
}

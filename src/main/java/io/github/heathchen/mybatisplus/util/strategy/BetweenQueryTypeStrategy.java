package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * BETWEEN 值1 AND 值2 查询策略
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class BetweenQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.BETWEEN;

    public BetweenQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 构造查询
     *
     * @param queryField   QueryField注解
     * @param clazz        类
     * @param field        字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQuery(QueryField queryField, Class clazz, Field field, QueryWrapper<T> queryWrapper, String[] groupIds) {
        if (!QueryUtil.checkIfInGroup(queryField, groupIds)) {
            QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.betweenStartVal());
            QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.betweenEndVal());
            return;
        }
        //查询属性名对应字段名
        String tableColumnName = TableUtil.getTableColumnName(clazz, field);

        Object startValue = QueryParamThreadLocal.getValueFromQueryParamMap(queryField.betweenStartVal());
        Object endValue = QueryParamThreadLocal.getValueFromQueryParamMap(queryField.betweenEndVal());

        if (QueryUtil.checkValue(startValue)) {
            queryWrapper.ge(tableColumnName, startValue);

        }
        if (QueryUtil.checkValue(endValue)) {
            queryWrapper.le(tableColumnName, endValue);
        }
        QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.betweenStartVal());
        QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.betweenEndVal());
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);
    }
}

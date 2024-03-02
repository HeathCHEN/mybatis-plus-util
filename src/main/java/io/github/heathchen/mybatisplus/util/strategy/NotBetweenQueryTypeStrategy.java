package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;

import java.lang.reflect.Field;

/**
 * NOT BETWEEN 值1 AND 值2 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class NotBetweenQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.NOT_BETWEEN;

    public NotBetweenQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * @param queryField
     * @param field
     * @author HeathCHEN
     */
    @Override
    public void removeParam(QueryField queryField, Field field) {
        QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.notBetweenStartVal());
        QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.notBetweenEndVal());
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
        Object notBetweenStartValue = QueryParamThreadLocal.getValueFromQueryParamMap(queryField.notBetweenStartVal());
        Object notBetweenEndValue = QueryParamThreadLocal.getValueFromQueryParamMap(queryField.notBetweenEndVal());

        if (QueryUtil.checkValue(notBetweenStartValue)) {
            queryWrapper.le(tableColumnName, notBetweenStartValue);

        }
        if (QueryUtil.checkValue(notBetweenEndValue)) {
            queryWrapper.ge(tableColumnName, notBetweenEndValue);
        }
    }
}

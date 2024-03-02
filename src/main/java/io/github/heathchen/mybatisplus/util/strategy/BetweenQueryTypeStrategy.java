package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;

import java.lang.reflect.Field;

/**
 * BETWEEN 值1 AND 值2 查询策略
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class BetweenQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.BETWEEN;

    public BetweenQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
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
        return !QueryUtil.checkIfInGroup(queryField, groupIds);
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

    }


    /**
     * 清除查询参数
     *
     * @param queryField QueryField注解
     * @param field      字段
     */
    @Override
    public void removeParam(QueryField queryField, Field field) {
        QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.betweenStartVal());
        QueryParamThreadLocal.removeParamFromQueryParamMap(queryField.betweenEndVal());
        QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
    }


}

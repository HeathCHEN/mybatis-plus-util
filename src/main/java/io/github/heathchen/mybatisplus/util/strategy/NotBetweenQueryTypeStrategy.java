package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.domain.QueryContext;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
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
        super(QUERY_TYPE);
    }


    /**
     * 构造查询
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(QueryContext<T, E> queryContext) {

        QueryWrapper<T> queryWrapper = queryContext.getQueryWrapper();
        String tableColumnName = queryContext.getTableColumnName();
        QueryField queryField = queryContext.getQueryField();

        Object notBetweenStartValue = QueryContext.getValueFromQueryParamMap(queryField.notBetweenStartVal());
        Object notBetweenEndValue = QueryContext.getValueFromQueryParamMap(queryField.notBetweenEndVal());

        if (QueryUtil.checkValue(notBetweenStartValue)) {
            queryWrapper.le(tableColumnName, notBetweenStartValue);

        }
        if (QueryUtil.checkValue(notBetweenEndValue)) {
            queryWrapper.ge(tableColumnName, notBetweenEndValue);
        }
    }

    /**
     * 清除查询参数
     *
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(QueryContext<T, E> queryContext) {
        QueryField queryField = queryContext.getQueryField();
        Field field = queryContext.getField();
        QueryContext.removeParamFromQueryParamMap(queryField.notBetweenStartVal());
        QueryContext.removeParamFromQueryParamMap(queryField.notBetweenEndVal());
        QueryContext.removeParamFromQueryParamMap(field.getName());
    }
}

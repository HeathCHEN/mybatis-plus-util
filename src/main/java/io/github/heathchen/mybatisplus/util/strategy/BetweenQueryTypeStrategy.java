package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
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
        super(QUERY_TYPE);
    }

    /**
     * 构造查询
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGenericDefinition<T, E> entityGenericDefinition) {
        QueryWrapper<T> queryWrapper = entityGenericDefinition.getQueryWrapper();
        String tableColumnName = entityGenericDefinition.getTableColumnName();

        Object startValue = EntityGenericDefinition.getValueFromQueryParamMap(entityGenericDefinition.getBetweenStartVal());
        Object endValue = EntityGenericDefinition.getValueFromQueryParamMap(entityGenericDefinition.getBetweenEndVal());
        if (QueryUtil.checkValue(startValue)) {
            queryWrapper.ge(tableColumnName, startValue);

        }
        if (QueryUtil.checkValue(endValue)) {
            queryWrapper.le(tableColumnName, endValue);
        }
    }


    /**
     * 清除查询参数
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(EntityGenericDefinition<T, E> entityGenericDefinition) {
        Field field = entityGenericDefinition.getField();
        EntityGenericDefinition.removeParamFromQueryParamMap(entityGenericDefinition.getBetweenStartVal());
        EntityGenericDefinition.removeParamFromQueryParamMap(entityGenericDefinition.getBetweenEndVal());
        EntityGenericDefinition.removeParamFromQueryParamMap(field.getName());
    }


}

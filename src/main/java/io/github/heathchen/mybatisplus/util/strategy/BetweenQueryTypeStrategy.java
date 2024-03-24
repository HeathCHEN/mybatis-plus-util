package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGernericDefinition;
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
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGernericDefinition<T, E> entityGernericDefinition) {
        QueryWrapper<T> queryWrapper = entityGernericDefinition.getQueryWrapper();
        String tableColumnName = entityGernericDefinition.getTableColumnName();

        Object startValue = EntityGernericDefinition.getValueFromQueryParamMap(entityGernericDefinition.getBetweenStartVal());
        Object endValue = EntityGernericDefinition.getValueFromQueryParamMap(entityGernericDefinition.getBetweenEndVal());
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
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(EntityGernericDefinition<T, E> entityGernericDefinition) {
        Field field = entityGernericDefinition.getField();
        EntityGernericDefinition.removeParamFromQueryParamMap(entityGernericDefinition.getBetweenStartVal());
        EntityGernericDefinition.removeParamFromQueryParamMap(entityGernericDefinition.getBetweenEndVal());
        EntityGernericDefinition.removeParamFromQueryParamMap(field.getName());
    }


}

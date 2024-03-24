package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGernericDefinition;
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
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGernericDefinition<T, E> entityGernericDefinition) {

        QueryWrapper<T> queryWrapper = entityGernericDefinition.getQueryWrapper();
        String tableColumnName = entityGernericDefinition.getTableColumnName();

        Object notBetweenStartValue = EntityGernericDefinition.getValueFromQueryParamMap(entityGernericDefinition.getNotBetweenStartVal());
        Object notBetweenEndValue = EntityGernericDefinition.getValueFromQueryParamMap(entityGernericDefinition.getNotBetweenEndVal());

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
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(EntityGernericDefinition<T, E> entityGernericDefinition) {
        Field field = entityGernericDefinition.getField();
        EntityGernericDefinition.removeParamFromQueryParamMap(entityGernericDefinition.getNotBetweenStartVal());
        EntityGernericDefinition.removeParamFromQueryParamMap(entityGernericDefinition.getNotBetweenEndVal());
        EntityGernericDefinition.removeParamFromQueryParamMap(field.getName());
    }
}

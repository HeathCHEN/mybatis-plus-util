package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
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
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGenericDefinition<T, E> entityGenericDefinition) {

        QueryWrapper<T> queryWrapper = entityGenericDefinition.getQueryWrapper();
        String tableColumnName = entityGenericDefinition.getTableColumnName();

        Object notBetweenStartValue = EntityGenericDefinition.getValueFromQueryParamMap(entityGenericDefinition.getNotBetweenStartVal());
        Object notBetweenEndValue = EntityGenericDefinition.getValueFromQueryParamMap(entityGenericDefinition.getNotBetweenEndVal());

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
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void removeParam(EntityGenericDefinition<T, E> entityGenericDefinition) {
        Field field = entityGenericDefinition.getField();
        EntityGenericDefinition.removeParamFromQueryParamMap(entityGenericDefinition.getNotBetweenStartVal());
        EntityGenericDefinition.removeParamFromQueryParamMap(entityGenericDefinition.getNotBetweenEndVal());
        EntityGenericDefinition.removeParamFromQueryParamMap(field.getName());
    }
}

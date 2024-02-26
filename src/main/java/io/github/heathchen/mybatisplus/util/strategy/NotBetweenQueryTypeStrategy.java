package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.ParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ObjectUtil;
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
     * @param customerQuery CustomerQuery注解
     * @param clazz 类
     * @param field 字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {

        //将属性转为下划线格式
        String underlineCase = TableUtil.getTableColumnName(clazz,field);

        Object notBetweenStartValue = ParamThreadLocal.getValueFromObjectMap(customerQuery.notBetweenStartVal());
        Object notBetweenEndValue = ParamThreadLocal.getValueFromObjectMap(customerQuery.notBetweenEndVal());

        if (ObjectUtil.isNotNull(notBetweenStartValue)) {
            queryWrapper.le(underlineCase, notBetweenStartValue);
            ParamThreadLocal.removeParamFromObjectMap(customerQuery.notBetweenStartVal());
        }
        if (ObjectUtil.isNotNull(notBetweenEndValue)) {
            queryWrapper.ge(underlineCase, notBetweenEndValue);
            ParamThreadLocal.removeParamFromObjectMap(customerQuery.notBetweenEndVal());
        }

    }
}

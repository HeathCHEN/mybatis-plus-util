package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.ParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

public class BetweenQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.BETWEEN;

    public BetweenQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }
    /**
     * 构造查询
     * @param queryWrapper
     * @author HeathCHEN
     * 2024/02/23
     */
    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {

        //将属性转为下划线格式
        String underlineCase = TableUtil.getTableColumnName(clazz,field);

        Object startValue = ParamThreadLocal.getValueFromObjectMap(customerQuery.betweenStartVal());
        Object endValue = ParamThreadLocal.getValueFromObjectMap(customerQuery.betweenEndVal());
        
        if (ObjectUtil.isNotNull(startValue)) {
            queryWrapper.ge(underlineCase, startValue);
            ParamThreadLocal.removeParamFromObjectMap(customerQuery.betweenStartVal());
        }
        if (ObjectUtil.isNotNull(endValue)) {
            queryWrapper.le(underlineCase, endValue);
            ParamThreadLocal.removeParamFromObjectMap(customerQuery.betweenEndVal());
        }

    }
}

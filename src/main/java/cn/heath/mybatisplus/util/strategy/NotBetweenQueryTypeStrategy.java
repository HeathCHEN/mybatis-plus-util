package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.heath.mybatisplus.util.utils.ParamThreadLocal;
import cn.heath.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

public class NotBetweenQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.NOT_BETWEEN;

    public NotBetweenQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }


    /**
     * 构造查询
     * @param queryWrapper
     * @author HeathCHEN
     * 2024/02/23
     */
    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, Field field, QueryWrapper<T> queryWrapper) {

        //将属性转为下划线格式
        String underlineCase = TableUtil.getTableColumnName(field);

        Object startValue = ParamThreadLocal.getValueFromObjectMap(customerQuery.betweenStartVal());
        Object endValue = ParamThreadLocal.getValueFromObjectMap(customerQuery.betweenEndVal());

        if (ObjectUtil.isNotNull(startValue)) {
            queryWrapper.le(underlineCase, startValue);
            ParamThreadLocal.removeParamFromObjectMap(customerQuery.betweenStartVal());
        }
        if (ObjectUtil.isNotNull(endValue)) {
            queryWrapper.ge(underlineCase, endValue);
            ParamThreadLocal.removeParamFromObjectMap(customerQuery.betweenEndVal());
        }

    }
}

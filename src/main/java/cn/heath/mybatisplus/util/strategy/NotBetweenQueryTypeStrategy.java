package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;
import java.util.Map;

public class NotBetweenQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.NOT_BETWEEN;

    public NotBetweenQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, QueryWrapper<T> queryWrapper, Map<String, Object> objectMap, Object value, String[] orColumns, String underlineCase, List<String> usedProperties) {
        Object startValue = objectMap.get(customerQuery.betweenStartVal());
        Object endValue = objectMap.get(customerQuery.betweenEndVal());

        if (ObjectUtil.isNotNull(startValue)) {
            queryWrapper.le(underlineCase, startValue);
            usedProperties.add(customerQuery.betweenStartVal());
        }
        if (ObjectUtil.isNotNull(endValue)) {
            queryWrapper.ge(underlineCase, endValue);
            usedProperties.add(customerQuery.betweenEndVal());
        }

    }
}

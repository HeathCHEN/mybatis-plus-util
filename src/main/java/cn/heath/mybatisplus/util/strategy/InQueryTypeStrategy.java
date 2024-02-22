package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class InQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.IN;

    public InQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, QueryWrapper<T> queryWrapper, Map<String, Object> objectMap, Object value, String[] orColumns, String underlineCase, List<String> usedProperties) {
        if (ObjectUtil.isNotNull(value)) {
            if (value instanceof Collection) {
                Collection<?> values = (Collection<?>) value;
                if (CollectionUtil.isNotEmpty(values)) {
                    queryWrapper.in(underlineCase, values);
                }
            }

            if (value instanceof Object[]) {
                Object[] values = (Object[]) value;
                if (ArrayUtil.isNotEmpty(values)) {
                    queryWrapper.in(underlineCase, values);
                }
            }
        }

    }
}

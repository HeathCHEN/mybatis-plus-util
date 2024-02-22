package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EqQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.EQ;

    public EqQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, QueryWrapper<T> queryWrapper, Map<String, Object> objectMap, Object value, String[] orColumns, String underlineCase, List<String> usedProperties) {

        if (ObjectUtil.isNull(value)) {
            return;
        }

        if (ArrayUtil.isNotEmpty(orColumns)) {
            String tempUnderlineCase = underlineCase;
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.eq(tempUnderlineCase, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.eq(orColumn, value);
                        }
                    }
            );
        } else {
            if (ObjectUtil.isNotNull(value)) {
                queryWrapper.eq(underlineCase, value);
            }
        }
    }
}

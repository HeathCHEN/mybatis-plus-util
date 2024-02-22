package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;
import java.util.Map;

public class LessEqualQueryTypeStrategy implements QueryTypeStrategy{

    private static final QueryType QUERY_TYPE = QueryType.LESS_EQUAL;


    public LessEqualQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(),this);
    }

    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, QueryWrapper<T> queryWrapper, Map<String, Object> objectMap, Object value, String[] orColumns, String underlineCase, List<String> usedProperties) {
        if (ObjectUtil.isNull(value)) {
            return;
        }

        if (ArrayUtil.isNotEmpty(orColumns)) {
            String tempUnderlineCase = underlineCase;
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.le(tempUnderlineCase, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.le(orColumn, value);
                        }

                    }
            );
        } else {
            if (ObjectUtil.isNotNull(value)) {
                queryWrapper.le(underlineCase, value);
            }
        }
    }
}

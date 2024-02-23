package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.heath.mybatisplus.util.utils.ParamThreadLocal;
import cn.heath.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

public class LessThanQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.LESS_THAN;

    public LessThanQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {
        Object value = ParamThreadLocal.getValueFromObjectMap(field.getName());
        if (ObjectUtil.isNull(value)) {
            return;
        }
        //将属性转为下划线格式
        String underlineCase = TableUtil.getTableColumnName(clazz,field);

        String[] orColumns = customerQuery.orColumn();
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

        if (ObjectUtil.isNotNull(value)) {
            queryWrapper.lt(underlineCase, value);
        }
        ParamThreadLocal.removeParamFromObjectMap(field.getName());
    }
}

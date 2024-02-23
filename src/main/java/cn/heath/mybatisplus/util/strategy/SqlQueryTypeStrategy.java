package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.heath.mybatisplus.util.utils.ParamThreadLocal;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

public class SqlQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.SQL;

    public SqlQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }


    @Override
    public  <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {
        Object value = ParamThreadLocal.getValueFromObjectMap(field.getName());
        if (ObjectUtil.isNull(value)|| StrUtil.isBlank(customerQuery.sql())) {
            return;
        }

        queryWrapper.apply(customerQuery.sql(), value);
        ParamThreadLocal.removeParamFromObjectMap(field.getName());
    }
}

package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.heath.mybatisplus.util.utils.ParamThreadLocal;
import cn.heath.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.Collection;

public class InQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.IN;

    public InQueryTypeStrategy() {
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
        Object value = ParamThreadLocal.getValueFromObjectMap(field.getName());
        //将属性转为下划线格式
        String underlineCase = TableUtil.getTableColumnName(clazz,field);

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
        ParamThreadLocal.removeParamFromObjectMap(field.getName());
    }
}

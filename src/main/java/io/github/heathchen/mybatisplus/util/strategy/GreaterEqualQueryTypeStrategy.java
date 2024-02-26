package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.ParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * 大于等于查询策略类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class GreaterEqualQueryTypeStrategy implements QueryTypeStrategy{
    private static final QueryType QUERY_TYPE = QueryType.GREATER_EQUAL;

    public GreaterEqualQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(),this);
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
                        tQueryWrapper.ge(tempUnderlineCase, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.ge(orColumn, value);
                        }
                    }
            );
        } else {
            if (ObjectUtil.isNotNull(value)) {
                queryWrapper.ge(underlineCase, value);
            }
        }
        //加入需要剔除的已匹配的值
        ParamThreadLocal.removeParamFromObjectMap(field.getName());
    }
}

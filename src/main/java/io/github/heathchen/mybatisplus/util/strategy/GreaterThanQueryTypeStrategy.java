package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * 大于查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class GreaterThanQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.GREATER_THAN;

    public GreaterThanQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 构造查询
     *
     * @param customerQuery CustomerQuery注解
     * @param clazz         类
     * @param field         字段
     * @param queryWrapper  查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {

        Object value = QueryParamThreadLocal.getValueFromObjectMap(field.getName());

        //查询属性名对应字段名
        String tableColumnName = TableUtil.getTableColumnName(clazz, field);
        String[] orColumns = customerQuery.orColumns();
        if (ObjectUtil.isNotNull(value)) {
            if (ArrayUtil.isNotEmpty(orColumns)) {
                queryWrapper.and(tQueryWrapper -> {
                            tQueryWrapper.gt(tableColumnName, value);
                            for (String orColumn : orColumns) {
                                tQueryWrapper.or();
                                tQueryWrapper.gt(TableUtil.checkOrColumnName(orColumn), value);
                            }

                        }
                );
            } else {
                queryWrapper.gt(tableColumnName, value);
            }
        }
        QueryParamThreadLocal.removeParamFromObjectMap(field.getName());
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(customerQuery, clazz, field, tableColumnName);
    }
}
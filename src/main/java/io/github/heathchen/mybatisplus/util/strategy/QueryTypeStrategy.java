package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface QueryTypeStrategy {


    /**
     * 构造查询
     * @param customerQuery
     * @param field
     * @param queryWrapper
     * @author HeathCHEN
     * 2024/02/23
     */
    <T> void buildQuery(CustomerQuery customerQuery,
                        Class clazz,
                        Field field,
                        QueryWrapper<T> queryWrapper);
}

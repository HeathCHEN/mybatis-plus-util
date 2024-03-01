package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * 查询策略类接口
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public interface QueryTypeStrategy {


    /**
     * 构造查询
     *
     * @param <T> 查询条件适用的实体类型
     * @param queryField QueryField注解
     * @param clazz 类
     * @param field 字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    <T> void buildQuery(QueryField queryField,
                        Class clazz,
                        Field field,
                        QueryWrapper<T> queryWrapper);
}

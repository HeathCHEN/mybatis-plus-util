package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 精确匹配策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class AccurateMatchingQueryTypeStrategy {


    /**
     * 构造查询
     *
     * @param <T>          查询条件适用的实体类型
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> void buildQuery(QueryWrapper<T> queryWrapper) {
        Map<String, Object> objectMap = QueryParamThreadLocal.getObjectMap();
        if (CollectionUtil.isNotEmpty(objectMap)) {
            Set<Map.Entry<String, Object>> entries = objectMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object value = entry.getValue();
                if (ObjectUtil.isNull(value)) {
                    continue;
                }
                if (value instanceof String
                        || value instanceof Number
                        || value instanceof Boolean
                        || value instanceof Date) {
                    queryWrapper.eq(StrUtil.toUnderlineCase(entry.getKey()), value);
                    continue;
                }

                if (value instanceof Map) {
                    Map<Object, Object> map = (Map<Object, Object>) value;
                    List<Object> collect = map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
                    if (CollectionUtil.isNotEmpty(map)) {
                        queryWrapper.in(StrUtil.toUnderlineCase(entry.getKey()), collect);
                        continue;
                    }
                }
                if (value instanceof Collection) {
                    Collection<Object> collection = (Collection<Object>) value;
                    if (CollectionUtil.isNotEmpty(collection)) {
                        queryWrapper.in(StrUtil.toUnderlineCase(entry.getKey()), collection);
                        continue;
                    }
                }
                if (value.getClass().isArray()) {
                    Object[] objectArray = (Object[]) value;
                    if (ArrayUtil.isNotEmpty(objectArray)) {
                        queryWrapper.in(StrUtil.toUnderlineCase(entry.getKey()), objectArray);
                        continue;
                    }
                }

            }
        }
    }
}

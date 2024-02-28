package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;

import java.util.Map;
import java.util.Set;

/**
 * 精确匹配策略类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class AccurateMatchingQueryTypeStrategy {


    /**
     * 构造查询
     *
     * @param <T> 查询条件适用的实体类型
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> void buildQuery(QueryWrapper<T> queryWrapper) {
        Map<String, Object> objectMap = QueryParamThreadLocal.getObjectMap();
        if (CollectionUtil.isNotEmpty(objectMap)) {
            Set<Map.Entry<String, Object>> entries = objectMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                queryWrapper.eq(StrUtil.toUnderlineCase(entry.getKey()), entry.getValue());
            }
        }
    }
}

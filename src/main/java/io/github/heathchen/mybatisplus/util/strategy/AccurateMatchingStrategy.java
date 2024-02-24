package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.ParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class AccurateMatchingStrategy {


    /**
     * 构造查询
     * @param queryWrapper
     * @author HeathCHEN
     * 2024/02/23
     */
    public static <T> void buildQuery(QueryWrapper<T> queryWrapper) {
        Map<String, Object> objectMap = ParamThreadLocal.getObjectMap();
        if (CollectionUtil.isNotEmpty(objectMap)) {
            Set<Map.Entry<String, Object>> entries = objectMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                queryWrapper.eq(StrUtil.toUnderlineCase(entry.getKey()), entry.getValue());
            }
        }
    }
}

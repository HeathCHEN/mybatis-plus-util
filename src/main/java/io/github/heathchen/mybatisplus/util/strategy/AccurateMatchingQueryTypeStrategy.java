package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryConfig;
import io.github.heathchen.mybatisplus.util.config.MyBatisPlusUtilConfig;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;
import io.github.heathchen.mybatisplus.util.utils.ApplicationContextProvider;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;

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
     * 按匹配模式构造查询
     * 获取匹配模式优先级 方法参数 大于 注解上配置 大于 全局设定
     *
     * @param clazz        查询参数类
     * @param queryWrapper 查询queryWrapper
     * @param matchMode    匹配模式
     * @author HeathCHEN
     */
    public static <T> void buildQuery(Class<?> clazz, QueryWrapper<T> queryWrapper, MatchMode matchMode) {

        if (matchByMethodParam(queryWrapper,matchMode)) {
            return;
        }
        if (matchByAnnotationOnClass(clazz,queryWrapper)) {
            return;
        }
        if (matchByGlobalSetting(queryWrapper)) {
            return;
        }

    }


    /**
     * 从方法参数获取匹配模式
     * @param queryWrapper 查询queryWrapper
     * @param matchMode 匹配模式
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public static <T> Boolean matchByMethodParam( QueryWrapper<T> queryWrapper, MatchMode matchMode){
        if (ObjectUtil.isNotNull(matchMode) && MatchMode.ALL_MATCH_MODE.equals(matchMode)) {
            buildQuery(queryWrapper);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 从注解中获取匹配模式
     * @param clazz 查询参数类
     * @param queryWrapper 查询queryWrapper
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public static <T> Boolean matchByAnnotationOnClass(Class<?> clazz, QueryWrapper<T> queryWrapper){
        if (ObjectUtil.isNotNull(clazz)) {
            while (!clazz.isAnnotationPresent(QueryConfig.class)) {
                Class<?> superclass = clazz.getSuperclass();
                if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                    break;
                } else {
                    clazz = superclass;
                }
            }
            if (clazz.isAnnotationPresent(QueryConfig.class)) {
                QueryConfig queryConfig = clazz.getAnnotation(QueryConfig.class);
                if (MatchMode.ALL_MATCH_MODE.equals(queryConfig.matchMode())) {
                    buildQuery(queryWrapper);
                    return Boolean.TRUE;
                }
                if (MatchMode.ACCURATE_MATCH_MODE.equals(queryConfig.matchMode())) {
                    return Boolean.TRUE;
                }
                if (MatchMode.USING_GLOBAL_MODE.equals(queryConfig.matchMode())) {
                    return matchByGlobalSetting(queryWrapper);
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 从全局设定中获取匹配模式
     * @param queryWrapper 查询queryWrapper
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public static <T> Boolean matchByGlobalSetting(QueryWrapper<T> queryWrapper){
        MyBatisPlusUtilConfig myBatisPlusUtilConfig = ApplicationContextProvider.getBean(MyBatisPlusUtilConfig.class);
        if (ObjectUtil.isNotNull(myBatisPlusUtilConfig)) {
            if (MatchMode.ALL_MATCH_MODE.getName().equals(myBatisPlusUtilConfig.getGlobalMatchMode())) {
                buildQuery(queryWrapper);
                return Boolean.TRUE;
            }
            if (MatchMode.ACCURATE_MATCH_MODE.getName().equals(myBatisPlusUtilConfig.getGlobalMatchMode())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }




    /**
     * 构造查询
     *
     * @param <T>          查询条件适用的实体类型
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> void buildQuery(QueryWrapper<T> queryWrapper) {
        Map<String, Object> objectMap = QueryParamThreadLocal.getQueryParamMap();
        if (CollectionUtil.isNotEmpty(objectMap)) {
            Set<Map.Entry<String, Object>> entries = objectMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object value = entry.getValue();
                if (!QueryUtil.checkValue(value)) {
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

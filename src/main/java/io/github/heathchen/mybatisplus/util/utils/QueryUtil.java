package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableField;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableId;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.annotation.UniqueValue;
import io.github.heathchen.mybatisplus.util.domain.CacheGroup;
import io.github.heathchen.mybatisplus.util.strategy.QueryTypeStrategyManager;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 查询工具类
 *
 * @author HeathCHEN
 * @since 2024/02/29
 */
public class QueryUtil {


    /**
     * 校验参数是否有意义
     *
     * @param t 查询参数
     * @return {@link Boolean } 有意义返回true,否则false
     * @author HeathCHEN
     */
    public static <T> Boolean checkValue(T t) {

        if (ObjectUtil.isNull(t)) {
            return false;
        }
        //数字\日期\布尔
        if (t instanceof Number || t instanceof Boolean || t instanceof Date) {
            return true;
        }
        //文字
        if (t instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) t;
            if (StrUtil.isNotBlank(charSequence)) {
                return true;
            } else {
                return false;
            }
        }
        //Map
        if (t instanceof Map) {
            Map map = (Map) t;
            if (map.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }
        //数组
        if (t.getClass().isArray()) {
            Object[] objectArray = (Object[]) t;
            if (ArrayUtil.isNotEmpty(objectArray)) {
                return true;
            } else {
                return false;
            }
        }
        //集合
        if (t instanceof Collection) {
            Collection collection = (Collection) t;
            if (CollectionUtil.isNotEmpty(collection)) {
                return true;
            } else {
                return false;
            }
        }


        return true;
    }


    /**
     * 递归反射获取注解构建单表查询
     *
     * @param <T>          查询参数dto或实体类的类型
     * @param clazz        clazz
     * @param queryWrapper 查询包装
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> QueryWrapper<T> buildQueryByReflect(Class<?> clazz, QueryWrapper<T> queryWrapper) {
        //如果父类为空,则不再递归
        if (ObjectUtil.isNull(clazz) || ObjectUtil.equals(clazz, Object.class)) {
            return queryWrapper;
        }
        Field[] clazzDeclaredFields = clazz.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(clazzDeclaredFields)) {
            for (Field clazzDeclaredField : clazzDeclaredFields) {
                Field field = clazzDeclaredField;
                try {
                    //如果没有注解就暂且跳过,留到父类看看能否匹配,如果都没有匹配,最后做eq匹配
                    if (!field.isAnnotationPresent(QueryField.class)) {
                        continue;
                    }
                    //获取属性上的注解
                    QueryField queryField = field.getAnnotation(QueryField.class);
                    //剔除不参与的参数
                    if (!queryField.exist()) {
                        QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
                        continue;
                    }
                    //根据查询类型构建查询
                    QueryTypeStrategyManager.invokeQueryStrategy(queryField, clazz, field, queryWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //如果已匹配全部则直接返回查询,否则继续迭代
        if (CollectionUtil.isNotEmpty(QueryParamThreadLocal.getQueryParamMap())) {
            return buildQueryByReflect(clazz.getSuperclass(), queryWrapper);
        } else {
            return queryWrapper;
        }
    }


    /**
     * 递归反射获取注解构建单表查询
     *
     * @param <T>   查询参数dto或实体类的类型
     * @param clazz clazz
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> Map<String, Map<String, Object>> buildUniqueCheckQueryByReflect(Class<?> clazz, Map<String, Map<String, Object>> queryGroupMap, String... groupIds) {
        //如果父类为空,则不再递归
        if (ObjectUtil.isNull(clazz) || ObjectUtil.equals(clazz, Object.class)) {
            return queryGroupMap;
        }
        Field[] clazzDeclaredFields = clazz.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(clazzDeclaredFields)) {
            for (Field clazzDeclaredField : clazzDeclaredFields) {
                Field field = clazzDeclaredField;
                try {
                    if (field.isAnnotationPresent(UniqueValue.class)) {
                        UniqueValue uniqueValue = field.getAnnotation(UniqueValue.class);

                        if (ArrayUtil.isNotEmpty(groupIds) && !ArrayUtil.contains(groupIds, uniqueValue.value())) {
                            continue;
                        }
                        Map<String, Object> queryParamMap = queryGroupMap.get(uniqueValue.value());
                        if (ObjectUtil.isNull(queryParamMap)) {
                            queryParamMap = new HashMap<>();
                            queryGroupMap.put(uniqueValue.value(), queryParamMap);
                        }
                        //查询属性名对应字段名
                        String tableColumnName = TableUtil.getTableColumnName(clazz, field);
                        Object value = QueryParamThreadLocal.getValueFromQueryParamMap(field.getName());
                        //校验数据有效性
                        if (QueryUtil.checkValue(value)) {
                            queryParamMap.put(tableColumnName, value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }

        //如果已匹配全部则直接返回查询,否则继续迭代
        if (CollectionUtil.isNotEmpty(QueryParamThreadLocal.getQueryParamMap())) {
            return buildUniqueCheckQueryByReflect(clazz.getSuperclass(), queryGroupMap, groupIds);
        } else {
            return queryGroupMap;
        }
    }


    /**
     * 构建冗余组对象List
     *
     * @param <T>         类型
     * @param clazz       类
     * @param cacheGroups 冗余组对象List
     * @author HeathCHEN
     */
    public static <T> void constructCacheGroup(Class<T> clazz, List<CacheGroup> cacheGroups) {
        Map<String, CacheGroup> cacheGroupHashMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        if (ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(CachedTableField.class)) {
                    CachedTableField CachedTableField = field.getAnnotation(CachedTableField.class);
                    String groupId = CachedTableField.value();
                    CacheGroup cacheGroup = getCachedGroupFromMap(groupId, cacheGroupHashMap);
                    cacheGroup.addCachedTableFields(CachedTableField);
                    cacheGroup.addTableFields(TableUtil.getTableColumnName(clazz, field));
                }
                if (field.isAnnotationPresent(CachedTableId.class)) {
                    CachedTableId CachedTableId = field.getAnnotation(CachedTableId.class);
                    String groupId = CachedTableId.value();
                    CacheGroup cacheGroup = getCachedGroupFromMap(groupId, cacheGroupHashMap);
                    cacheGroup.setCachedTableId(CachedTableId);
                    cacheGroup.setTableId(TableUtil.getTableColumnName(clazz, field));
                }

            }

        }
        cacheGroupHashMap.forEach((key, value) -> cacheGroups.add(value));

    }

    /**
     * 从Map中获取自定义冗余组对象
     *
     * @param groupId           组id
     * @param cacheGroupHashMap 缓存map
     * @return {@link CacheGroup } 冗余组对象List
     * @author HeathCHEN
     */
    public static CacheGroup getCachedGroupFromMap(String groupId, Map<String, CacheGroup> cacheGroupHashMap) {
        CacheGroup cacheGroup = cacheGroupHashMap.get(groupId);

        if (ObjectUtil.isNull(cacheGroup)) {
            cacheGroup = new CacheGroup();
            cacheGroup.setGroupId(cacheGroup.getGroupId());
            cacheGroupHashMap.put(groupId, cacheGroup);

        }
        return cacheGroup;
    }

    /**
     * 清除查询和分页数据,防止内存溢出
     *
     * @author HeathCHEN
     */
    public static void cleanData() {
        QueryParamThreadLocal.cleanData();
    }

}

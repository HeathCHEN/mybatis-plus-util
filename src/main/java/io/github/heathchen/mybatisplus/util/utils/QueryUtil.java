package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableField;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableId;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.annotation.UniqueValue;
import io.github.heathchen.mybatisplus.util.domain.CacheGroup;
import io.github.heathchen.mybatisplus.util.domain.QueryContext;
import io.github.heathchen.mybatisplus.util.strategy.QueryTypeStrategyManager;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询工具类
 *
 * @author HeathCHEN
 * @since 2024/02/29
 */
public class QueryUtil {

    private static final Log log = GlobalLogFactory.get().getLog(QueryUtil.class);

    /**
     * 校验参数是否有意义
     *
     * @param queryParam 查询参数
     * @return {@link Boolean } 有意义返回true,否则false
     * @author HeathCHEN
     */
    public static <T> Boolean checkValue(T queryParam) {
        return ObjectUtil.isNotEmpty(queryParam);
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
    public static <T, E> QueryWrapper<T> buildQueryByReflect(Class<E> clazz, QueryWrapper<T> queryWrapper) {
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
                        QueryContext.removeParamFromQueryParamMap(field.getName());
                        continue;
                    }
                    QueryContext<T, E> queryContext = new QueryContext<T, E>(queryField, clazz, field, queryWrapper);
                    //根据查询类型构建查询
                    QueryTypeStrategyManager.invokeQueryStrategy(queryContext);
                } catch (Exception e) {
                    log.error("构造查询异常,类名:{},字段名:{}", clazz.getName(), field.getName());
                    e.printStackTrace();
                }
            }
        }

        //如果已匹配全部则直接返回查询,否则继续迭代
        if (CollectionUtil.isNotEmpty(QueryContext.getQueryParamMap())) {
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
    public static <T> Map<String, Map<String, Object>> buildUniqueCheckQueryByReflect(Class<?> clazz, Map<String, Map<String, Object>> queryGroupMap) {
        //如果父类为空,则不再递归
        if (ObjectUtil.isNull(clazz) || ObjectUtil.equals(clazz, Object.class)) {
            return queryGroupMap;
        }

        String[] groupIds = QueryContext.getGroupIds();
        Field[] clazzDeclaredFields = clazz.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(clazzDeclaredFields)) {
            for (Field clazzDeclaredField : clazzDeclaredFields) {
                Field field = clazzDeclaredField;
                try {
                    if (field.isAnnotationPresent(UniqueValue.class)) {
                        UniqueValue uniqueValue = field.getAnnotation(UniqueValue.class);
                        String groupId = uniqueValue.value();
                        if (StrUtil.isBlank(groupId)) {
                            groupId = field.getName();
                        }
                        if (ArrayUtil.isNotEmpty(groupIds) && !ArrayUtil.contains(groupIds, groupId)) {
                            continue;
                        }
                        Map<String, Object> queryParamMap = queryGroupMap.get(groupId);
                        if (ObjectUtil.isNull(queryParamMap)) {
                            queryParamMap = new HashMap<>();
                            queryGroupMap.put(groupId, queryParamMap);
                        }
                        //查询属性名对应字段名
                        String tableColumnName = TableUtil.getTableColumnName(clazz, field);
                        Object value = QueryContext.getValueFromQueryParamMap(field.getName());
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
        if (CollectionUtil.isNotEmpty(QueryContext.getQueryParamMap())) {
            return buildUniqueCheckQueryByReflect(clazz.getSuperclass(), queryGroupMap);
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
    public static <T> void constructCacheGroup(Class<T> clazz, List<CacheGroup> cacheGroups, String groupId) {
        Map<String, CacheGroup> cacheGroupHashMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        if (ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(CachedTableField.class)) {
                    CachedTableField CachedTableField = field.getAnnotation(CachedTableField.class);
                    String cachedTableFieldGroupId = CachedTableField.value();
                    if (StrUtil.isNotBlank(groupId) && !cachedTableFieldGroupId.equals(groupId)) {
                        continue;
                    }
                    CacheGroup cacheGroup = getCachedGroupFromMap(cachedTableFieldGroupId, cacheGroupHashMap);
                    cacheGroup.setGroupId(cachedTableFieldGroupId);
                    cacheGroup.setCachedTableField(CachedTableField);
                    cacheGroup.setPropertyFieldName(field.getName());
                }
                if (field.isAnnotationPresent(CachedTableId.class)) {
                    CachedTableId CachedTableId = field.getAnnotation(CachedTableId.class);
                    String[] groupIds = CachedTableId.value();
                    if (ArrayUtil.isNotEmpty(groupIds)) {
                        for (String cachedTableIdGroupId : groupIds) {
                            if (StrUtil.isNotBlank(groupId) && !cachedTableIdGroupId.equals(groupId)) {
                                continue;
                            }
                            CacheGroup cacheGroup = getCachedGroupFromMap(cachedTableIdGroupId, cacheGroupHashMap);
                            cacheGroup.setGroupId(cachedTableIdGroupId);
                            cacheGroup.setCachedTableId(CachedTableId);
                            cacheGroup.setTableColumnIdName(TableUtil.getTableColumnName(clazz, field));
                        }

                    }

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
        QueryContext.cleanData();
    }


    /**
     * 转行数字到BigDecimal
     *
     * @param number 数字
     * @return {@link BigDecimal }
     * @author HeathCHEN
     */
    public static BigDecimal numberToBigDecimal(Number number) {

        if (ObjectUtil.isNull(number)) {
            log.error("数字类型转换异常!");
            throw new RuntimeException("数字类型转换异常!");
        }

        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }

        if (number instanceof Long) {
            return BigDecimal.valueOf((Long) number);
        }
        if (number instanceof Integer) {
            return BigDecimal.valueOf((Integer) number);
        }
        if (number instanceof Double) {
            return BigDecimal.valueOf((Double) number);
        }
        if (number instanceof Short) {
            return BigDecimal.valueOf((Short) number);
        }

        log.error("数字类型转换异常!");
        throw new RuntimeException("数字类型转换异常!");
    }


    public static <T, E> Boolean checkIfInGroup(QueryContext<T, E> queryContext) {
        String[] groupIds = QueryContext.getGroupIds();
        String[] groupIdsOnQueryField = queryContext.getGroupId();
        boolean inGroup = Boolean.FALSE;
        if (ArrayUtil.isNotEmpty(groupIds)) {
            for (String groupId : groupIds) {
                if (ArrayUtil.contains(groupIdsOnQueryField, groupId)) {
                    inGroup = Boolean.TRUE;
                }
            }
        } else {
            inGroup = Boolean.TRUE;
        }
        return inGroup;
    }
}

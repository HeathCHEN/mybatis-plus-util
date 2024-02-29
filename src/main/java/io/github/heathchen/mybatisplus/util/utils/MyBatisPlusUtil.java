package io.github.heathchen.mybatisplus.util.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableField;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableId;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.domain.CustomerCacheGroup;
import io.github.heathchen.mybatisplus.util.strategy.AccurateMatchingQueryTypeStrategy;
import io.github.heathchen.mybatisplus.util.strategy.QueryTypeStrategyManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * MyBatisPlus工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class MyBatisPlusUtil {



    /**
     * 补全时间到当天最后时分秒毫秒
     *
     * @param date 时间
     * @return {@link Date } 时间
     * @author HeathCHEN
     */
    public static Date getNewDateTo235959999FromDate(Date date) {
        DateTime dateTime = DateUtil.dateNew(date);
        dateTime.setField(DateField.HOUR_OF_DAY, 23);
        dateTime.setField(DateField.SECOND, 59);
        dateTime.setField(DateField.MINUTE, 59);
        dateTime.setField(DateField.MILLISECOND, 999);
        return dateTime.toJdkDate();
    }

    /**
     * 置空时间到当天最开始时分秒毫秒
     *
     * @param date 时间
     * @return {@link Date } 时间
     * @author HeathCHEN
     */
    public static Date getNewDateTimeTo000000000FromDate(Date date) {
        DateTime dateTime = DateUtil.dateNew(date);
        dateTime.setField(DateField.SECOND, 0);
        dateTime.setField(DateField.MINUTE, 0);
        dateTime.setField(DateField.HOUR_OF_DAY, 0);
        dateTime.setField(DateField.MILLISECOND, 0);
        return dateTime.toJdkDate();
    }


    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param ignoreParams 忽略参数名
     * @param <T>          查询结果的返回类型
     * @param e            查询参数
     * @param clazz        返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(E e, Class<T> clazz, String... ignoreParams) {
        Class<?> tClazz = e.getClass();
        while (!clazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                tClazz = e.getClass();
                break;
            } else {
                tClazz = superclass;
            }
        }
        if (tClazz.equals(clazz)) {
            return ((List<T>) queryByReflect(e, ignoreParams));
        } else {
            List<E> list = queryByReflect(e);
            return BeanUtil.copyToList(list, clazz);
        }
    }


    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param <T>   查询结果的返回类型
     * @param e     查询参数
     * @param clazz 返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(E e, Class<T> clazz) {
        return queryByReflect(e, clazz, ArrayUtil.newArray(String.class, 0));
    }

    /**
     * 反射构筑Query后获取Bean查询
     *
     * @param ignoreParams 忽略参数名
     * @param e            查询参数
     * @param <E>          查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> List<E> queryByReflect(E e, String... ignoreParams) {
        QueryWrapper query = getQuery(e, ignoreParams);
        BaseMapper<?> baseMapper = ApplicationContextUtil.getMapperBean(e.getClass());
        if (ObjectUtil.isNotNull(baseMapper)) {
            return baseMapper.selectList(query);
        } else {
            throw new RuntimeException("can not find Mapper");
        }

    }


    /**
     * 反射构筑Query后获取Bean查询
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> List<E> queryByReflect(E e) {
        return queryByReflect(e, ArrayUtil.newArray(String.class, 0));
    }

    /**
     * 根据CustomerQuery注解构筑单表通用查询
     *
     * @param ignoreParams 忽略参数名
     * @param e            查询参数
     * @param <E>          查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQuery(E e, String... ignoreParams) {
        QueryParamThreadLocal.setObjectMap(BeanUtil.beanToMap(e, false, true));
        QueryParamThreadLocal.removeParamFromObjectMap(ignoreParams);
        Class<?> clazz = e.getClass();
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        //剔除查询参数中的分页参数
        PageHelperUtil.getPageParamFromQueryParam();
        //从类上注解获取排序字段
        PageHelperUtil.checkColumnOrderOnClass(clazz);
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = buildQueryByReflect(clazz, queryWrapper);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingQueryTypeStrategy.buildQuery(queryWrapper);
        //构筑排序
        PageHelperUtil.buildQueryOrder(queryWrapper);
        //清除查询数据
        cleanData();
        return queryWrapper;
    }

    /**
     * 根据CustomerQuery注解构筑单表通用查询
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQuery(E e) {
        return getQuery(e, ArrayUtil.newArray(String.class, 0));
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
                        QueryParamThreadLocal.removeParamFromObjectMap(field.getName());
                        continue;
                    }
                    //根据查询类型构建查询
                    QueryTypeStrategyManager.
                            getQueryTypeStrategyToManager(queryField.value().getCompareType())
                            .buildQuery(queryField, clazz, field, queryWrapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //如果已匹配全部则直接返回查询,否则继续迭代
        if (CollectionUtil.isNotEmpty(QueryParamThreadLocal.getObjectMap())) {
            return buildQueryByReflect(clazz.getSuperclass(), queryWrapper);
        } else {
            return queryWrapper;
        }
    }


    /**
     * 利用注解标注字段更新冗余字段
     *
     * @param clazz               类
     * @param associationKeyValue 关联键值
     * @param newCacheFieldValue  新的冗余值
     * @author HeathCHEN
     */
    public static <T> void updateCacheField(Class<T> clazz, Object associationKeyValue, Object newCacheFieldValue) {
        try {

            List<CustomerCacheGroup> customerCacheGroups = new ArrayList<>();
            constructCacheGroup(clazz, customerCacheGroups);

            if (CollectionUtil.isNotEmpty(customerCacheGroups)) {
                for (CustomerCacheGroup customerCacheGroup : customerCacheGroups) {
                    customerCacheGroup.checkGroupConfig();
                    BaseMapper mapperBean = ApplicationContextUtil.getMapperBean(clazz);
                    QueryWrapper<?> queryWrapper = new QueryWrapper();
                    queryWrapper.eq(customerCacheGroup.getTableId(), associationKeyValue);

                    Constructor<?> constructor = clazz.getConstructor();
                    Object o = constructor.newInstance();
                    ReflectUtil.setFieldValue(o, customerCacheGroup.getTableFields().get(0), newCacheFieldValue);

                    mapperBean.update(o, queryWrapper);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 构建冗余组对象List
     *
     * @param <T>                 类型
     * @param clazz               类
     * @param customerCacheGroups 冗余组对象List
     * @author HeathCHEN
     */
    public static <T> void constructCacheGroup(Class<T> clazz, List<CustomerCacheGroup> customerCacheGroups) {
        Map<String, CustomerCacheGroup> cacheGroupHashMap = new HashMap<>();
        Field[] declaredFields = clazz.getDeclaredFields();

        if (ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(CachedTableField.class)) {
                    CachedTableField CachedTableField = field.getAnnotation(CachedTableField.class);
                    String groupId = CachedTableField.value();
                    CustomerCacheGroup customerCacheGroup = getCustomerCacheGroupFromMap(groupId, cacheGroupHashMap);
                    customerCacheGroup.addCachedTableFields(CachedTableField);
                    customerCacheGroup.addTableFields(TableUtil.getTableColumnName(clazz, field));
                }
                if (field.isAnnotationPresent(CachedTableId.class)) {
                    CachedTableId CachedTableId = field.getAnnotation(CachedTableId.class);
                    String groupId = CachedTableId.value();
                    CustomerCacheGroup customerCacheGroup = getCustomerCacheGroupFromMap(groupId, cacheGroupHashMap);
                    customerCacheGroup.setCachedTableId(CachedTableId);
                    customerCacheGroup.setTableId(TableUtil.getTableColumnName(clazz, field));
                }

            }

        }
        cacheGroupHashMap.forEach((key, value) -> customerCacheGroups.add(value));

    }

    /**
     * 从Map中获取自定义冗余组对象
     *
     * @param groupId           组id
     * @param cacheGroupHashMap 缓存map
     * @return {@link CustomerCacheGroup } 冗余组对象List
     * @author HeathCHEN
     */
    public static CustomerCacheGroup getCustomerCacheGroupFromMap(String groupId, Map<String, CustomerCacheGroup> cacheGroupHashMap) {
        CustomerCacheGroup customerCacheGroup = cacheGroupHashMap.get(groupId);

        if (ObjectUtil.isNull(customerCacheGroup)) {
            customerCacheGroup = new CustomerCacheGroup();
            customerCacheGroup.setGroupId(customerCacheGroup.getGroupId());
            cacheGroupHashMap.put(groupId, customerCacheGroup);

        }
        return customerCacheGroup;
    }

    /**
     * 批量更新
     *
     * @param clazzArr           类Array
     * @param associationKey     关联键
     * @param newCacheFieldValue 新的冗余值
     * @author HeathCHEN
     */
    public static void updateCacheField(Object associationKey, Object newCacheFieldValue, Class<?>... clazzArr) {
        if (ArrayUtil.isNotEmpty(clazzArr)) {
            for (Class<?> clazz : clazzArr) {
                updateCacheField(clazz, associationKey, newCacheFieldValue);
            }

        }
    }

    /**
     * 清除查询和分页数据,防止内存溢出
     * @author HeathCHEN
     */
    public static void cleanData(){
        OrderAndPageParamThreadLocal.cleanData();
        QueryParamThreadLocal.cleanData();
    }

}

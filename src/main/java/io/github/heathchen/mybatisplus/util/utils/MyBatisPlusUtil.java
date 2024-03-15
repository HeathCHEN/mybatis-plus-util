package io.github.heathchen.mybatisplus.util.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.domain.*;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;
import io.github.heathchen.mybatisplus.util.strategy.AccurateMatchingQueryTypeStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * MyBatisPlus工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class MyBatisPlusUtil extends QueryContextThreadLocal {

    private static final Log log = GlobalLogFactory.get().getLog(MyBatisPlusUtil.class);

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
     * 清除分页工具的排序
     * 保留分页配置
     */
    public static void cleanPageHelperOrder() {
        PageHelper.clearPage();
        Page<Object> localPage = PageHelper.getLocalPage();
        if (ObjectUtil.isNotNull(localPage)) {
            PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());
        }
    }


    /**
     * 关闭分页
     * 只对本次查询生效
     */
    public static void closePage() {
        QueryContext.setStartPage(Boolean.FALSE);
    }

    /**
     * 关闭排序
     * 只对本次查询生效
     */
    public static void closeOrder() {
        QueryContext.setOrderColumn(Boolean.FALSE);
    }

    //===================================================================================================================================================================

    /**
     * 反射构筑CountQuery后获取Bean查询再转成对应类型
     *
     * @param countBuilder 计数查询建造者
     * @return {@link Integer}
     */
    public static <E> Integer countByReflect(CountBuilder<E> countBuilder) {
        QueryContext.setMatchMode(countBuilder.getMatchMode());
        QueryContext.setGroupIds(countBuilder.getGroupIds());
        QueryContext.setConsumer(countBuilder.getConsumer());
        QueryContext.setIgnoreParams(countBuilder.getIgnoreParams());
        QueryContext.setWithoutLike(countBuilder.getWithoutLike());
        return countByReflect(countBuilder.getE());
    }


    /**
     * 反射构筑CountQuery后获取Bean查询再转成对应类型
     *
     * @param e            查询参数
     * @param matchMode    匹配模式
     * @param ignoreParams 忽略参数名
     * @return {@link Integer}
     */
    public static <E> Integer countByReflect(E e, MatchMode matchMode, String[] groupIds, Consumer<QueryWrapper<?>> consumer, String... ignoreParams) {
        QueryContext.setConsumer(consumer);
        QueryContext.setGroupIds(groupIds);
        QueryContext.setIgnoreParams(ignoreParams);
        QueryContext.setMatchMode(matchMode);
        return countByReflect(e);
    }

    /**
     * 反射构筑CountQuery后获取Bean查询再转成对应类型
     *
     * @param e         查询参数
     * @param matchMode 匹配模式
     * @return {@link Integer}
     */
    public static <E> Integer countByReflect(E e, MatchMode matchMode) {
        QueryContext.setMatchMode(matchMode);
        return countByReflect(e);
    }

    /**
     * 反射构筑CountQuery后获取Bean查询再转成对应类型
     *
     * @param e            查询参数
     * @param ignoreParams 忽略参数名
     * @return {@link Integer}
     */
    public static <E> Integer countByReflect(E e, String... ignoreParams) {
        QueryContext.setIgnoreParams(ignoreParams);
        return countByReflect(e);
    }

    /**
     * 反射构筑CountQuery后获取Bean查询再转成对应类型
     *
     * @param e 查询参数
     * @return {@link Integer}
     */
    public static <E> Integer countByReflect(E e) {
        QueryWrapper query = getCountQueryWrapper(e);
        BaseMapper<?> baseMapper = ApplicationContextUtil.getMapperBean(e.getClass());
        return baseMapper.selectList(query).size();
    }

    //===================================================================================================================================================================

    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param queryBuilder 查询构造器
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(QueryBuilder<E, T> queryBuilder) {
        QueryContext.setConsumer(queryBuilder.getConsumer());
        QueryContext.setGroupIds(queryBuilder.getGroupIds());
        QueryContext.setIgnoreParams(queryBuilder.getIgnoreParams());
        QueryContext.setMatchMode(queryBuilder.getMatchMode());
        return queryByReflect(queryBuilder.getE(), queryBuilder.getClazz());
    }


    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param ignoreParams 忽略参数名
     * @param matchMode    匹配模式
     * @param groupIds     分组id
     * @param consumer     QueryWrapper消费者
     * @param <T>          查询结果的返回类型
     * @param e            查询参数
     * @param clazz        返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(E e, Class<T> clazz, MatchMode matchMode, String[] groupIds, Consumer<QueryWrapper<?>> consumer, String... ignoreParams) {
        QueryContext.setConsumer(consumer);
        QueryContext.setGroupIds(groupIds);
        QueryContext.setIgnoreParams(ignoreParams);
        QueryContext.setMatchMode(matchMode);
        return queryByReflect(e, clazz);

    }


    /**
     * 反射构筑Query后获取Bean查询
     *
     * @param e         查询参数
     * @param matchMode 匹配模式
     * @param <E>       查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> List<E> queryByReflect(E e, MatchMode matchMode) {
        QueryContext.setMatchMode(matchMode);
        return queryByReflect(e);

    }


    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param e        查询参数
     * @param groupIds 分组id
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> List<E> queryByReflect(E e, Collection<String> groupIds) {
        String[] groupIdArr = ArrayUtil.toArray(groupIds, String.class);
        QueryContext.setGroupIds(groupIdArr);
        return queryByReflect(e);
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
        QueryWrapper<E> query = getQueryWrapper(e);
        BaseMapper<E> baseMapper = ApplicationContextUtil.getMapperBean(e.getClass());
        return baseMapper.selectList(query);
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
        QueryContext.setIgnoreParams(ignoreParams);
        return queryByReflect(e);
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
        Class<?> tClazz = e.getClass();
        if (ObjectUtil.isNull(clazz)) {
            return ((List<T>) queryByReflect(e));
        }
        while (!tClazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = tClazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                tClazz = e.getClass();
                break;
            } else {
                tClazz = superclass;
            }
        }
        if (clazz.equals(tClazz)) {
            return ((List<T>) queryByReflect(e));
        } else {
            List<E> list = queryByReflect(e);
            return BeanUtil.copyToList(list, clazz);
        }
    }

    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param e 查询参数
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> List<E> queryByReflect(E e, Boolean withoutLike) {
        QueryContext.setWithoutLike(withoutLike);
        return queryByReflect(e);
    }


    /**
     * 反射构筑排除所有模糊查询(withoutLike为True时忽略QueryField的QueryType.Like\QueryType.LikeLEFT\QueryType.LikeRight)的Query后获取Bean查询再转成对应类型
     *
     * @param <T>         查询结果的返回类型
     * @param withoutLike 排除模糊查询
     * @param e           查询参数
     * @param clazz       返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(E e, Class<T> clazz, Boolean withoutLike) {
        QueryContext.setWithoutLike(withoutLike);
        return queryByReflect(e, clazz);
    }

    /**
     * 反射构筑Query后获取Bean查询
     *
     * @param ignoreParams 忽略参数名
     * @param groupIds     分组id
     * @param e            查询参数
     * @param <E>          查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> List<E> queryByReflect(E e, String[] groupIds, String... ignoreParams) {
        QueryContext.setGroupIds(groupIds);
        QueryContext.setIgnoreParams(ignoreParams);
        return queryByReflect(e);
    }

    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param <T>          查询结果的返回类型
     * @param ignoreParams 忽略参数名
     * @param e            查询参数
     * @param clazz        返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(E e, Class<T> clazz, String... ignoreParams) {
        QueryContext.setIgnoreParams(ignoreParams);
        return queryByReflect(e, clazz);
    }


    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param <T>      查询结果的返回类型
     * @param groupIds 分组id
     * @param e        查询参数
     * @param clazz    返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T, E> List<T> queryByReflect(E e, Class<T> clazz, Collection<String> groupIds) {
        String[] groupIdArr = ArrayUtil.toArray(groupIds, String.class);
        QueryContext.setGroupIds(groupIdArr);
        return queryByReflect(e, clazz);
    }


    //===================================================================================================================================================================

    /**
     * 反射构筑Query后校验被UniqueValue标注的字段是否超过limit
     *
     * @param checkUniqueBuilder 检查唯一查询构造器
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> Boolean checkUniqueByReflect(CheckUniqueBuilder<E> checkUniqueBuilder) {
        QueryContext.setLimitValue(checkUniqueBuilder.getLimit());
        QueryContext.setConsumer(checkUniqueBuilder.getConsumer());
        QueryContext.setGroupIds(checkUniqueBuilder.getGroupIds());
        return checkUniqueByReflect(checkUniqueBuilder.getE());

    }

    /**
     * 反射构筑Query后校验被UniqueValue标注的字段是否超过limit
     *
     * @param e        查询参数
     * @param limit    最多个数
     * @param groupIds 指定分组id
     * @param <E>      查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> Boolean checkUniqueByReflect(E e, Integer limit, Consumer<QueryWrapper<?>> consumer, String... groupIds) {
        QueryContext.setLimitValue(limit);
        QueryContext.setConsumer(consumer);
        QueryContext.setGroupIds(groupIds);
        return checkUniqueByReflect(e);
    }

    /**
     * 反射构筑Query后校验被UniqueValue标注的字段是否唯一
     *
     * @param e        查询参数
     * @param limit    最多个数
     * @param groupIds 指定分组id
     * @param <E>      查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> Boolean checkUniqueByReflect(E e, Integer limit, String... groupIds) {
        QueryContext.setLimitValue(limit);
        QueryContext.setGroupIds(groupIds);
        return checkUniqueByReflect(e);
    }

    /**
     * 反射构筑Query后校验被UniqueValue标注的字段是否唯一
     *
     * @param e     查询参数
     * @param limit 最多个数
     * @param <E>   查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> Boolean checkUniqueByReflect(E e, Integer limit) {
        QueryContext.setLimitValue(limit);
        return checkUniqueByReflect(e);
    }

    /**
     * 反射构筑Query后校验被UniqueValue标注的字段是否唯一
     *
     * @param e        查询参数
     * @param groupIds 指定分组id
     * @param <E>      查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> Boolean checkUniqueByReflect(E e, String... groupIds) {
        QueryContext.setGroupIds(groupIds);
        return checkUniqueByReflect(e);
    }

    /**
     * 反射构筑Query后校验被UniqueValue标注的字段是否唯一
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <E> Boolean checkUniqueByReflect(E e) {
        QueryWrapper<E> checkUniqueQueryWrapper = getCheckUniqueQueryWrapper(e);
        Integer limit = QueryContext.getLimitValue();
        BaseMapper<E> baseMapper = ApplicationContextUtil.getMapperBean(e.getClass());
        int count = baseMapper.selectList(checkUniqueQueryWrapper).size();
        if (ObjectUtil.isNull(limit)) {
            limit = 1;
        }
        BigDecimal countBigDecimal = QueryUtil.numberToBigDecimal(count);
        BigDecimal limitBigDecimal = QueryUtil.numberToBigDecimal(limit);
        return countBigDecimal.compareTo(limitBigDecimal) <= 0;

    }


    //===================================================================================================================================================================

    /**
     * 将结果集合转为Map结构
     *
     * @param list 查询结果集合
     * @return {@link Map }  key为id vaLue为实体类
     * @author HeathCHEN
     */
    public static <K, T> Map<K, T> listToDataMapping(List<T> list) {
        Map<K, T> result = new HashMap<>();
        if (CollectionUtil.isEmpty(list)) {
            return result;
        }
        T t = list.get(0);
        Class<?> clazz = t.getClass();
        while (!clazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                clazz = t.getClass();
                break;
            }
            clazz = superclass;
        }
        Field field = null;
        Field[] declaredFields = clazz.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(declaredFields)) {
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(TableId.class)) {
                    field = declaredField;
                    break;
                }
            }
        }
        if (ObjectUtil.isNull(field)) {
            throw new IllegalArgumentException("仅支持MyBatisPlus实体类");
        }
        String fieldName = field.getName();
        String methodName = "get" + StrUtil.upperFirst(fieldName);
        Method getMethod = ReflectUtil.getMethodByName(clazz, methodName);
        result = list.stream().collect(Collectors.toMap(t1 -> {
            K k = null;
            try {
                k = (K) getMethod.invoke(t1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return k;
        }, Function.identity()));
        return result;
    }

    //===================================================================================================================================================================

    /**
     * 根据QueryField注解构筑单表通用查询
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E e) {
        QueryContext.setQueryParamMap(BeanUtil.beanToMap(e, false, true));
        QueryContext.removeParamFromQueryParamMap();
        Class<?> clazz = e.getClass();
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        //剔除查询参数中的分页参数
        PageHelperUtil.getPageParamFromQueryParam();
        //从类上注解获取排序字段
        PageHelperUtil.checkColumnOrderOnClass(clazz);
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = QueryUtil.buildQueryByReflect(clazz, queryWrapper);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingQueryTypeStrategy.buildQuery(clazz, queryWrapper);
        Consumer<QueryWrapper<?>> consumer = QueryContext.getConsumer();
        if (ObjectUtil.isNotNull(consumer)) {
            consumer.accept(queryWrapper);
        }

        //构筑排序
        PageHelperUtil.buildQueryOrder(queryWrapper);
        //清除查询数据
        QueryUtil.cleanData();
        return queryWrapper;
    }


    /**
     * 根据QueryField注解构筑单表Count查询
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getCountQueryWrapper(E e) {
        String[] ignoreParams = QueryContext.getIgnoreParams();
        QueryContext.setQueryParamMap(BeanUtil.beanToMap(e, false, true));
        QueryContext.removeParamFromQueryParamMap(ignoreParams);
        Class<?> clazz = e.getClass();
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = QueryUtil.buildQueryByReflect(clazz, queryWrapper);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingQueryTypeStrategy.buildQuery(clazz, queryWrapper);
        Consumer<QueryWrapper<?>> consumer = QueryContext.getConsumer();
        if (ObjectUtil.isNotNull(consumer)) {
            consumer.accept(queryWrapper);
        }

        //清除查询数据
        QueryUtil.cleanData();
        return queryWrapper;
    }

    /**
     * 根据UniqueValue注解构筑单表查询
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getCheckUniqueQueryWrapper(E e) {
        QueryContext.setQueryParamMap(BeanUtil.beanToMap(e, false, true));
        Consumer<QueryWrapper<?>> consumer = QueryContext.getConsumer();
        Class<?> clazz = e.getClass();
        Map<String, Map<String, Object>> queryGroupMap = new HashMap<>();
        //遍历map然后从子级逐级反射获得注解判断比较类型
        QueryUtil.buildUniqueCheckQueryByReflect(clazz, queryGroupMap);

        QueryWrapper<E> queryWrapper = new QueryWrapper<>();

        if (CollectionUtil.isNotEmpty(queryGroupMap)) {
            if (queryGroupMap.size() == 1) {
                Iterator<Map<String, Object>> iterator = queryGroupMap.values().iterator();
                Map<String, Object> queryParamMap = iterator.next();
                if (CollectionUtil.isNotEmpty(queryParamMap)) {
                    queryParamMap.forEach((key, value) -> queryWrapper.eq(key, value));
                }
            }
        } else {
            queryWrapper.and(tQueryWrapper -> {
                Iterator<Map<String, Object>> iterator = queryGroupMap.values().iterator();
                while (iterator.hasNext()) {
                    Map<String, Object> queryParamMap = iterator.next();
                    if (CollectionUtil.isNotEmpty(queryParamMap)) {
                        queryParamMap.forEach((key, value) -> queryWrapper.eq(key, value));
                    }
                    if (iterator.hasNext()) {
                        queryWrapper.or();
                    }
                }
            });
        }

        if (ObjectUtil.isNotNull(consumer)) {
            consumer.accept(queryWrapper);
        }

        //清除查询数据
        QueryUtil.cleanData();
        return queryWrapper;
    }

    /**
     * 根据QueryField注解构筑单表通用查询
     *
     * @param e            查询参数
     * @param ignoreParams 忽略参数名
     * @param <E>          查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E e, String... ignoreParams) {
        QueryContext.setIgnoreParams(ignoreParams);
        return getQueryWrapper(e);
    }

    /**
     * 根据QueryField注解构筑单表通用查询
     *
     * @param e           查询参数
     * @param withoutLike 是否排除模糊查询
     * @return {@link QueryWrapper}
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E e, Boolean withoutLike) {
        QueryContext.setWithoutLike(withoutLike);
        return getQueryWrapper(e);
    }


    /**
     * 根据QueryField注解构筑单表通用查询
     * 新方法名getQueryWrapper()
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    @Deprecated
    @SuppressWarnings("depracated method! please use getQueryWrapper() or queryByReflect() ")
    public static <E> QueryWrapper<E> getQuery(E e) {
        return getQueryWrapper(e);
    }

    /**
     * 根据QueryField注解构筑单表通用查询
     *
     * @param e         查询参数
     * @param matchMode 匹配模式
     * @param <E>       查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E e, MatchMode matchMode) {
        QueryContext.setMatchMode(matchMode);
        return getQueryWrapper(e);
    }

    //===================================================================================================================================================================

    /**
     * 利用注解标注字段更新冗余字段
     *
     * @param clazz               需要刚更新冗余值的类
     * @param groupId             分组id
     * @param associationKeyValue 关联键值
     * @param newCacheFieldValue  新的冗余值
     * @author HeathCHEN
     */
    public static <T> void updateCacheField(Class<T> clazz, Object associationKeyValue, Object newCacheFieldValue, String groupId) {
        try {

            List<CacheGroup> cacheGroups = new ArrayList<>();
            QueryUtil.constructCacheGroup(clazz, cacheGroups, groupId);

            if (CollectionUtil.isNotEmpty(cacheGroups)) {
                for (CacheGroup cacheGroup : cacheGroups) {
                    cacheGroup.checkGroupConfig();
                    BaseMapper mapperBean = ApplicationContextUtil.getMapperBean(clazz);
                    QueryWrapper<?> queryWrapper = new QueryWrapper();
                    queryWrapper.eq(cacheGroup.getTableColumnIdName(), associationKeyValue);

                    Constructor<?> constructor = clazz.getConstructor();
                    Object o = constructor.newInstance();
                    ReflectUtil.setFieldValue(o, cacheGroup.getPropertyFieldName(), newCacheFieldValue);

                    mapperBean.update(o, queryWrapper);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用注解标注字段更新冗余字段
     *
     * @param clazz               需要刚更新冗余值的类
     * @param associationKeyValue 关联键值
     * @param newCacheFieldValue  新的冗余值
     * @author HeathCHEN
     */
    public static <T> void updateCacheField(Class<T> clazz, Object associationKeyValue, Object newCacheFieldValue) {
        updateCacheField(clazz, associationKeyValue, newCacheFieldValue);
    }

    /**
     * 批量更新
     *
     * @param clazzArr           需要刚更新冗余值的类Array
     * @param groupId            分组id
     * @param associationKey     关联键
     * @param newCacheFieldValue 新的冗余值
     * @author HeathCHEN
     */
    public static void updateCacheField(Object associationKey, Object newCacheFieldValue, String groupId, Class<?>... clazzArr) {
        if (ArrayUtil.isNotEmpty(clazzArr)) {
            for (Class<?> clazz : clazzArr) {
                updateCacheField(clazz, associationKey, newCacheFieldValue, groupId);
            }

        }
    }


}

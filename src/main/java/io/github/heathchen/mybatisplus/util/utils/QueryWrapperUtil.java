package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;
import io.github.heathchen.mybatisplus.util.strategy.AccurateMatchingQueryTypeStrategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class QueryWrapperUtil {

    /**
     * 根据QueryField注解构筑单表通用查询
     *
     * @param e   查询参数
     * @param <E> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E e) {
        String[] ignoreParams = QueryContextThreadLocal.getIgnoreParams();
        Consumer<QueryWrapper<?>> consumer = QueryContextThreadLocal.getConsumer();
        String[] groupIds = QueryContextThreadLocal.getGroupIds();
        MatchMode matchMode = QueryContextThreadLocal.getMatchMode();

        QueryContextThreadLocal.setQueryParamMap(BeanUtil.beanToMap(e, false, true));
        QueryContextThreadLocal.removeParamFromQueryParamMap(ignoreParams);
        Class<?> clazz = e.getClass();
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        //剔除查询参数中的分页参数
        PageHelperUtil.getPageParamFromQueryParam();
        //从类上注解获取排序字段
        PageHelperUtil.checkColumnOrderOnClass(clazz);
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = QueryUtil.buildQueryByReflect(clazz, queryWrapper, groupIds);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingQueryTypeStrategy.buildQuery(clazz, queryWrapper, matchMode);

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
     * 根据QueryField注解构筑单表通用查询
     *
     * @param e            查询参数
     * @param ignoreParams 忽略参数名
     * @param <E>          查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <E> QueryWrapper<E> getQueryWrapper(E e, String... ignoreParams) {
        QueryContextThreadLocal.setIgnoreParams(ignoreParams);
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
        QueryContextThreadLocal.setMatchMode(matchMode);
        return getQueryWrapper(e);
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
        String[] ignoreParams = QueryContextThreadLocal.getIgnoreParams();
        Consumer<QueryWrapper<?>> consumer = QueryContextThreadLocal.getConsumer();
        String[] groupIds = QueryContextThreadLocal.getGroupIds();
        MatchMode matchMode = QueryContextThreadLocal.getMatchMode();
        QueryContextThreadLocal.setQueryParamMap(BeanUtil.beanToMap(e, false, true));
        QueryContextThreadLocal.removeParamFromQueryParamMap(ignoreParams);
        Class<?> clazz = e.getClass();
        QueryWrapper<E> queryWrapper = new QueryWrapper<E>();
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = QueryUtil.buildQueryByReflect(clazz, queryWrapper, groupIds);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingQueryTypeStrategy.buildQuery(clazz, queryWrapper, matchMode);

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
    public static <E> QueryWrapper getCheckUniqueQueryWrapper(E e) {
        QueryContextThreadLocal.setQueryParamMap(BeanUtil.beanToMap(e, false, true));
        Consumer<QueryWrapper<?>> consumer = QueryContextThreadLocal.getConsumer();
        String[] groupIds = QueryContextThreadLocal.getGroupIds();
        Class<?> clazz = e.getClass();

        Map<String, Map<String, Object>> queryGroupMap = new HashMap<>();
        //遍历map然后从子级逐级反射获得注解判断比较类型
        QueryUtil.buildUniqueCheckQueryByReflect(clazz, queryGroupMap, groupIds);

        QueryWrapper queryWrapper = new QueryWrapper<>();

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

}

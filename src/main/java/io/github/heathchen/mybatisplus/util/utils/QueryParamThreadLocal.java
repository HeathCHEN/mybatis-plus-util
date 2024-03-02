package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.heathchen.mybatisplus.util.consts.PageAndOrderConst;
import io.github.heathchen.mybatisplus.util.domain.OrderDto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 存放查询参数线程工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class QueryParamThreadLocal {

    private static final ThreadLocal<Map<String, Object>> QUERY_PARAM_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> ORDER_AND_PAGE_PARAM_LOCAL = new ThreadLocal<>();

    private QueryParamThreadLocal() {
    }

    /**
     * 清空数据,防止内存溢出
     *
     * @author HeathCHEN
     */
    public static void cleanData() {
        QUERY_PARAM_LOCAL.remove();
        ORDER_AND_PAGE_PARAM_LOCAL.remove();
    }

    /**
     * 获取全部查询参数
     *
     * @return {@link Map }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Map<String, Object> getQueryParamMap() {
        if (ObjectUtil.isNull(QUERY_PARAM_LOCAL.get())) {
            QUERY_PARAM_LOCAL.set(new HashMap<>());
        }
        return QUERY_PARAM_LOCAL.get();
    }

    /**
     * 设置查询参数到线程中
     *
     * @param data 查询参数的map
     * @author HeathCHEN
     * @since 2024/02/23
     */
    public static void setQueryParamMap(Map<String, Object> data) {
        if (ObjectUtil.isNull(QUERY_PARAM_LOCAL.get())) {
            QUERY_PARAM_LOCAL.set(new HashMap<>());
        }
        QUERY_PARAM_LOCAL.set(data);
    }

    /**
     * 获取查询参数
     *
     * @param key 查询参数的属性名
     * @return {@link Object }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Object getValueFromQueryParamMap(String key) {
        return QUERY_PARAM_LOCAL.get().get(key);
    }


    /**
     * 移除查询参数
     *
     * @param keys 查询参数的属性名
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static void removeParamFromQueryParamMap(String... keys) {
        if (ArrayUtil.isNotEmpty(keys)) {
            Map<String, Object> objectMap = getQueryParamMap();
            for (String key : keys) {
                objectMap.remove(key);
            }
        }
    }


    /**
     * 设置排序参数到线程中
     *
     * @param data 查询参数的map
     * @author HeathCHEN
     * @since 2024/02/23
     */
    public static void setOrderAndPageParamMap(Map<String, Object> data) {
        ORDER_AND_PAGE_PARAM_LOCAL.set(data);
    }

    /**
     * 获取排序参数
     *
     * @param key 查询参数的属性名
     * @return {@link Object }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Object getValueFromOrderAndPageParamMap(String key) {
        if (ObjectUtil.isNull(ORDER_AND_PAGE_PARAM_LOCAL.get())) {
            ORDER_AND_PAGE_PARAM_LOCAL.set(new HashMap<>());
        }
        return ORDER_AND_PAGE_PARAM_LOCAL.get().get(key);
    }

    /**
     * 设置排序参数
     *
     * @param key  查询参数的属性名
     * @param data 查询参数
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static void setValueToOrderAndPageParamMap(String key, Object data) {
        if (ObjectUtil.isNull(ORDER_AND_PAGE_PARAM_LOCAL.get())) {
            ORDER_AND_PAGE_PARAM_LOCAL.set(new HashMap<>());
        }
        ORDER_AND_PAGE_PARAM_LOCAL.get().put(key, data);
    }

    /**
     * 获取全部查询参数
     *
     * @return {@link Map }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Map<String, Object> getObjectMap() {
        return ORDER_AND_PAGE_PARAM_LOCAL.get();
    }

    public static Map<String, OrderDto> getOrderMap() {
        Map<String, OrderDto> map = (Map<String, OrderDto>) getValueFromOrderAndPageParamMap(PageAndOrderConst.ORDER_MAP);
        if (CollectionUtil.isEmpty(map)) {
            Map<String, OrderDto> newMap = new HashMap<>();
            setValueToOrderAndPageParamMap(PageAndOrderConst.ORDER_MAP, newMap);
            map = newMap;
        }
        return map;
    }

    public static List<OrderDto> getOrderList() {

        return getOrderMap().
                values().
                stream().
                sorted(Comparator.comparingInt(OrderDto::getOrderPriority)).
                collect(Collectors.toList());
    }

    /**
     * 放入排序列表
     *
     * @param orderDto
     * @author HeathCHEN
     */
    public static void putOrderDtoIntoOrderList(OrderDto orderDto) {
        Map<String, OrderDto> orderMap = getOrderMap();
        orderMap.putIfAbsent(orderDto.getTableColumnName(), orderDto);
    }

    /**
     * 如果不存在则放入排序列表
     *
     * @param orderDto 排序对象
     * @author HeathCHEN
     */
    public static void putIntoOrderListIfOrderDtoAbsent(OrderDto orderDto) {
        Map<String, OrderDto> orderMap = getOrderMap();
        if (orderMap.containsKey(orderDto.getTableColumnName())) {
            return;
        }
        orderMap.putIfAbsent(orderDto.getTableColumnName(), orderDto);
    }

    public static Boolean getStartPage() {
        Boolean startPage = (Boolean) getValueFromOrderAndPageParamMap(PageAndOrderConst.START_PAGE);
        if (ObjectUtil.isNull(startPage)) {
            startPage = Boolean.TRUE;
            setStartPage(startPage);
        }
        return startPage;
    }

    public static void setStartPage(Boolean startPage) {
        setValueToOrderAndPageParamMap(PageAndOrderConst.START_PAGE, startPage);
    }

}

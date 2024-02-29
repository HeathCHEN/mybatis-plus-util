package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.heathchen.mybatisplus.util.consts.PageAndOrderConst;
import io.github.heathchen.mybatisplus.util.domain.QueryConfigDto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 存放排序和分页参数线程工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * 2024/02/26
 */
public class OrderAndPageParamThreadLocal {

    private OrderAndPageParamThreadLocal() {
    }

    private static final ThreadLocal<Map<String, Object>> LOCAL = new ThreadLocal<>();

    /**
     * 清空数据,防止内存溢出
     * @author HeathCHEN
     */
    public static void cleanData(){
        LOCAL.set(null);
    }


    /**
     * 设置排序参数到线程中
     *
     * @param data 查询参数的map
     * @author HeathCHEN
     * @since 2024/02/23
     */
    public static void setObjectMap(Map<String, Object> data) {
        LOCAL.set(data);
    }

    /**
     * 获取排序参数
     *
     * @param key 查询参数的属性名
     * @return {@link Object }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Object getValueFromObjectMap(String key) {
        if (ObjectUtil.isNull(LOCAL.get())) {
            LOCAL.set(new HashMap<>());
        }
        return LOCAL.get().get(key);
    }

    /**
     * 设置排序参数
     *
     * @param key  查询参数的属性名
     * @param data 查询参数
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static void setValueToObjectMap(String key, Object data) {
        if (ObjectUtil.isNull(LOCAL.get())) {
            LOCAL.set(new HashMap<>());
        }
        LOCAL.get().put(key, data);
    }

    /**
     * 获取全部查询参数
     *
     * @return {@link Map }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Map<String, Object> getObjectMap() {
        return LOCAL.get();
    }

    public static Map<String, QueryConfigDto> getOrderMap() {
        Map<String, QueryConfigDto> map = (Map<String, QueryConfigDto>) getValueFromObjectMap(PageAndOrderConst.ORDER_MAP);
        if (CollectionUtil.isEmpty(map)) {
            Map<String, QueryConfigDto> newMap = new HashMap<>();
            setValueToObjectMap(PageAndOrderConst.ORDER_MAP, newMap);
            map = newMap;
        }
        return map;
    }

    public static List<QueryConfigDto> getOrderList() {

        return getOrderMap().
                values().
                stream().
                sorted(Comparator.comparingInt(QueryConfigDto::getOrderPriority)).
                collect(Collectors.toList());
    }

    public static void putQueryConfigDtoIntoOrderList(QueryConfigDto QueryConfigDto) {
        Map<String, QueryConfigDto> orderMap = getOrderMap();
        orderMap.put(QueryConfigDto.getTableColumnName(), QueryConfigDto);
    }

    public static void setStartPage(Boolean startPage) {
        setValueToObjectMap(PageAndOrderConst.START_PAGE, startPage);
    }

    public static Boolean getStartPage() {
        Boolean startPage = (Boolean) getValueFromObjectMap(PageAndOrderConst.START_PAGE);
        if (ObjectUtil.isNull(startPage)) {
            startPage = Boolean.TRUE;
            setStartPage(startPage);
        }
        return startPage;
    }

}

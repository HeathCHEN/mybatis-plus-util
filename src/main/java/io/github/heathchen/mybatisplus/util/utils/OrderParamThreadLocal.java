package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.heathchen.mybatisplus.util.consts.PageConst;
import io.github.heathchen.mybatisplus.util.domain.CustomerOrderDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 存放排序参数线程工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * 2024/02/26
 */
public class OrderParamThreadLocal {

    private OrderParamThreadLocal() {
    }

    private static final ThreadLocal<Map<String, Object>> LOCAL = new ThreadLocal<>();


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
        return LOCAL.get().get(key);
    }

    /**
     * 设置排序参数
     *
     * @param key  查询参数的属性名
     * @param data 查询参数
     * @return {@link Object }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Object setValueToObjectMap(String key, Object data) {
        return LOCAL.get().put(key, data);
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

    public static List<CustomerOrderDto> getOrderList() {
        List<CustomerOrderDto> list = (List<CustomerOrderDto>) getValueFromObjectMap(PageConst.ORDER_LIST);
        if (CollectionUtil.isEmpty(list)) {
            List<CustomerOrderDto> newList = new ArrayList<>();
            setValueToObjectMap(PageConst.ORDER_LIST, newList);
            list = newList;
        }
        return list;
    }

    public static void putCustomerOrderDtoIntoOrderList(CustomerOrderDto customerOrderDto) {
        List<CustomerOrderDto> orderList = getOrderList();
        orderList.add(customerOrderDto);
    }

    public static void setStartPage(Boolean startPage) {
        setValueToObjectMap(PageConst.START_PAGE, startPage);
    }

    public static Boolean getStartPage() {
        Boolean startPage = (Boolean) getValueFromObjectMap(PageConst.START_PAGE);
        if (ObjectUtil.isNull(startPage)) {
            startPage = Boolean.TRUE;
            setStartPage(startPage);
        }
        return startPage;
    }

}

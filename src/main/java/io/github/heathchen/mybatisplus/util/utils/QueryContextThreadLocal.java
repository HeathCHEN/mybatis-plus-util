package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.config.MyBatisPlusUtilConfig;
import io.github.heathchen.mybatisplus.util.consts.MyBatisPlusUtilConst;
import io.github.heathchen.mybatisplus.util.consts.PageAndOrderConst;
import io.github.heathchen.mybatisplus.util.domain.OrderDto;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 存放查询参数线程工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class QueryContextThreadLocal {

    private static final ThreadLocal<Map<String, Object>> QUERY_PARAM_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> ORDER_AND_PAGE_PARAM_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> QUERY_CONFIG_LOCAL = new ThreadLocal<>();
    private static MyBatisPlusUtilConfig myBatisPlusUtilConfig;


    public QueryContextThreadLocal() {
        myBatisPlusUtilConfig = ApplicationContextProvider.getBean(MyBatisPlusUtilConfig.class);
    }

    /**
     * 清空数据,防止内存溢出
     *
     * @author HeathCHEN
     */
    public static void cleanData() {
        QUERY_PARAM_LOCAL.remove();
        ORDER_AND_PAGE_PARAM_LOCAL.remove();
        QUERY_CONFIG_LOCAL.remove();
    }


    /**
     * 获取查询配置Map
     *
     * @return {@link Map }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Map<String, Object> getQueryConfigMap() {
        if (ObjectUtil.isNull(QUERY_CONFIG_LOCAL.get())) {
            QUERY_CONFIG_LOCAL.set(new HashMap<>());
        }
        return QUERY_CONFIG_LOCAL.get();
    }

    /**
     * 获取忽略参数
     *
     * @author HeathCHEN
     */
    public static String[] getIgnoreParams() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        String[] ignoreParams = (String[]) queryConfigMap.get(MyBatisPlusUtilConst.IGNORE_PARAMS);
        if (ArrayUtil.isEmpty(ignoreParams)) {
            ignoreParams = new String[0];
        }
        return ignoreParams;
    }

    /**
     * 设置忽略参数
     *
     * @param ignoreParams 忽略参数
     * @author HeathCHEN
     */
    public static void setIgnoreParams(String... ignoreParams) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.IGNORE_PARAMS, ignoreParams);
    }

    /**
     * 获取最大个数
     *
     * @author HeathCHEN
     */
    public static Integer getLimitValue() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        Integer limit = (Integer) queryConfigMap.get(MyBatisPlusUtilConst.LIMIT_VALUE);
        if (ObjectUtil.isNull(limit)) {
            limit = 1;
        }
        return limit;
    }

    /**
     * 设置最大个数
     *
     * @param limit 忽略参数
     * @author HeathCHEN
     */
    public static void setLimitValue(Integer limit) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.LIMIT_VALUE, limit);
    }

    /**
     * 获取匹配模式
     *
     * @author HeathCHEN
     */
    public static MatchMode getMatchMode() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        MatchMode matchMode = (MatchMode) queryConfigMap.get(MyBatisPlusUtilConst.MATCH_MODE);
        if (ObjectUtil.isNull(matchMode)) {
            matchMode = MatchMode.USING_GLOBAL_MODE;
        }
        return matchMode;
    }

    /**
     * 设置匹配模式
     *
     * @param matchMode 匹配模式
     * @author HeathCHEN
     */
    public static void setMatchMode(MatchMode matchMode) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.MATCH_MODE, matchMode);
    }

    /**
     * 获取匹配模式
     *
     * @author HeathCHEN
     */
    public static Consumer<QueryWrapper<?>> getConsumer() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        Consumer<QueryWrapper<?>> consumer = (Consumer<QueryWrapper<?>>) queryConfigMap.get(MyBatisPlusUtilConst.CONSUMER);
        return consumer;
    }

    /**
     * 设置消费者
     *
     * @param consumer QueryWrapper消费者
     * @author HeathCHEN
     */
    public static void setConsumer(Consumer<QueryWrapper<?>> consumer) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.CONSUMER, consumer);
    }

    /**
     * 获取分组参数
     *
     * @author HeathCHEN
     */
    public static String[] getGroupIds() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        String[] groupIds = (String[]) queryConfigMap.get(MyBatisPlusUtilConst.GROUP_IDS);
        if (ArrayUtil.isEmpty(groupIds)) {
            groupIds = new String[0];
        }
        return groupIds;
    }

    /**
     * 设置分组ID
     *
     * @param groupIds 分组ID
     * @author HeathCHEN
     */
    public static void setGroupIds(String... groupIds) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.GROUP_IDS, groupIds);
    }

    /**
     * 获取排除模糊查询参数
     *
     * @author HeathCHEN
     */
    public static Boolean getWithoutLike() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        Boolean withoutLike = (Boolean) queryConfigMap.get(MyBatisPlusUtilConst.WITHOUT_LIKE);
        if (ObjectUtil.isNull(withoutLike)) {
            withoutLike = Boolean.FALSE;
        }
        return withoutLike;
    }

    /**
     * 设置排除模糊查询参数
     *
     * @param withoutLike 排除模糊查询参数
     * @author HeathCHEN
     */
    public static void setWithoutLike(Boolean withoutLike) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.WITHOUT_LIKE, withoutLike);
    }

    /**
     * 获取返回类型
     *
     * @author HeathCHEN
     */
    public static Class<?> getReturnClassType() {
        Map<String, Object> queryConfigMap = getQueryConfigMap();
        Class<?> clzz = (Class) queryConfigMap.get(MyBatisPlusUtilConst.RETURN_CLASS_TYPE);
        return clzz;
    }

    /**
     * 设置返回类型
     *
     * @param clazz 返回类型
     * @author HeathCHEN
     */
    public static <T> void setReturnClassType(Class<T> clazz) {
        putDataIntoQueryConfigMap(MyBatisPlusUtilConst.RETURN_CLASS_TYPE, clazz);
    }

    /**
     * 将参数放入配置Map
     *
     * @param key   键
     * @param value 值
     * @author HeathCHEN
     */
    public static void putDataIntoQueryConfigMap(String key, Object value) {
        if (ObjectUtil.isNotEmpty(value)) {
            Map<String, Object> queryConfigMap = getQueryConfigMap();
            if (value instanceof Map) {
                Map<Object, Object> map = (Map<Object, Object>) value;
                if (CollectionUtil.isNotEmpty(map)) {
                    queryConfigMap.put(key, value);
                    return;
                }
            }
            if (value instanceof Collection) {
                Collection<Object> collection = (Collection<Object>) value;
                if (CollectionUtil.isNotEmpty(collection)) {
                    queryConfigMap.put(key, value);
                    return;
                }
            }
            if (value.getClass().isArray()) {
                Object[] objectArray = (Object[]) value;
                if (ArrayUtil.isNotEmpty(objectArray)) {
                    queryConfigMap.put(key, value);
                    return;
                }
            }
            queryConfigMap.put(key, value);
        }

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
        if (ObjectUtil.isNotEmpty(data)) {
            QUERY_PARAM_LOCAL.set(data);
        }
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

        if (ObjectUtil.isNotEmpty(data)) {
            ORDER_AND_PAGE_PARAM_LOCAL.get().put(key, data);
        }
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

    /**
     * 获取排序Map
     *
     * @return {@link Map }
     * @author HeathCHEN
     */
    public static Map<String, OrderDto> getOrderMap() {
        Map<String, OrderDto> map = (Map<String, OrderDto>) getValueFromOrderAndPageParamMap(PageAndOrderConst.ORDER_MAP);
        if (CollectionUtil.isEmpty(map)) {
            Map<String, OrderDto> newMap = new HashMap<>();
            setValueToOrderAndPageParamMap(PageAndOrderConst.ORDER_MAP, newMap);
            map = newMap;
        }
        return map;
    }

    /**
     * 获取排序List
     *
     * @return {@link List }<{@link OrderDto }>
     * @author HeathCHEN
     */
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

    /**
     * 获取是否开启分页参数
     *
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public static Boolean getStartPage() {
        Boolean startPage = (Boolean) getValueFromOrderAndPageParamMap(myBatisPlusUtilConfig.getStarPagePropertyName());
        if (ObjectUtil.isNull(startPage)) {
            startPage = myBatisPlusUtilConfig.getStarPagePropertyDefaultValue();
        }
        return startPage;
    }

    /**
     * 设置是否分页参数
     *
     * @param startPage 是否开启分页
     * @author HeathCHEN
     */
    public static void setStartPage(Object startPage) {
        if (startPage instanceof Boolean) {
            setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getStarPagePropertyName(), startPage);
        }
        if (startPage instanceof String) {
            try {
                String startPageString = (String) startPage;
                if (StrUtil.isNotBlank(startPageString)) {
                    setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getStarPagePropertyName(), Boolean.valueOf(startPageString));
                }
            } catch (Exception e) {
            }
        }
    }


    /**
     * 获取排序顺序参数
     *
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public static Boolean getIsAsc() {
        Boolean isAsc = (Boolean) getValueFromOrderAndPageParamMap(myBatisPlusUtilConfig.getIsAscPropertyName());
        if (ObjectUtil.isNull(isAsc)) {
            isAsc = myBatisPlusUtilConfig.getAscPropertyDefaultValue();
        }
        return isAsc;
    }

    /**
     * 设置排序顺序参数
     *
     * @param isAsc 排序顺序
     * @author HeathCHEN
     */
    public static void setIsAsc(Object isAsc) {
        if (isAsc instanceof Boolean) {
            setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getIsAscPropertyName(), isAsc);
        }
        if (isAsc instanceof String) {
            try {
                String isAscString = (String) isAsc;
                if (StrUtil.isNotBlank(isAscString)) {
                    setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getIsAscPropertyName(), Boolean.valueOf(isAscString));
                }
            } catch (Exception e) {
            }
        }
    }


    /**
     * 获取页面大小参数
     *
     * @return {@link Integer }
     * @author HeathCHEN
     */
    public static Integer getPageSize() {
        Integer pageSize = (Integer) getValueFromOrderAndPageParamMap(myBatisPlusUtilConfig.getPageSizePropertyName());
        if (ObjectUtil.isNull(pageSize)) {
            pageSize = myBatisPlusUtilConfig.getPageSizePropertyDefaultValue();
        }
        return pageSize;
    }

    /**
     * 设置页面大小参数
     *
     * @param pageSize 页面大小
     * @author HeathCHEN
     */
    public static void setPageSize(Object pageSize) {
        if (pageSize instanceof Integer) {
            setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getPageSizePropertyName(), pageSize);
        }
        if (pageSize instanceof String) {
            try {
                String pageSizeString = (String) pageSize;
                if (StrUtil.isNotBlank(pageSizeString)) {
                    setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getPageSizePropertyName(), Integer.valueOf(pageSizeString));
                }
            } catch (Exception e) {
            }
        }
    }


    /**
     * 获取分页页码参数
     *
     * @return {@link Integer }
     * @author HeathCHEN
     */
    public static Integer getPageNum() {
        Integer pageNum = (Integer) getValueFromOrderAndPageParamMap(myBatisPlusUtilConfig.getPageNumPropertyName());
        if (ObjectUtil.isNull(pageNum)) {
            pageNum = myBatisPlusUtilConfig.getPageNumPropertyDefaultValue();
        }
        return pageNum;
    }


    /**
     * 设置分页页码参数
     *
     * @param pageNum 分页页码
     * @author HeathCHEN
     */
    public static void setPageNum(Object pageNum) {
        if (pageNum instanceof Integer) {
            setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getPageNumPropertyName(), pageNum);
        }
        if (pageNum instanceof String) {
            try {
                String pageNumString = (String) pageNum;
                if (StrUtil.isNotBlank(pageNumString)) {
                    setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getPageNumPropertyName(), Integer.valueOf(pageNumString));
                }
            } catch (Exception e) {
            }
        }
    }


    /**
     * 获取排序字段参数
     *
     * @return {@link String }
     * @author HeathCHEN
     */
    public static String getOrderByColumn() {
        String orderByColumn = (String) getValueFromOrderAndPageParamMap(myBatisPlusUtilConfig.getOrderByColumnPropertyName());
        if (StrUtil.isBlank(orderByColumn)) {
            orderByColumn = myBatisPlusUtilConfig.getOrderByColumnPropertyDefaultValue();
        }
        return orderByColumn;
    }


    /**
     * 设置排序字段参数
     *
     * @param orderByColumn 排序字段
     * @author HeathCHEN
     */
    public static void setOrderByColumn(Object orderByColumn) {
        String orderByColumnString = orderByColumn.toString();
        if (StrUtil.isNotBlank(orderByColumnString)) {
            setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getOrderByColumnPropertyName(), orderByColumnString);
        }

    }


    /**
     * 获取是否分页合理参数
     *
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public static Boolean getReasonable() {
        Boolean reasonable = (Boolean) getValueFromOrderAndPageParamMap(myBatisPlusUtilConfig.getReasonablePropertyName());
        if (ObjectUtil.isNull(reasonable)) {
            reasonable = myBatisPlusUtilConfig.getReasonablePropertyDefaultValue();
        }
        return reasonable;
    }

    /**
     * 设置是否分页合理参数
     *
     * @param reasonable 是否分页合理
     * @author HeathCHEN
     */
    public static void setReasonable(Object reasonable) {
        if (reasonable instanceof Boolean) {
            setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getReasonablePropertyName(), reasonable);
        }
        if (reasonable instanceof String) {
            try {
                String reasonableString = (String) reasonable;
                if (StrUtil.isNotBlank(reasonableString)) {
                    setValueToOrderAndPageParamMap(myBatisPlusUtilConfig.getReasonablePropertyName(), Boolean.valueOf(reasonableString));
                }
            } catch (Exception e) {
            }
        }
    }

}

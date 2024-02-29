package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.annotation.QueryConfig;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.consts.PageAndOrderConst;
import io.github.heathchen.mybatisplus.util.domain.OrderDto;
import io.github.heathchen.mybatisplus.util.enums.OrderType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 分页插件工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class PageHelperUtil {


    public static void getPageParamFromQueryParam() {
        OrderAndPageParamThreadLocal.setStartPage((Boolean) QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.START_PAGE));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.IS_ASC, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.IS_ASC));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.PAGE_SIZE, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_SIZE));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.PAGE_NUM, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_NUM));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.ORDER_BY_COLUMN, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.ORDER_BY_COLUMN));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.REASONABLE, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.REASONABLE));
        QueryParamThreadLocal.removeParamFromObjectMap(PageAndOrderConst.START_PAGE,
                PageAndOrderConst.IS_ASC,
                PageAndOrderConst.PAGE_SIZE,
                PageAndOrderConst.PAGE_NUM,
                PageAndOrderConst.ORDER_BY_COLUMN,
                PageAndOrderConst.REASONABLE);
    }


    /**
     * 检查是否排序
     *
     * @param clazz 类
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnClass(Class<?> clazz) {
        //清除分页插件的排序参数 使用该注解分页
        Boolean startPage = OrderAndPageParamThreadLocal.getStartPage();
        PageHelper.clearPage();
        if (startPage) {
            com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
            if (ObjectUtil.isNotNull(localPage)) {
                PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());
            } else {
                Integer pageSize = (Integer) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_SIZE);
                Integer pageNum = (Integer) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_NUM);
                if (ObjectUtil.isNotNull(pageSize) && ObjectUtil.isNotNull(pageNum)) {
                    PageHelper.startPage(pageNum, pageSize);
                }
            }
        }
        while (!clazz.isAnnotationPresent(QueryConfig.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                break;
            } else {
                clazz = superclass;
            }
        }
        if (!clazz.isAnnotationPresent(QueryConfig.class)) {
            return;
        }
        QueryConfig QueryConfig = clazz.getDeclaredAnnotation(QueryConfig.class);
        String[] columns = QueryConfig.orderColumnNames();
        OrderType[] orderTypes = QueryConfig.orderTypes();
        boolean orderColumn = QueryConfig.orderColumn();

        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.ORDER_COLUMN, orderColumn);

        if (ArrayUtil.isNotEmpty(columns) && orderColumn) {
            for (int i = 0; i < columns.length; i++) {
                OrderDto OrderDto = new OrderDto();
                if (StrUtil.isBlank(columns[i])) {
                    continue;
                }
                OrderDto.setTableColumnName(columns[i].toUpperCase());
                if (ObjectUtil.isNull(orderTypes[i])) {
                    continue;
                }

                OrderDto.setOrderType(orderTypes[i]);
                OrderDto.setOrderPriority(i + 1);
                OrderAndPageParamThreadLocal.putOrderDtoIntoOrderList(OrderDto);
            }

        }
    }


    /**
     * 检查是否使用排序
     *
     * @param queryField 注解CustomerQuery
     * @param field         字段
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnField(QueryField queryField, Class<?> clazz, Field field, String tableColumnName) {

        if (queryField.orderType().equals(OrderType.NONE)) {
            return;
        }
        OrderDto OrderDto = new OrderDto();
        OrderDto.setTableColumnName(tableColumnName.toUpperCase());
        OrderDto.setOrderPriority(queryField.orderPriority());
        OrderDto.setOrderType(queryField.orderType());
        OrderDto.setField(field);
        OrderDto.setClazz(clazz);
        OrderAndPageParamThreadLocal.putIntoOrderListIfOrderDtoAbsent(OrderDto);


    }

    /**
     * 构筑排序
     *
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static void buildQueryOrder(QueryWrapper<?> queryWrapper) {

        Boolean orderColumn = (Boolean) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.ORDER_COLUMN);

        if (ObjectUtil.isNotNull(orderColumn) && !orderColumn) {
            return;
        }

        String orderByColumn = (String) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.ORDER_BY_COLUMN);
        String isAsc = (String) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.IS_ASC);

        if (StrUtil.isNotBlank(orderByColumn) && StrUtil.isNotBlank(isAsc)) {
            OrderDto OrderDto = new OrderDto();
            if (isAsc.equals(Boolean.TRUE.toString())) {
                OrderDto.setOrderType(OrderType.ASC);
            } else {
                OrderDto.setOrderType(OrderType.DESC);
            }
            OrderDto.setTableColumnName(TableUtil.checkOrColumnName(orderByColumn).toUpperCase());
            OrderDto.setOrderPriority(-1);
            OrderAndPageParamThreadLocal.putOrderDtoIntoOrderList(OrderDto);
        }

        List<OrderDto> orderList = OrderAndPageParamThreadLocal.getOrderList();

        //对查询进行排序
        if (CollectionUtil.isNotEmpty(orderList)) {
            for (OrderDto OrderDto : orderList) {
                String tableColumnName = OrderDto.getTableColumnName();
                OrderType orderType = OrderDto.getOrderType();
                if (ObjectUtil.isNotNull(orderType) && orderType.equals(OrderType.ASC)) {
                    queryWrapper.orderByAsc(tableColumnName);
                }
                if (ObjectUtil.isNotNull(orderType) && orderType.equals(OrderType.DESC)) {
                    queryWrapper.orderByDesc(tableColumnName);
                }
            }

        }

    }


}

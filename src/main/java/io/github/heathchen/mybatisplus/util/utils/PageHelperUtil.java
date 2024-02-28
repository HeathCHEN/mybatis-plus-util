package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.annotation.CustomerOrder;
import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.consts.PageConst;
import io.github.heathchen.mybatisplus.util.domain.CustomerOrderDto;
import io.github.heathchen.mybatisplus.util.enums.OrderType;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分页插件工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class PageHelperUtil {


    public static void getPageParamFromQueryParam() {
        OrderParamThreadLocal.setStartPage((Boolean) QueryParamThreadLocal.getValueFromObjectMap(PageConst.START_PAGE));
        OrderParamThreadLocal.setValueToObjectMap(PageConst.IS_ASC, QueryParamThreadLocal.getValueFromObjectMap(PageConst.IS_ASC));
        OrderParamThreadLocal.setValueToObjectMap(PageConst.PAGE_SIZE, QueryParamThreadLocal.getValueFromObjectMap(PageConst.PAGE_SIZE));
        OrderParamThreadLocal.setValueToObjectMap(PageConst.PAGE_NUM, QueryParamThreadLocal.getValueFromObjectMap(PageConst.PAGE_NUM));
        OrderParamThreadLocal.setValueToObjectMap(PageConst.ORDER_BY_COLUMN, QueryParamThreadLocal.getValueFromObjectMap(PageConst.ORDER_BY_COLUMN));
        QueryParamThreadLocal.removeParamFromObjectMap(PageConst.START_PAGE,PageConst.IS_ASC,PageConst.PAGE_SIZE,PageConst.PAGE_NUM,PageConst.ORDER_BY_COLUMN);
    }


    /**
     * 检查是否排序
     *
     * @param clazz 类
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(CustomerOrder.class)) {
            return;
        }
        CustomerOrder customerOrder = clazz.getDeclaredAnnotation(CustomerOrder.class);
        String[] columns = customerOrder.orderColumnNames();
        OrderType[] orderTypes = customerOrder.orderTypes();
        boolean orderColumn = customerOrder.orderColumn();
        if (ArrayUtil.isNotEmpty(columns) && orderColumn) {
            for (int i = 0; i < columns.length; i++) {
                CustomerOrderDto customerOrderDto = new CustomerOrderDto();
                if (StrUtil.isBlank(columns[i])) {
                    continue;
                }
                customerOrderDto.setTableColumnName(columns[i]);
                if (ObjectUtil.isNull(orderTypes[i])) {
                    continue;
                }
                customerOrderDto.setOrderType(orderTypes[i]);
                customerOrderDto.setOrderPriority(i + 1);
                OrderParamThreadLocal.putCustomerOrderDtoIntoOrderList(customerOrderDto);
            }
            //清除分页插件的排序参数 使用该注解分页
            com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
            PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());
        }
    }

    /**
     * 检查是否使用排序
     *
     * @param customerQuery 注解CustomerQuery
     * @param field         字段
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnField(CustomerQuery customerQuery, Class<?> clazz, Field field, String tableColumnName) {

        if (ObjectUtil.isNull(customerQuery.orderType())) {
            return;
        }
        CustomerOrderDto customerOrderDto = new CustomerOrderDto();
        customerOrderDto.setTableColumnName(tableColumnName);
        customerOrderDto.setOrderPriority(customerQuery.orderPriority());
        customerOrderDto.setOrderType(customerQuery.orderType());
        customerOrderDto.setField(field);
        customerOrderDto.setClazz(clazz);
        OrderParamThreadLocal.putCustomerOrderDtoIntoOrderList(customerOrderDto);


    }

    /**
     * 构筑排序
     *
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static void buildQueryOrder(QueryWrapper<?> queryWrapper) {
        Boolean startPage = OrderParamThreadLocal.getStartPage();
        List<CustomerOrderDto> orderList = OrderParamThreadLocal.getOrderList();

        //对查询进行排序
        if (startPage && CollectionUtil.isNotEmpty(orderList)) {
            List<CustomerOrderDto> customerOrderDtos = orderList.stream().sorted(Comparator.comparingInt(CustomerOrderDto::getOrderPriority)).collect(Collectors.toList());
            for (CustomerOrderDto customerOrderDto : customerOrderDtos) {
                String tableColumnName = customerOrderDto.getTableColumnName();
                if (ObjectUtil.isNotNull(customerOrderDto.getOrderType()) && customerOrderDto.getOrderType().equals(OrderType.ASC)) {
                    queryWrapper.orderByAsc(tableColumnName);
                }
                if (ObjectUtil.isNotNull(customerOrderDto.getOrderType()) && customerOrderDto.getOrderType().equals(OrderType.DESC)) {
                    queryWrapper.orderByDesc(tableColumnName);
                }
            }

        }

    }


}

package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.domain.CustomerOrder;
import io.github.heathchen.mybatisplus.util.enums.OrderType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 分页插件工具类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class PageHelperUtil {

    /**
     * 检查是否排序
     *
     * @param clazz     类
     * @param orderList 排序List
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnClass(Class<?> clazz, List<CustomerOrder> orderList) {
        CustomerQuery customerQuery = clazz.getDeclaredAnnotation(CustomerQuery.class);
        if (ObjectUtil.isNotNull(customerQuery)) {
            String[] columns = customerQuery.orderColumns();
            OrderType[] orderTypes = customerQuery.orderTypes();

            if (ArrayUtil.isNotEmpty(columns)) {
                for (int i = 0; i < columns.length; i++) {
                    CustomerOrder customerOrder = new CustomerOrder();
                    customerOrder.setOrderColumn(columns[i]);
                    customerOrder.setOrderType(orderTypes[i]);
                    customerOrder.setOrderPriority(i + 1);
                    orderList.add(customerOrder);
                }

                //清除分页插件的排序参数 使用该注解分页
                com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
                PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());

            }
        }


    }

    /**
     * 检查是否使用排序
     *
     * @param customerQuery 注解CustomerQuery
     * @param field         字段
     * @param orderList     排序List
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnField(CustomerQuery customerQuery, Field field, List<CustomerOrder> orderList) {
        //检测是否启用排序
        if (customerQuery.orderColumn()) {
            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setOrderColumn(field.getName());
            customerOrder.setOrderPriority(customerQuery.orderPriority());
            customerOrder.setOrderType(customerQuery.orderType());
            orderList.add(customerOrder);

        }
    }


}

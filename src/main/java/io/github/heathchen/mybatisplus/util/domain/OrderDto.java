package io.github.heathchen.mybatisplus.util.domain;

import io.github.heathchen.mybatisplus.util.enums.OrderType;

import java.lang.reflect.Field;

/**
 * 自定义排序
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2023/07/24
 */
public class OrderDto {

    /**
     * 排序优先级
     */

    private Integer orderPriority;

    /**
     * 排序字段
     */

    private String tableColumnName;

    /**
     * 排序类型
     */

    private OrderType orderType;

    /**
     * 类名
     */

    private Class clazz;

    /**
     * 字段名
     */
    private Field field;




    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName = tableColumnName;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}

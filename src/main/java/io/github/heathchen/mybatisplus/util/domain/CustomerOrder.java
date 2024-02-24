package io.github.heathchen.mybatisplus.util.domain;

import io.github.heathchen.mybatisplus.util.enums.OrderType;

/**
 * 自定义排序
 *
 * @author HeathCHEN
 * @version 1.0
 * 2023/07/24
 */
public class CustomerOrder {

    /**
     * 排序优先级
     */

    private Integer orderPriority;

    /**
     * 排序字段
     */

    private String orderColumn;

    /**
     * 排序类型
     */

    private OrderType orderType;


    public Integer getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(Integer orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}

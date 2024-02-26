package io.github.heathchen.mybatisplus.util.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.util.StringUtil;
import io.github.heathchen.mybatisplus.util.annotation.CustomerCacheTableField;
import io.github.heathchen.mybatisplus.util.annotation.CustomerCacheTableId;

import java.util.ArrayList;
import java.util.List;

/**
 * 冗余字段组
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class CustomerCacheGroup {


    /**
     * 自定义冗余字段关联键
     */
    CustomerCacheTableId customerCacheTableId;


    /**
     * 自定义冗余字段注解List
     */
    List<CustomerCacheTableField> customerCacheTableFields ;


    /**
     * 关联键
     */
    String tableId;

    /**
     * 冗余字段List
     */
    List<String> tableFields;


    /**
     * 组id
     */
    String groupId;


    public CustomerCacheTableId getCustomerCacheTableId() {
        return customerCacheTableId;
    }

    public void setCustomerCacheTableId(CustomerCacheTableId customerCacheTableId) {
        this.customerCacheTableId = customerCacheTableId;
    }

    public List<CustomerCacheTableField> getCustomerCacheTableFields() {
        if (CollectionUtil.isEmpty(customerCacheTableFields)) {
            customerCacheTableFields = new ArrayList<>();
        }
        return customerCacheTableFields;
    }



    public void addCustomerCacheTableFields(CustomerCacheTableField customerCacheTableField) {

        customerCacheTableFields.add(customerCacheTableField);
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<String> getTableFields() {
        if (CollectionUtil.isEmpty(tableFields)) {
            tableFields = new ArrayList<>();
        }
        return tableFields;
    }

    public void setCustomerCacheTableFields(List<CustomerCacheTableField> customerCacheTableFields) {
        this.customerCacheTableFields = customerCacheTableFields;
    }

    public void setTableFields(List<String> tableFields) {
        this.tableFields = tableFields;
    }

    public void addTableFields(String tableField) {
        tableFields.add(tableField);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void checkGroupConfig() throws Exception {
        if (StringUtil.isEmpty(tableId)||CollectionUtil.isEmpty(tableFields)) {
            throw new Exception("注解配置异常!");
        }


    }
}

package io.github.heathchen.mybatisplus.util.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.util.StringUtil;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableField;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableId;

import java.util.ArrayList;
import java.util.List;

/**
 * 冗余字段组
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class CacheGroup {


    /**
     * 自定义冗余字段关联键
     */
    CachedTableId CachedTableId;


    /**
     * 自定义冗余字段注解List
     */
    List<CachedTableField> CachedTableFields ;


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


    public CachedTableId getCachedTableId() {
        return CachedTableId;
    }

    public void setCachedTableId(CachedTableId CachedTableId) {
        this.CachedTableId = CachedTableId;
    }

    public List<CachedTableField> getCachedTableFields() {
        if (CollectionUtil.isEmpty(CachedTableFields)) {
            CachedTableFields = new ArrayList<>();
        }
        return CachedTableFields;
    }



    public void addCachedTableFields(CachedTableField CachedTableField) {

        CachedTableFields.add(CachedTableField);
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

    public void setCachedTableFields(List<CachedTableField> CachedTableFields) {
        this.CachedTableFields = CachedTableFields;
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

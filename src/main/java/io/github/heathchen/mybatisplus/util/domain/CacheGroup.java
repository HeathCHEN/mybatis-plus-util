package io.github.heathchen.mybatisplus.util.domain;

import com.github.pagehelper.util.StringUtil;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableField;
import io.github.heathchen.mybatisplus.util.annotation.CachedTableId;

/**
 * 冗余字段组
 *
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
    CachedTableField cachedTableField;


    /**
     * 关联键对应表字段名
     */
    String tableColumnIdName;

    /**
     * 冗余值对应表字段名
     */
    String tableColumnFieldName;
    /**
     * 冗余值对应属性名
     */
    String propertyFieldName;

    /**
     * 组id
     */
    String groupId;


    public CachedTableId getCachedTableId() {
        return CachedTableId;
    }

    public void setCachedTableId(CachedTableId cachedTableId) {
        this.CachedTableId = cachedTableId;
    }

    public CachedTableField getCachedTableField() {
        return cachedTableField;
    }

    public void setCachedTableField(CachedTableField cachedTableField) {
        this.cachedTableField = cachedTableField;
    }


    public String getTableColumnIdName() {
        return tableColumnIdName;
    }

    public void setTableColumnIdName(String tableColumnIdName) {
        this.tableColumnIdName = tableColumnIdName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTableColumnFieldName() {
        return tableColumnFieldName;
    }

    public void setTableColumnFieldName(String tableColumnFieldName) {
        this.tableColumnFieldName = tableColumnFieldName;
    }

    public String getPropertyFieldName() {
        return propertyFieldName;
    }

    public void setPropertyFieldName(String propertyFieldName) {
        this.propertyFieldName = propertyFieldName;
    }

    public void checkGroupConfig() throws Exception {
        if (StringUtil.isEmpty(tableColumnIdName) || StringUtil.isEmpty(propertyFieldName)) {
            throw new Exception("注解配置异常!");
        }


    }
}

package io.github.heathchen.mybatisplus.util.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.utils.QueryContextThreadLocal;

import java.lang.reflect.Field;

/**
 * 查询配置上下文
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/15
 */
public class QueryContext<T, E> extends QueryContextThreadLocal {

    /**
     * 查询QueryWrapper
     */
    private QueryWrapper<T> queryWrapper;

    /**
     * 查询QueryField注解
     */
    private QueryField queryField;

    /**
     * 查询参数类
     */
    private Class<E> clazz;

    /**
     * 查询参数值
     */
    private Object queryParam;

    /**
     * 查询参数属性
     */
    private Field field;

    /**
     * 表字段名
     */
    private String tableColumnName;

    public QueryContext(QueryField queryField, Class<E> clazz, Field field, QueryWrapper<T> queryWrapper) {
        this.queryWrapper = queryWrapper;
        this.queryField = queryField;
        this.clazz = clazz;
        this.field = field;
    }

    public QueryWrapper<T> getQueryWrapper() {
        return queryWrapper;
    }

    public void setQueryWrapper(QueryWrapper<T> queryWrapper) {
        this.queryWrapper = queryWrapper;
    }

    public QueryField getQueryField() {
        return queryField;
    }

    public void setQueryField(QueryField queryField) {
        this.queryField = queryField;
    }

    public Object getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(Object queryParam) {
        this.queryParam = queryParam;
    }

    public String getTableColumnName() {
        return tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName = tableColumnName;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class<E> getClazz() {
        return clazz;
    }

    public void setClazz(Class<E> clazz) {
        this.clazz = clazz;
    }
}

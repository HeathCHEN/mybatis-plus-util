package io.github.heathchen.mybatisplus.util.definiton;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.JoinType;
import io.github.heathchen.mybatisplus.util.enums.OrderType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;

import java.lang.reflect.Field;

/**
 * 查询配置上下文
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/15
 */
public class EntityGenericDefinition<T, E> extends EntityDefinitionThreadLocal implements EntityDefinition {


    private AnnotationReader annotationReader;


    public EntityGenericDefinition() {

    }


    /**
     * 查询QueryWrapper
     */
    private QueryWrapper<T> queryWrapper;

    /**
     * 查询类型
     */
    private QueryType queryType;

    /**
     * or 查询, 用于匹配多个字段
     */
    private String[] orColumns;
    /**
     * 数据库表中实际的字段名
     */
    private String columnName;
    /**
     * 排序顺序
     */
    private OrderType orderType;
    /**
     * 排序优先级
     */
    private int orderPriority;
    /**
     * 用于between查询的字段名
     */
    private String betweenStartVal;
    /**
     * 用于between查询的字段名
     */
    private String betweenEndVal;
    /**
     * 用于between查询的字段名
     */
    private String notBetweenStartVal;
    /**
     * 用于between查询的字段名
     */
    private String notBetweenEndVal;
    /**
     * 是否数据库字段参与查询或排序
     */
    private boolean exist;
    /**
     * 字段值类型
     */
    private ConditionType conditionType;
    /**
     * 表连接类型
     */
    private JoinType joinType;
    /**
     * 表连接实体类
     */
    private Class<?> joinEntityClass;
    /**
     * 自动义SQL
     */
    private String sql;

    /**
     * 分组id 用于分组查询
     */
    private String[] groupId;
    /**
     * 查询参数类
     */
    private Class<E> paramClass;

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

    public EntityGenericDefinition(QueryField queryField, Class<E> paramClass, Field field, QueryWrapper<T> queryWrapper) {
        this.queryWrapper = queryWrapper;
        this.queryType = queryField.value();
        this.orColumns = queryField.orColumns();
        this.columnName = queryField.columnName();
        this.orderType = queryField.orderType();
        this.orderPriority = queryField.orderPriority();
        this.orColumns = queryField.orColumns();
        this.betweenStartVal = queryField.betweenStartVal();
        this.betweenEndVal = queryField.betweenEndVal();
        this.notBetweenStartVal = queryField.notBetweenStartVal();
        this.notBetweenEndVal = queryField.notBetweenEndVal();
        this.exist = queryField.exist();
        this.conditionType = queryField.conditionType();
        this.joinType = queryField.joinType();
        this.joinEntityClass = queryField.joinEntityClass();
        this.orColumns = queryField.orColumns();
        this.sql = queryField.sql();
        this.groupId = queryField.groupId();
        this.paramClass = paramClass;
        this.field = field;
    }

    public QueryWrapper<T> getQueryWrapper() {
        return queryWrapper;
    }

    public void setQueryWrapper(QueryWrapper<T> queryWrapper) {
        this.queryWrapper = queryWrapper;
    }


    public String[] getOrColumns() {
        return orColumns;
    }

    public void setOrColumns(String[] orColumns) {
        this.orColumns = orColumns;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public int getOrderPriority() {
        return orderPriority;
    }

    public void setOrderPriority(int orderPriority) {
        this.orderPriority = orderPriority;
    }

    public String getBetweenStartVal() {
        return betweenStartVal;
    }

    public void setBetweenStartVal(String betweenStartVal) {
        this.betweenStartVal = betweenStartVal;
    }

    public String getBetweenEndVal() {
        return betweenEndVal;
    }

    public void setBetweenEndVal(String betweenEndVal) {
        this.betweenEndVal = betweenEndVal;
    }

    public String getNotBetweenStartVal() {
        return notBetweenStartVal;
    }

    public void setNotBetweenStartVal(String notBetweenStartVal) {
        this.notBetweenStartVal = notBetweenStartVal;
    }

    public String getNotBetweenEndVal() {
        return notBetweenEndVal;
    }

    public void setNotBetweenEndVal(String notBetweenEndVal) {
        this.notBetweenEndVal = notBetweenEndVal;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public Class<?> getJoinEntityClass() {
        return joinEntityClass;
    }

    public void setJoinEntityClass(Class<?> joinEntityClass) {
        this.joinEntityClass = joinEntityClass;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getGroupId() {
        return groupId;
    }

    public void setGroupId(String[] groupId) {
        this.groupId = groupId;
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

    public Class<E> getParamClass() {
        return paramClass;
    }

    public void setParamClass(Class<E> paramClass) {
        this.paramClass = paramClass;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }
}

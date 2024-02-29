package io.github.heathchen.mybatisplus.util.annotation;


import io.github.heathchen.mybatisplus.util.consts.MyBatisPlusUtilConst;
import io.github.heathchen.mybatisplus.util.enums.JoinType;
import io.github.heathchen.mybatisplus.util.enums.OrderType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义查询注解
 * 放在requestDto类上或者实体类的成员变量上
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface QueryField {


    /**
     * 查询类型 默认精确匹配
     * @return {@link QueryType }
     * @author HeathCHEN
     */
    QueryType value() default QueryType.EQ;


    /**
     * or 查询, 用于匹配多个字段
     * 对IN\NOT_IN\BETWEEN\NOT_BETWEEN\SQL等QueryType不生效
     * @return {@link String[] }
     * @author HeathCHEN
     */
    String[] orColumns() default {};


    /**
     * 数据库表中实际的字段名 默认为空
     * 如果该属性非空则默认以该字段查询数据库
     * @return {@link String }
     * @author HeathCHEN
     */
    String columnName() default "";


    /**
     * 排序顺序 默认自然排序
     * @return {@link OrderType }
     * @author HeathCHEN
     */
    OrderType orderType() default OrderType.NONE;

    /**
     * 排序优先级 默认优先级最高
     * @return int
     * @author HeathCHEN
     */
    int orderPriority() default 0;


    /**
     * 用于between查询的字段名 大于等于
     * 默认字段为 startTime
     * @return {@link String }
     * @author HeathCHEN
     */
    String betweenStartVal() default MyBatisPlusUtilConst.START_TIME;


    /**
     * 用于between查询的字段名 小于等于
     * 默认字段为 endTime
     * @return {@link String }
     * @author HeathCHEN
     */
    String betweenEndVal() default MyBatisPlusUtilConst.END_TIME;


    /**
     *  用于between查询的字段名 不大于等于
     *  默认字段为 startTime
     * @return {@link String }
     * @author HeathCHEN
     */
    String notBetweenStartVal() default MyBatisPlusUtilConst.START_TIME;


    /**
     * 用于between查询的字段名 不小于等于
     * 默认字段为 endTime
     * @return {@link String }
     * @author HeathCHEN
     */
    String notBetweenEndVal() default MyBatisPlusUtilConst.END_TIME;


    /**
     * 是否数据库字段参与查询或排序
     * @return boolean
     * @author HeathCHEN
     */
    boolean exist() default true;


    /**
     * 表连接类型 默认左连接
     * @return {@link JoinType }
     * @author HeathCHEN
     */
    JoinType joinType() default JoinType.LEFT_JOIN;


    /**
     * 表连接实体类
     * @return {@link Class }
     * @author HeathCHEN
     */
    Class<?> joinEntityClass() default Object.class;


    /**
     * 自动义SQL
     * @return {@link String }
     * @author HeathCHEN
     */
    String sql() default "";


}

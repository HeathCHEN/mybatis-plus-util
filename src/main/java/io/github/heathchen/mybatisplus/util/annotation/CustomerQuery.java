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
 * @author HeathCHEN
 * @version 1.0
 * 2023/07/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CustomerQuery {

    /**
     * 查询类型 默认精确匹配
     * @return {@link QueryType }
     * @author HeathCHEN
     * 2024/02/26
     */
    QueryType value() default QueryType.EQ;


    /**
     *  or 查询, 用于匹配多个字段
     *  对IN\NOT_IN\BETWEEN\NOT_BETWEEN\SQL等QueryType不生效
     * @return {@link String[] }
     * @author HeathCHEN
     * 2024/02/26
     */
    String[] orColumns() default {};


    /**
     * 数据库中实际的字段名 默认为空
     * 如果该属性非空则默认以该字段查询数据库
     * @return {@link String }
     * @author HeathCHEN
     * 2024/02/26
     */
    String columnName();

    /**
     * 排序顺序 默认自然排序
     * @return {@link OrderType }
     * @author HeathCHEN
     * 2024/02/26
     */
    OrderType orderType();


    /**
     * 排序优先级 默认优先级最高
     * @return int
     * @author HeathCHEN
     * 2024/02/26
     */
    int orderPriority() default 0;


    /**
     * 用于between的字段名 大于等于
     * 默认字段为 startTime
     * @return {@link String }
     * @author HeathCHEN
     * 2024/02/26
     */
    String betweenStartVal() default MyBatisPlusUtilConst.START_TIME;


    /**
     * 用于between的字段名 小于等于
     * 默认字段为 endTime
     * @return {@link String }
     * @author HeathCHEN
     * 2024/02/26
     */
    String betweenEndVal() default MyBatisPlusUtilConst.END_TIME;


    /**
     * 用于between的字段名 不大于等于
     * 默认字段为 startTime
     * @return {@link String }
     * @author HeathCHEN
     * 2024/02/26
     */
    String notBetweenStartVal() default MyBatisPlusUtilConst.START_TIME;


    /**
     * 用于between的字段名 不小于等于
     * 默认字段为 endTime
     * @return {@link String }
     * @author HeathCHEN
     * 2024/02/26
     */
    String notBetweenEndVal() default MyBatisPlusUtilConst.END_TIME;


    /**
     * 是否数据库字段参与查询或排序
     * @return boolean
     * @author HeathCHEN
     * 2024/02/26
     */
    boolean exist() default true;


    /**
     * 表连接类型 默认左连接
     * @return {@link JoinType }
     * @author HeathCHEN
     * 2024/02/26
     */
    JoinType joinType() default JoinType.LEFT_JOIN;


    /**
     * 表连接实体类
     * @return {@link Class }
     * @author HeathCHEN
     * 2024/02/26
     */
    Class<?> joinEntityClass();


    /**
     * 自动义SQL
     * @return {@link String }
     * @author HeathCHEN
     * 2024/02/26
     */
    String sql() default "";


}

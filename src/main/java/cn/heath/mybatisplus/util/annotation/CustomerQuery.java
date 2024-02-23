package cn.heath.mybatisplus.util.annotation;


import cn.heath.mybatisplus.util.consts.MyBatisPlusUtilConst;
import cn.heath.mybatisplus.util.enums.JoinType;
import cn.heath.mybatisplus.util.enums.OrderType;
import cn.heath.mybatisplus.util.enums.QueryType;

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
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface CustomerQuery {
    /**
     * 查询类型 默认精确匹配
     * @return
     */
    QueryType value() default QueryType.EQ;

    /**
     *  or 查询, 用于匹配多个字段
     * 对IN\NOT_IN\BETWEEN\NOT_BETWEEN\SQL等QueryType不生效
     * @return
     */
    String[] orColumn() default {};

    /**
     * 数据库中实际的字段名 默认为空
     * 如果该属性非空则默认以该字段查询数据库
     * @return
     */

    String columnName() default "";

    /**
     * 用于放在requestDto类上直接对结果做排序 排序字段
     * 此注解的排序优先级比分页插件高,当此排序生效时会替换分页插件的排序,但分页插件的分页仍会生效
     * @return
     */

    String[] orderColumns() default {};

    /**
     * 用于放在requestDto类上直接对结果做排序 排序顺序
     * @return
     */

    OrderType[] orderTypes() default {};


    /**
     * 是否参与排序 默认不参与
     * @return
     */

    boolean orderColumn() default false;

    /**
     * 排序顺序 默认自然排序
     * @return
     */

    OrderType orderType() default OrderType.ASC;


    /**
     * 排序优先级 默认优先级最高
     * @return
     */

    int orderPriority() default 1;

    /**
     * 用于between的字段名 大于等于
     * 默认字段为 startTime
     * @return
     */

    String betweenStartVal() default MyBatisPlusUtilConst.START_TIME;

    /**
     *  用于between的字段名 小于等于
     *  默认字段为 endTime
     * @return
     */
    String betweenEndVal() default MyBatisPlusUtilConst.END_TIME;


    /**
     * 用于between的字段名 不大于等于
     * 默认字段为 startTime
     * @return
     */

    String notBetweenStartVal() default MyBatisPlusUtilConst.START_TIME;

    /**
     *  用于between的字段名 不小于等于
     *  默认字段为 endTime
     * @return
     */
    String notBetweenEndVal() default MyBatisPlusUtilConst.END_TIME;


    /**
     *  是否数据库字段参与查询或排序
     * @return
     */

    boolean exist() default true;

    /**
     * 表连接类型 默认左连接
     * @return
     */

    JoinType joinType() default JoinType.LEFT_JOIN;

    /**
     * 表连接实体类
     * @return
     */

    Class<?> joinEntityClass() default Object.class;


    /**
     * 自动义SQL
     * @return
     */
    String sql() default "";


}

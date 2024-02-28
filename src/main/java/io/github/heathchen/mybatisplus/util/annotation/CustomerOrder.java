package io.github.heathchen.mybatisplus.util.annotation;


import io.github.heathchen.mybatisplus.util.enums.OrderType;

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
@Target({ElementType.TYPE})
public @interface CustomerOrder {

    /**
     * 用于放在requestDto类上直接对结果做排序 排序字段
     * 此注解的排序优先级比分页插件高,当此排序生效时会替换分页插件的排序,但分页插件的分页仍会生效
     * @return {@link String[] }
     * @author HeathCHEN
     * 2024/02/26
     */
    String[] orderColumnNames() default {};


    /**
     * 用于放在requestDto类上直接对结果做排序 排序顺序
     * @return {@link OrderType[] }
     * @author HeathCHEN
     * 2024/02/26
     */
    OrderType[] orderTypes() default {};


    /**
     * 是否参与排序 默认不参与
     * @return boolean
     * @author HeathCHEN
     * 2024/02/26
     */
    boolean orderColumn() default true;


    /**
     * 排序顺序 默认自然排序
     * @return {@link OrderType }
     * @author HeathCHEN
     * 2024/02/26
     */
    OrderType orderType() default OrderType.ASC;





}

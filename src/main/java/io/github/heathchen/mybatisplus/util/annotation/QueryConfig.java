package io.github.heathchen.mybatisplus.util.annotation;


import io.github.heathchen.mybatisplus.util.enums.MatchMode;
import io.github.heathchen.mybatisplus.util.enums.OrderType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义分页排序注解
 * 此注解的排序优先级比分页插件高,当此排序生效时会替换分页插件的排序,但分页插件的分页仍会生效
 * 放在requestDto类上或者实体类上
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface QueryConfig {

    /**
     * 排序字段
     * @return {@link String[] }
     * @author HeathCHEN
     */
    String[] orderColumnNames() default {};


    /**
     * 排序类型和优先级
     * @return {@link OrderType[] }
     * @author HeathCHEN
     */
    OrderType[] orderTypes() default {};


    /**
     * 是否参与排序 默认不参与
     * @return boolean
     * @author HeathCHEN
     */
    boolean orderColumn() default true;


    /**
     * 匹配模式 默认使用全局设定(全部匹配)
     * @return {@link MatchMode }
     * @author HeathCHEN
     */
    MatchMode matchMode() default MatchMode.USING_GLOBAL_MODE;









}

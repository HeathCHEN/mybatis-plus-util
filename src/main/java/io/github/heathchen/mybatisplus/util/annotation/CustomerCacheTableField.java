package io.github.heathchen.mybatisplus.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义冗余字段
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface CustomerCacheTableField {
    /**
     * 分组id
     * @return int
     * @author HeathCHEN
     */
    String value() default "default";

}

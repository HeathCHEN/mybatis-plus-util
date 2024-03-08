package io.github.heathchen.mybatisplus.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表字段值唯一注解
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UniqueValue {

    /**
     * 分组id
     * 同一个实体被标记多个@UniqueValue,
     * 分组id相同的@UniqueValue会构筑在同一个QueryWrapper查询是否唯一
     * 如果具有多组@UniqueValue,则会分别构筑多个QueryWrapper去查询是否每组都唯一
     * 当value为空时,默认取字段名当作分组名
     *
     * @return {@link String }
     * @author HeathCHEN
     */
    String value() default "";


}

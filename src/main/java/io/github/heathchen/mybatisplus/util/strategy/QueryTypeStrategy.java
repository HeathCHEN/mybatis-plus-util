package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;

import java.lang.reflect.Field;

/**
 * 查询策略类接口
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public interface QueryTypeStrategy {


    /**
     * 检测分组
     *
     * @param queryField QueryField注解
     * @param groupIds   传入的分组Ids
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    Boolean checkIfNotInGroup(QueryField queryField,
                              String[] groupIds);

    /**
     * 构造查询
     *
     * @param <T>             查询条件适用的实体类型
     * @param queryField      QueryField注解
     * @param value           类
     * @param tableColumnName 字段
     * @param queryWrapper    查询queryWrapper
     * @author HeathCHEN
     */
    <T> void buildQueryWrapper(QueryField queryField,
                               Object value,
                               String tableColumnName,
                               QueryWrapper<T> queryWrapper);

    /**
     * 检查字段值状态
     *
     * @param queryField   QueryField注解
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    <T> void checkConditionType(QueryField queryField,
                                QueryWrapper<T> queryWrapper,
                                String tableColumnName);

    /**
     * 清除查询参数
     *
     * @param queryField QueryField注解
     * @param field      字段
     * @author HeathCHEN
     */
    void removeParam(QueryField queryField, Field field);


    /**
     * 检查排序
     *
     * @param queryField      QueryField注解
     * @param clazz           类
     * @param field           字段
     * @param tableColumnName 表字段名
     * @author HeathCHEN
     */
    void checkOrder(QueryField queryField,
                    Class clazz,
                    Field field,
                    String tableColumnName);

}

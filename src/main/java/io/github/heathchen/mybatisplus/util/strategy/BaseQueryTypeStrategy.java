package io.github.heathchen.mybatisplus.util.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

import java.lang.reflect.Field;

/**
 * 基础查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/02
 */
public abstract class BaseQueryTypeStrategy implements QueryTypeStrategy {


    /**
     * 校验参数配置和构建查询
     *
     * @param queryField   QueryField注解
     * @param clazz        类
     * @param field        字段
     * @param queryWrapper 查询queryWrapper
     * @param groupIds     传入组织Id
     * @author HeathCHEN
     */
    <T> void constructQueryWrapper(QueryField queryField,
                                   Class clazz,
                                   Field field,
                                   QueryWrapper<T> queryWrapper,
                                   String[] groupIds) {

        if (checkIfNotInGroup(queryField, groupIds)) {
            removeParam(queryField, field);
            return;
        }
        //查询属性名对应字段名
        Object value = QueryParamThreadLocal.getValueFromQueryParamMap(field.getName());
        String tableColumnName = TableUtil.getTableColumnName(clazz, field);

        if (queryField.value().equals(QueryType.BETWEEN) || queryField.value().equals(QueryType.NOT_BETWEEN)) {
            buildQueryWrapper(queryField, value, tableColumnName, queryWrapper);
        } else {
            if (QueryUtil.checkValue(value)) {
                buildQueryWrapper(queryField, value, tableColumnName, queryWrapper);
            } else {
                checkConditionType(queryField, queryWrapper, tableColumnName);
            }
        }
        removeParam(queryField, field);
        checkOrder(queryField, clazz, field, tableColumnName);
    }

    /**
     * 检测分组
     *
     * @param queryField QueryField注解
     * @param groupIds   传入的分组Ids
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public Boolean checkIfNotInGroup(QueryField queryField, String[] groupIds) {
        return !QueryUtil.checkIfInGroup(queryField, groupIds);
    }

    /**
     * 检查排序
     *
     * @param queryField      QueryField注解
     * @param clazz           类
     * @param field           字段
     * @param tableColumnName 表字段名
     * @author HeathCHEN
     */
    public void checkOrder(QueryField queryField, Class clazz, Field field, String tableColumnName) {
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);
    }


    /**
     * 清除查询参数
     *
     * @param queryField QueryField注解
     * @param field      字段
     */
    @Override
    public void removeParam(QueryField queryField, Field field) {
        QueryParamThreadLocal.removeParamFromQueryParamMap(field.getName());
    }

    /**
     * 检查字段值状态
     *
     * @param queryField   QueryField注解
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */

    public <T> void checkConditionType(QueryField queryField, QueryWrapper<T> queryWrapper, String tableColumnName) {
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NULL)) {
            queryWrapper.isNull(tableColumnName);
        }
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NOT_NULL)) {
            queryWrapper.isNotNull(tableColumnName);
        }
    }

}

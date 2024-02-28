package io.github.heathchen.mybatisplus.util.utils;

import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 表工具类
 * @author HeathCHEN
 * @version 1.0
 * 2024/02/26
 */
public class TableUtil {


    /**
     * 获取实体属性对应表字段名
     * @param clazz 类
     * @param field 字段
     * @return {@link String } 表字段名
     * @author HeathCHEN
     * 2024/02/26
     */
    public static String getTableColumnName(Class<?> clazz, Field field) {

        String columnName = "";
        if (field.isAnnotationPresent(CustomerQuery.class)) {
            CustomerQuery customerQuery = field.getAnnotation(CustomerQuery.class);
            columnName = customerQuery.columnName();
            if (StrUtil.isNotBlank(columnName)) {
                return columnName;
            }
        }
        if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            columnName = tableField.value();
            if (StrUtil.isNotBlank(columnName)) {
                return columnName;
            }
        }
        try {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
            String key = tableInfo.getCurrentNamespace() + ".BaseResultMap";
            ResultMap resultMap = tableInfo.getConfiguration().getResultMap(key);
            List<ResultMapping> resultMappings = resultMap.getResultMappings();
            if (CollectionUtil.isNotEmpty(resultMappings)) {
                for (ResultMapping resultMapping : resultMappings) {
                    if (resultMapping.getProperty().equals(field.getName())) {
                        columnName = resultMapping.getColumn();
                        if (StrUtil.isNotBlank(columnName)) {
                            return columnName;
                        }
                    }
                }
            }
        }catch (Exception e){}


        columnName = StrUtil.toUnderlineCase(field.getName());

        return columnName;
    }


    public static String checkOrColumnName(String orColumn){
        if (orColumn.contains("_")) {
            return orColumn;
        }else {
            return StrUtil.toUnderlineCase(orColumn);
        }

    }
}

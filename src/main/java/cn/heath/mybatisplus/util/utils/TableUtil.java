package cn.heath.mybatisplus.util.utils;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;

import java.lang.reflect.Field;
import java.util.List;

public class TableUtil {


    /**
     * 获取表字段命
     *
     * @param field
     * @return {@link String}
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
}

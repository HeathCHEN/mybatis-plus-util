package cn.heath.mybatisplus.util.utils;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;

import java.lang.reflect.Field;

public class TableUtil {

    /**
     *  获取表字段命
     *
     * @param field
     * @return {@link String}
     */
    public static String getTableColumnName(Field field) {

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

        columnName = StrUtil.toUnderlineCase(field.getName());


        return columnName;
    }
}

package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 *  查询工具类
 *
 * @author HeathCHEN
 * @since 2024/02/29
 */
public class QueryUtil {


    /**
     * 校验参数是否有意义
     *
     * @param t 查询参数
     * @return {@link Boolean } 有意义返回true,否则false
     * @author HeathCHEN
     */
    public static <T> Boolean checkValue(T t) {

        if (ObjectUtil.isNull(t)) {
            return false;
        }
        //数字\日期\布尔
        if (t instanceof Number || t instanceof Boolean || t instanceof Date) {
            return true;
        }
        //文字
        if (t instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) t;
            if (StrUtil.isNotBlank(charSequence)) {
                return true;
            } else {
                return false;
            }


        }
        //Map
        if (t instanceof Map) {
            Map map = (Map) t;
            if (map.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }


        //数组
        if (t.getClass().isArray()) {
            Object[] objectArray = (Object[]) t;
            if (ArrayUtil.isNotEmpty(objectArray)) {
                return true;
            } else {
                return false;
            }
        }
        //集合
        if (t instanceof Collection) {
            Collection collection = (Collection) t;
            if (CollectionUtil.isNotEmpty(collection)) {
                return true;
            } else {
                return false;
            }
        }


        return true;
    }
}

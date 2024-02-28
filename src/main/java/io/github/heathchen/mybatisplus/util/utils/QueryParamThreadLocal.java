package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.util.ArrayUtil;

import java.util.Map;

/**
 * 存放查询参数线程工具类
 * @author HeathCHEN
 * @version 1.0
 * 2024/02/26
 */
public class QueryParamThreadLocal {

	private QueryParamThreadLocal(){
    }

	private static final ThreadLocal<Map<String,Object>> LOCAL = new ThreadLocal<>();

    /**
     * 设置查询参数到线程中
     * @param data 查询参数的map
     * @author HeathCHEN
     * @since 2024/02/23
     */
    public static void setObjectMap(Map<String,Object> data){
        LOCAL.set(data);
    }


    /**
     * 获取全部查询参数
     * @return {@link Map }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Map<String,Object> getObjectMap(){
        return LOCAL.get();
    }


    /**
     * 获取查询参数
     * @param key 查询参数的属性名
     * @return {@link Object }
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static Object getValueFromObjectMap(String key){
        return LOCAL.get().get(key);
    }


    /**
     * 移除查询参数
     * @param keys 查询参数的属性名
     * @author HeathCHEN
     * @since 2024/02/26
     */
    public static void removeParamFromObjectMap(String... keys){
        if (ArrayUtil.isNotEmpty(keys)) {
            Map<String, Object> objectMap = getObjectMap();
            for (String key : keys) {
                objectMap.remove(key);
            }
        }
    }
}

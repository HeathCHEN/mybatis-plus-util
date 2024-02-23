package cn.heath.mybatisplus.util.utils;

import cn.hutool.core.util.ArrayUtil;

import java.util.Map;

public class ParamThreadLocal {
	//私有构造函数
	private ParamThreadLocal(){
    }

	private static final ThreadLocal<Map<String,Object>> LOCAL = new ThreadLocal<>();

    /**
     * 设置查询参数到线程中
     * @param data
     * @author HeathCHEN
     * 2024/02/23
     */
    public static void setObjectMap(Map<String,Object> data){
        LOCAL.set(data);
    }

    /**
     * 获取全部查询参数
     * @return {@link Map }<{@link String },{@link Object }>
     * @author HeathCHEN
     * 2024/02/23
     */
    public static Map<String,Object> getObjectMap(){
        return LOCAL.get();
    }

    /**
     * 通过key获取查询参数
     * @param key
     * @return {@link Object }
     * @author HeathCHEN
     * 2024/02/23
     */
    public static Object getValueFromObjectMap(String key){
        return LOCAL.get().get(key);
    }


    /**
     * 剔除查询参数
     * @param keys
     * @author HeathCHEN
     * 2024/02/23
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

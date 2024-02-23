package cn.heath.mybatisplus.util.utils;

import cn.hutool.core.util.ArrayUtil;

import java.util.Map;

public class ParamThreadLocal {
	//私有构造函数
	private ParamThreadLocal(){
    }

	private static final ThreadLocal<Map<String,Object>> LOCAL = new ThreadLocal<>();
	
	public static void setObjectMap(Map<String,Object> data){
        LOCAL.set(data);
    }
	public static Map<String,Object> getObjectMap(){
        return LOCAL.get();
    }

    public static Object getValueFromObjectMap(String key){
        return LOCAL.get().get(key);
    }




    public static void removeParamFromObjectMap(String... keys){
        if (ArrayUtil.isNotEmpty(keys)) {
            Map<String, Object> objectMap = getObjectMap();
            for (String key : keys) {
                objectMap.remove(key);
            }
        }
    }
}

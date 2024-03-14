package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Servlet工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/14
 */
public class ServletUtils {

    /**
     * 获取String参数
     *
     * @param name 参数名
     * @return {@link String }
     * @author HeathCHEN
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }


    /**
     * 获得所有请求参数
     *
     * @param request 请求对象
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象
     * @return Map
     */
    public static Map<String, String> getParamMap(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {

            params.put(entry.getKey(), StrUtil.join(",", entry.getValue()));
        }
        return params;
    }


    /**
     * 获取request
     *
     * @return {@link HttpServletRequest }
     * @author HeathCHEN
     */
    public static HttpServletRequest getRequest() {
        try {
            return getRequestAttributes().getRequest();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取response
     *
     * @return {@link HttpServletResponse }
     * @author HeathCHEN
     */
    public static HttpServletResponse getResponse() {
        try {
            return getRequestAttributes().getResponse();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取session
     *
     * @return {@link HttpSession }
     * @author HeathCHEN
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取RequestAttributes
     *
     * @return {@link ServletRequestAttributes }
     * @author HeathCHEN
     */
    public static ServletRequestAttributes getRequestAttributes() {
        try {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            return (ServletRequestAttributes) attributes;
        } catch (Exception e) {
            return null;
        }
    }

}

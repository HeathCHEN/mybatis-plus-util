package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;


/**
 * 上下文工具类
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class ApplicationContextUtil {

    private static final Log log = GlobalLogFactory.get().getLog(ApplicationContextUtil.class);

    /**
     * 获取实体类对应的MapperBean
     * @param clazz 类
     * @return {@link BaseMapper } 实体类对应BaseMapper的Bean
     * @author HeathCHEN
     */
    public static  BaseMapper getMapperBean(Class<?> clazz) {

        while (!clazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                break;
            } else {
                clazz = superclass;
            }
        }

        BaseMapper baseMapper = null;
        baseMapper = getMapperBeanByMyBatisCache(clazz);
        if (ObjectUtil.isNotNull(baseMapper)) {
            return baseMapper;
        }
        baseMapper = getMapperBeanByName(clazz);
        if (ObjectUtil.isNotNull(baseMapper)) {
            return baseMapper;
        }

        if (ObjectUtil.isNull(baseMapper)) {
            log.error("找不到执行SQL的baseMapper,查询参数对象需要继承实体类或用@TableName(value=\"表名\")");
            throw new RuntimeException("找不到执行SQL的baseMapper");
        }

        return baseMapper;
    }

    /**
     * 通过MyBatis解析获取Mapper
     *
     * @param <T> 实体类的实体类型
     * @param clazz 类
     * @return {@link BaseMapper } 实体类对应BaseMapper的Bean
     * @author HeathCHEN
     */
    public static <T> BaseMapper<T> getMapperBeanByMyBatisCache(Class<T> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        //通过MyBatis解析实体获取的命名空间
        String currentNamespace = tableInfo.getCurrentNamespace();
        Class<?> mapperClass = null;
        try {
            //再用命名空间获取Mapper类
            mapperClass = Class.forName(currentNamespace);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //通过类型在容器获取Bean
        return (BaseMapper<T>) ApplicationContextProvider.getBean(mapperClass);
    }

    /**
     * 通过名字拼接获取Mapper
     *
     * @param <T> 实体类的实体类型
     * @param clazz 实体类
     * @return {@link BaseMapper } 实体类对应BaseMapper的Bean
     * @author HeathCHEN
     */
    public static <T> BaseMapper<T> getMapperBeanByName(Class<?> clazz) {
        String serviceName = StrUtil.lowerFirst(clazz.getSimpleName()) + "Mapper";
        return ApplicationContextProvider.getBean(serviceName, BaseMapper.class);
    }


}

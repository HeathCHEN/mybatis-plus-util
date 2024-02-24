package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationContextUtil {


    /**
     * 获取实体类对应的MapperBean
     * @param clazz
     * @return {@link BaseMapper }<{@link T }>
     * @author HeathCHEN
     * 2024/02/23
     */
    public static <T> BaseMapper<T> getMapperBean(Class<?> clazz) {

        while (!clazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                break;
            } else {
                clazz = superclass;
            }
        }

        BaseMapper<T> baseMapper = null;
        baseMapper = getMapperBeanByMyBatisCache(clazz);
        if (ObjectUtil.isNotNull(baseMapper)) {
            return baseMapper;
        }
        baseMapper = getMapperBeanByName(clazz);
        if (ObjectUtil.isNotNull(baseMapper)) {
            return baseMapper;
        }
        return baseMapper;
    }

    /**
     * 通过MyBatis解析获取Mapper
     * @param clazz
     * @return {@link BaseMapper }<{@link T }>
     * @author HeathCHEN
     * 2024/02/23
     */
    public static <T> BaseMapper<T> getMapperBeanByMyBatisCache(Class<?> clazz) {
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
     * @param clazz
     * @return {@link BaseMapper }<{@link T }>
     * @author HeathCHEN
     * 2024/02/23
     */
    public static <T> BaseMapper<T> getMapperBeanByName(Class<?> clazz) {
        String serviceName = StrUtil.lowerFirst(clazz.getSimpleName()) + "Mapper";
        return ApplicationContextProvider.getBean(serviceName, BaseMapper.class);
    }


}

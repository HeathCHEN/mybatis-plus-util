package cn.heath.mybatisplus.util.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationContextUtil  {



    public static IService getIServiceBean(Class<?> clazz){
        String serviceName = StrUtil.lowerFirst(clazz.getSimpleName()) + "ServiceImpl";
        IService service = ApplicationContextProvider.getBean(serviceName, IService.class);
        return service;
    }


}

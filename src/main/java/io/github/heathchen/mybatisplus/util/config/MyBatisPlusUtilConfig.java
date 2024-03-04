package io.github.heathchen.mybatisplus.util.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;
import io.github.heathchen.mybatisplus.util.strategy.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * MyBatisPlusUtil配置类
 *
 * @author Administrator
 * @since 2024/02/22
 */
public class MyBatisPlusUtilConfig {

    @Value("${myBatisPlusUtil.globalMatchMode:allMatchMode}")
    public String globalMatchMode;


    public MyBatisPlusUtilConfig() {
        super();
        //初始化策略类
        initStrategy();
        //设置全局的日志工厂
        if (ObjectUtil.isNull(GlobalLogFactory.get())) {
            GlobalLogFactory.set(Slf4jLogFactory.class);
        }

    }

    public MyBatisPlusUtilConfig(String globalMatchMode) {
        this();
        this.globalMatchMode = globalMatchMode;
    }

    /**
     * 初始化策略类
     *
     * @author HeathCHEN
     */
    public void initStrategy() {
        new BetweenQueryTypeStrategy();
        new EqQueryTypeStrategy();
        new GreaterEqualQueryTypeStrategy();
        new GreaterThanQueryTypeStrategy();
        new InQueryTypeStrategy();
        new LessEqualQueryTypeStrategy();
        new LessThanQueryTypeStrategy();
        new LikeLeftQueryTypeStrategy();
        new LikeRightQueryTypeStrategy();
        new LikeQueryTypeStrategy();
        new NotBetweenQueryTypeStrategy();
        new NotEqQueryTypeStrategy();
        new NotInQueryTypeStrategy();
        new NotLikeQueryTypeStrategy();
        new SqlQueryTypeStrategy();
    }

    public String getGlobalMatchMode() {
        return globalMatchMode;
    }

    public void setGlobalMatchMode(String globalMatchMode) {
        this.globalMatchMode = globalMatchMode;
    }
}

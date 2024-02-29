package io.github.heathchen.mybatisplus.util.config;

import io.github.heathchen.mybatisplus.util.strategy.*;
import org.springframework.beans.factory.annotation.Value;

/**
 *  MyBatisPlusUtil配置类
 *
 * @author Administrator
 * @since 2024/02/22
 */
public class MyBatisPlusUtilConfig {

    @Value("${myBatisPlusUtil.globalMatchMode:allMatchMode}")
    public String globalMatchMode;


    public MyBatisPlusUtilConfig() {
        initStrategy();
    }

    /**
     * 初始化策略类
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

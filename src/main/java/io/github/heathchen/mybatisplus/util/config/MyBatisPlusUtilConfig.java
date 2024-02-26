package io.github.heathchen.mybatisplus.util.config;

import io.github.heathchen.mybatisplus.util.strategy.*;

/**
 *  MyBatisPlusUtil配置类
 *
 * @author Administrator
 * @since 2024/02/22
 */
public class MyBatisPlusUtilConfig {


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
}

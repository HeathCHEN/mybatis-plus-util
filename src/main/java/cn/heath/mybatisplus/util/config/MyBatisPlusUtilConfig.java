package cn.heath.mybatisplus.util.config;

import cn.heath.mybatisplus.util.strategy.*;

/**
 *  MyBatisPlusUtil配置类
 *
 * @author Administrator
 * @date 2024/02/22
 */
public class MyBatisPlusUtilConfig {


    public MyBatisPlusUtilConfig() {
        initStrategy();
    }

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

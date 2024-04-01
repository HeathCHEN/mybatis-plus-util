package io.github.heathchen.mybatisplus.util.factory;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityDefinition;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.strategy.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultQueryWrapperFactory implements QueryWrapperFactory {

    public static final ConcurrentHashMap<String, EntityDefinition> definition_map = new ConcurrentHashMap<>();


    public static final Map<String, QueryTypeStrategy> QUERY_TYPE_STRATEGY_MAP = new HashMap<>();


    public DefaultQueryWrapperFactory() {
        initStrategy();
    }

    @Override
    public QueryWrapper<?> getQueryWrapper() {
        return null;
    }

    /**
     * 设置策略
     *
     * @param queryType         查询类型名称
     * @param queryTypeStrategy 查询策略类
     * @author HeathCHEN
     */
    public static void putQueryTypeStrategyToManager(String queryType, QueryTypeStrategy queryTypeStrategy) {
        QUERY_TYPE_STRATEGY_MAP.putIfAbsent(queryType, queryTypeStrategy);
    }


    /**
     * 获取策略
     *
     * @param queryType 查询类型名称
     * @return {@link QueryTypeStrategy } 查询策略类
     * @author HeathCHEN
     */
    public static QueryTypeStrategy getQueryTypeStrategyToManager(String queryType) {
        return QUERY_TYPE_STRATEGY_MAP.get(queryType);
    }


    /**
     * 获取对应策略类并执行
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    public static <T, E> void invokeQueryStrategy(EntityGenericDefinition<T, E> entityGenericDefinition) {
        BaseQueryTypeStrategy queryTypeStrategy = (BaseQueryTypeStrategy) getQueryTypeStrategyToManager(entityGenericDefinition.getQueryType().getCompareType());
        if (ObjectUtil.isNotNull(queryTypeStrategy)) {
            queryTypeStrategy.constructQueryWrapper(entityGenericDefinition);
        }
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

}

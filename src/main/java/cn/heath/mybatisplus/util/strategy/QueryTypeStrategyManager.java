package cn.heath.mybatisplus.util.strategy;

import java.util.HashMap;
import java.util.Map;

public class QueryTypeStrategyManager {

    public static final Map<String, QueryTypeStrategy> QUERY_TYPE_STRATEGY_MAP = new HashMap<>();

    public static void putQueryTypeStrategyToManager(String queryType, QueryTypeStrategy queryTypeStrategy) {
        QUERY_TYPE_STRATEGY_MAP.put(queryType, queryTypeStrategy);
    }

    public static QueryTypeStrategy getQueryTypeStrategyToManager(String queryType) {
        return QUERY_TYPE_STRATEGY_MAP.get(queryType);
    }


}

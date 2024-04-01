package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.enums.QueryType;

/**
 * 自定义SQL查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class SqlQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.SQL;

    public SqlQueryTypeStrategy() {
        super(QUERY_TYPE);
    }

    /**
     * 构造查询
     *
     * @param entityGenericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGenericDefinition<T, E> entityGenericDefinition) {
        QueryWrapper<T> queryWrapper = entityGenericDefinition.getQueryWrapper();
        Object queryParam = entityGenericDefinition.getQueryParam();
        if (StrUtil.isNotBlank(entityGenericDefinition.getSql())) {
            queryWrapper.apply(entityGenericDefinition.getSql(), queryParam);
        }
    }
}

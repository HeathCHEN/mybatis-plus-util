package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.domain.QueryContext;
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
     * @param queryContext 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(QueryContext<T, E> queryContext) {
        QueryWrapper<T> queryWrapper = queryContext.getQueryWrapper();
        QueryField queryField = queryContext.getQueryField();
        Object queryParam = queryContext.getQueryParam();
        if (StrUtil.isNotBlank(queryField.sql())) {
            queryWrapper.apply(queryField.sql(), queryParam);
        }
    }
}

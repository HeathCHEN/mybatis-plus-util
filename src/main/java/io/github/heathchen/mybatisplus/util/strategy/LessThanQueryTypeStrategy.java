package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.domain.QueryContext;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

/**
 * 小于查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class LessThanQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.LESS_THAN;

    public LessThanQueryTypeStrategy() {
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
        String tableColumnName = queryContext.getTableColumnName();
        QueryField queryField = queryContext.getQueryField();
        Object queryParam = queryContext.getQueryParam();

        String[] orColumns = queryField.orColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.lt(tableColumnName, queryParam);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.lt(TableUtil.checkOrColumnName(orColumn), queryParam);
                        }
                    }
            );
        } else {
            queryWrapper.lt(tableColumnName, queryParam);
        }
    }

}

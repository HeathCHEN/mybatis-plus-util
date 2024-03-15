package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.domain.QueryContext;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

/**
 * LIKE '%值' 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class LikeLeftQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.LIKE_LEFT;

    public LikeLeftQueryTypeStrategy() {
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

        QueryField queryField = queryContext.getQueryField();
        QueryWrapper<T> queryWrapper = queryContext.getQueryWrapper();
        String tableColumnName = queryContext.getTableColumnName();
        Object queryParam = queryContext.getQueryParam();

        if (checkWithoutLike(queryContext)) {
            return;
        }

        String[] orColumns = queryField.orColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.likeLeft(tableColumnName, queryParam);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.likeLeft(TableUtil.checkOrColumnName(orColumn), queryParam);
                        }
                    }
            );
        } else {
            queryWrapper.likeLeft(tableColumnName, queryParam);
        }
    }


}

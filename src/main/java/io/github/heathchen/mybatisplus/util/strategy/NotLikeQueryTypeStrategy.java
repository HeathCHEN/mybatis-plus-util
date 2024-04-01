package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

/**
 * NOT LIKE '%值%' 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class NotLikeQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.NOT_LIKE;

    public NotLikeQueryTypeStrategy() {
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
        String tableColumnName = entityGenericDefinition.getTableColumnName();
        Object queryParam = entityGenericDefinition.getQueryParam();

        if (checkWithoutLike(entityGenericDefinition)) {
            return;
        }

        String[] orColumns = entityGenericDefinition.getOrColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.notLike(tableColumnName, queryParam);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.notLike(TableUtil.checkOrColumnName(orColumn), queryParam);
                        }
                    }
            );
        } else {
            queryWrapper.notLike(tableColumnName, queryParam);
        }
    }
}

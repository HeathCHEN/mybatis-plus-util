package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGenericDefinition;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

/**
 * LIKE '值%' 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class LikeRightQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.LIKE_RIGHT;

    public LikeRightQueryTypeStrategy() {
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
                        tQueryWrapper.likeRight(tableColumnName, queryParam);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.likeRight(TableUtil.checkOrColumnName(orColumn), queryParam);
                        }
                    }
            );
        } else {
            queryWrapper.likeRight(tableColumnName, queryParam);
        }
    }
}

package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.definiton.EntityGernericDefinition;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

/**
 * 小于等于查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class LessEqualQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.LESS_EQUAL;


    public LessEqualQueryTypeStrategy() {
        super(QUERY_TYPE);
    }

    /**
     * 构造查询
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    @Override
    public <T, E> void buildQueryWrapper(EntityGernericDefinition<T, E> entityGernericDefinition) {
        QueryWrapper<T> queryWrapper = entityGernericDefinition.getQueryWrapper();
        String tableColumnName = entityGernericDefinition.getTableColumnName();
        Object queryParam = entityGernericDefinition.getQueryParam();

        String[] orColumns = entityGernericDefinition.getOrColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.le(tableColumnName, queryParam);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.le(TableUtil.checkOrColumnName(orColumn), queryParam);
                        }

                    }
            );
        } else {
            queryWrapper.le(tableColumnName, queryParam);
        }
    }
}

package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
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
     * @param queryField      QueryField注解
     * @param value           类
     * @param tableColumnName 字段
     * @param queryWrapper    查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQueryWrapper(QueryField queryField, Object value, String tableColumnName, QueryWrapper<T> queryWrapper) {

        if (checkWithoutLike(queryField, value, tableColumnName, queryWrapper)) {
            return;
        }

        String[] orColumns = queryField.orColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.notLike(tableColumnName, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.notLike(TableUtil.checkOrColumnName(orColumn), value);
                        }
                    }
            );
        } else {
            queryWrapper.notLike(tableColumnName, value);
        }
    }
}

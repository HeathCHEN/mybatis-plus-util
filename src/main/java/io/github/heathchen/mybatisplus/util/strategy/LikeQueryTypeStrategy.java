package io.github.heathchen.mybatisplus.util.strategy;

import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryParamThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * LIKE '%值%' 查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class LikeQueryTypeStrategy extends BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final QueryType QUERY_TYPE = QueryType.LIKE;

    public LikeQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 构造查询
     *
     * @param queryField QueryField注解
     * @param value         类
     * @param tableColumnName         字段
     * @param queryWrapper  查询queryWrapper
     * @author HeathCHEN
     */
    @Override
    public <T> void buildQueryWrapper(QueryField queryField, Object value, String tableColumnName, QueryWrapper<T> queryWrapper) {
        String[] orColumns = queryField.orColumns();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.like(tableColumnName, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.like(TableUtil.checkOrColumnName(orColumn), value);
                        }
                    }
            );
        } else {
            queryWrapper.like(tableColumnName, value);
        }
    }
}

package cn.heath.mybatisplus.util.strategy;

import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.enums.QueryType;
import cn.heath.mybatisplus.util.utils.ParamThreadLocal;
import cn.heath.mybatisplus.util.utils.TableUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

public class LikeLeftQueryTypeStrategy implements QueryTypeStrategy {
    private static final QueryType QUERY_TYPE = QueryType.LIKE_LEFT;

    public LikeLeftQueryTypeStrategy() {
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);

    }

    /**
     * 构造查询
     * @param queryWrapper
     * @author HeathCHEN
     * 2024/02/23
     */
    @Override
    public <T> void buildQuery(CustomerQuery customerQuery, Class clazz, Field field, QueryWrapper<T> queryWrapper) {
        Object value = ParamThreadLocal.getValueFromObjectMap(field.getName());
        if (ObjectUtil.isNull(value)) {
            return;
        }

        //将属性转为下划线格式
        String underlineCase = TableUtil.getTableColumnName(clazz,field);

        String[] orColumns = customerQuery.orColumn();
        if (ArrayUtil.isNotEmpty(orColumns)) {
            String tempUnderlineCase = underlineCase;
            queryWrapper.and(tQueryWrapper -> {
                        tQueryWrapper.likeLeft(tempUnderlineCase, value);
                        for (String orColumn : orColumns) {
                            tQueryWrapper.or();
                            tQueryWrapper.likeLeft(orColumn, value);
                        }
                    }
            );
        } else {
            if (ObjectUtil.isNotNull(value)) {
                queryWrapper.likeLeft(underlineCase, value);
            }
        }
        ParamThreadLocal.removeParamFromObjectMap(field.getName());
    }
}

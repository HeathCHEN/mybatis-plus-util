package io.github.heathchen.mybatisplus.util.strategy;

import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.enums.ConditionType;
import io.github.heathchen.mybatisplus.util.enums.QueryType;
import io.github.heathchen.mybatisplus.util.utils.PageHelperUtil;
import io.github.heathchen.mybatisplus.util.utils.QueryContextThreadLocal;
import io.github.heathchen.mybatisplus.util.utils.QueryUtil;
import io.github.heathchen.mybatisplus.util.utils.TableUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

/**
 * 基础查询策略类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/02
 */
public abstract class BaseQueryTypeStrategy implements QueryTypeStrategy {

    private static final Log log = GlobalLogFactory.get().getLog(BaseQueryTypeStrategy.class);

    public BaseQueryTypeStrategy() {
        super();
    }

    public BaseQueryTypeStrategy(QueryType QUERY_TYPE) {
        this();
        QueryTypeStrategyManager.putQueryTypeStrategyToManager(QUERY_TYPE.getCompareType(), this);
    }

    /**
     * 校验参数配置和构建查询
     *
     * @param queryField   QueryField注解
     * @param clazz        类
     * @param field        字段
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    <T> void constructQueryWrapper(QueryField queryField,
                                   Class clazz,
                                   Field field,
                                   QueryWrapper<T> queryWrapper) {

        Object value = null;
        String[] groupIds = QueryContextThreadLocal.getGroupIds();
        try {
            //检测是否分组
            if (checkIfNotInGroup(queryField, groupIds)) {
                return;
            }
            //获取查询值
            value = QueryContextThreadLocal.getValueFromQueryParamMap(field.getName());
            //获取真实表字段名
            String tableColumnName = TableUtil.getTableColumnName(clazz, field);
            //BETWEEN和NOT_BETWEEN的查询不校验查询参数
            if (queryField.value().equals(QueryType.BETWEEN) || queryField.value().equals(QueryType.NOT_BETWEEN)) {
                buildQueryWrapper(queryField, value, tableColumnName, queryWrapper);
            } else {
                //校验参数是否为空
                if (QueryUtil.checkValue(value)) {
                    //构建QueryWrapper
                    buildQueryWrapper(queryField, value, tableColumnName, queryWrapper);
                } else {
                    //检查值空时是否查询
                    checkConditionType(queryField, queryWrapper, tableColumnName);
                }
            }
            //检查是否排序
            checkOrder(queryField, clazz, field, tableColumnName);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            log.error("构造查询异常,类名:{},字段名:{},值:{},异常原因:{}", clazz.getName(), field.getName(), value, sw.toString());
        } finally {
            //清除已匹配的查询参数
            removeParam(queryField, field);
        }
    }

    /**
     * 检测分组
     *
     * @param queryField QueryField注解
     * @param groupIds   传入的分组Ids
     * @return {@link Boolean }
     * @author HeathCHEN
     */
    public Boolean checkIfNotInGroup(QueryField queryField, String[] groupIds) {
        return !QueryUtil.checkIfInGroup(queryField, groupIds);
    }

    /**
     * 检查排序
     *
     * @param queryField      QueryField注解
     * @param clazz           类
     * @param field           字段
     * @param tableColumnName 表字段名
     * @author HeathCHEN
     */
    public void checkOrder(QueryField queryField, Class clazz, Field field, String tableColumnName) {
        //检查是否使用排序
        PageHelperUtil.checkColumnOrderOnField(queryField, clazz, field, tableColumnName);
    }


    /**
     * 清除查询参数
     *
     * @param queryField QueryField注解
     * @param field      字段
     */
    @Override
    public void removeParam(QueryField queryField, Field field) {
        //从线程中移除参数
        QueryContextThreadLocal.removeParamFromQueryParamMap(field.getName());
    }

    /**
     * 检查查询值状态
     *
     * @param queryField   QueryField注解
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */

    public <T> void checkConditionType(QueryField queryField, QueryWrapper<T> queryWrapper, String tableColumnName) {
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NULL)) {
            queryWrapper.isNull(tableColumnName);
        }
        if (queryField.conditionType().equals(ConditionType.TABLE_COLUMN_IS_NOT_NULL)) {
            queryWrapper.isNotNull(tableColumnName);
        }
    }


    public <T> Boolean checkWithoutLike(QueryField queryField, Object value, String tableColumnName, QueryWrapper<T> queryWrapper) {
        Boolean withoutLike = QueryContextThreadLocal.getWithoutLike();

        if (withoutLike) {
            QueryTypeStrategy EqQueryTypeStrategy = QueryTypeStrategyManager.getQueryTypeStrategyToManager(QueryType.EQ.getCompareType());
            EqQueryTypeStrategy.buildQueryWrapper(queryField,value,tableColumnName,queryWrapper);

        }

        return withoutLike;
    }

}

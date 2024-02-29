package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.annotation.QueryConfig;
import io.github.heathchen.mybatisplus.util.annotation.QueryField;
import io.github.heathchen.mybatisplus.util.consts.PageAndOrderConst;
import io.github.heathchen.mybatisplus.util.domain.QueryConfigDto;
import io.github.heathchen.mybatisplus.util.enums.OrderType;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 分页插件工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class PageHelperUtil {


    public static void getPageParamFromQueryParam() {
        OrderAndPageParamThreadLocal.setStartPage((Boolean) QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.START_PAGE));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.IS_ASC, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.IS_ASC));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.PAGE_SIZE, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_SIZE));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.PAGE_NUM, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_NUM));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.ORDER_BY_COLUMN, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.ORDER_BY_COLUMN));
        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.REASONABLE, QueryParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.REASONABLE));
        QueryParamThreadLocal.removeParamFromObjectMap(PageAndOrderConst.START_PAGE,
                PageAndOrderConst.IS_ASC,
                PageAndOrderConst.PAGE_SIZE,
                PageAndOrderConst.PAGE_NUM,
                PageAndOrderConst.ORDER_BY_COLUMN,
                PageAndOrderConst.REASONABLE);
    }


    /**
     * 检查是否排序
     *
     * @param clazz 类
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnClass(Class<?> clazz) {
        //清除分页插件的排序参数 使用该注解分页
        Boolean startPage = OrderAndPageParamThreadLocal.getStartPage();
        PageHelper.clearPage();
        if (startPage) {
            com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
            if (ObjectUtil.isNotNull(localPage)) {
                PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());
            } else {
                Integer pageSize = (Integer) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_SIZE);
                Integer pageNum = (Integer) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.PAGE_NUM);
                if (ObjectUtil.isNotNull(pageSize) && ObjectUtil.isNotNull(pageNum)) {
                    PageHelper.startPage(pageNum, pageSize);
                }
            }
        }
        if (!clazz.isAnnotationPresent(QueryConfig.class)) {
            return;
        }
        QueryConfig QueryConfig = clazz.getDeclaredAnnotation(QueryConfig.class);
        String[] columns = QueryConfig.orderColumnNames();
        OrderType[] orderTypes = QueryConfig.orderTypes();
        boolean orderColumn = QueryConfig.orderColumn();

        OrderAndPageParamThreadLocal.setValueToObjectMap(PageAndOrderConst.ORDER_COLUMN, orderColumn);

        if (ArrayUtil.isNotEmpty(columns) && orderColumn) {
            for (int i = 0; i < columns.length; i++) {
                QueryConfigDto QueryConfigDto = new QueryConfigDto();
                if (StrUtil.isBlank(columns[i])) {
                    continue;
                }
                QueryConfigDto.setTableColumnName(columns[i]);
                if (ObjectUtil.isNull(orderTypes[i])) {
                    continue;
                }
                QueryConfigDto.setOrderType(orderTypes[i]);
                QueryConfigDto.setOrderPriority(i + 1);
                OrderAndPageParamThreadLocal.putQueryConfigDtoIntoOrderList(QueryConfigDto);
            }

        }
    }


    /**
     * 检查是否使用排序
     *
     * @param queryField 注解CustomerQuery
     * @param field         字段
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnField(QueryField queryField, Class<?> clazz, Field field, String tableColumnName) {

        if (queryField.orderType().equals(OrderType.NONE)) {
            return;
        }
        QueryConfigDto QueryConfigDto = new QueryConfigDto();
        QueryConfigDto.setTableColumnName(tableColumnName);
        QueryConfigDto.setOrderPriority(queryField.orderPriority());
        QueryConfigDto.setOrderType(queryField.orderType());
        QueryConfigDto.setField(field);
        QueryConfigDto.setClazz(clazz);
        OrderAndPageParamThreadLocal.putQueryConfigDtoIntoOrderList(QueryConfigDto);


    }

    /**
     * 构筑排序
     *
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static void buildQueryOrder(QueryWrapper<?> queryWrapper) {

        Boolean orderColumn = (Boolean) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.ORDER_COLUMN);

        if (ObjectUtil.isNotNull(orderColumn) && !orderColumn) {
            return;
        }

        String orderByColumn = (String) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.ORDER_BY_COLUMN);
        String isAsc = (String) OrderAndPageParamThreadLocal.getValueFromObjectMap(PageAndOrderConst.IS_ASC);

        if (StrUtil.isNotBlank(orderByColumn) && StrUtil.isNotBlank(isAsc)) {
            QueryConfigDto QueryConfigDto = new QueryConfigDto();
            if (isAsc.equals(Boolean.TRUE.toString())) {
                QueryConfigDto.setOrderType(OrderType.ASC);
            } else {
                QueryConfigDto.setOrderType(OrderType.DESC);
            }
            QueryConfigDto.setTableColumnName(TableUtil.checkOrColumnName(orderByColumn));
            QueryConfigDto.setOrderPriority(-1);
            OrderAndPageParamThreadLocal.putQueryConfigDtoIntoOrderList(QueryConfigDto);
        }

        List<QueryConfigDto> orderList = OrderAndPageParamThreadLocal.getOrderList();

        //对查询进行排序
        if (CollectionUtil.isNotEmpty(orderList)) {
            for (QueryConfigDto QueryConfigDto : orderList) {
                String tableColumnName = QueryConfigDto.getTableColumnName();
                OrderType orderType = QueryConfigDto.getOrderType();
                if (ObjectUtil.isNotNull(orderType) && orderType.equals(OrderType.ASC)) {
                    queryWrapper.orderByAsc(tableColumnName);
                }
                if (ObjectUtil.isNotNull(orderType) && orderType.equals(OrderType.DESC)) {
                    queryWrapper.orderByDesc(tableColumnName);
                }
            }

        }

    }


}

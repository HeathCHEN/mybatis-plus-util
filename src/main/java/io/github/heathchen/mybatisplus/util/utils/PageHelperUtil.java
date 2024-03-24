package io.github.heathchen.mybatisplus.util.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.annotation.QueryConfig;
import io.github.heathchen.mybatisplus.util.config.MyBatisPlusUtilConfig;
import io.github.heathchen.mybatisplus.util.definiton.EntityGernericDefinition;
import io.github.heathchen.mybatisplus.util.domain.OrderDto;
import io.github.heathchen.mybatisplus.util.enums.OrderType;

import javax.servlet.http.HttpServletRequest;
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


    private static final MyBatisPlusUtilConfig myBatisPlusUtilConfig = ApplicationContextProvider.getBean(MyBatisPlusUtilConfig.class);


    public PageHelperUtil() {

    }

    /**
     * 获取分页参数
     *
     * @author HeathCHEN
     */
    public static void getPageParamFromQueryParam() {

        HttpServletRequest request = ServletUtils.getRequest();

        if (request.getMethod().equals(Method.GET.toString())) {
            EntityGernericDefinition.setStartPageIfAbsent(ServletUtils.getParameter(myBatisPlusUtilConfig.getStarPagePropertyName()));
            EntityGernericDefinition.setIsAscIfAbsent(ServletUtils.getParameter(myBatisPlusUtilConfig.getIsAscPropertyName()));
            EntityGernericDefinition.setPageSizeIfAbsent(ServletUtils.getParameter(myBatisPlusUtilConfig.getPageSizePropertyName()));
            EntityGernericDefinition.setPageNumIfAbsent(ServletUtils.getParameter(myBatisPlusUtilConfig.getPageSizePropertyName()));
            EntityGernericDefinition.setOrderByColumnIfAbsent(ServletUtils.getParameter(myBatisPlusUtilConfig.getOrderByColumnPropertyName()));
            EntityGernericDefinition.setReasonableIfAbsent(ServletUtils.getParameter(myBatisPlusUtilConfig.getReasonablePropertyName()));

        } else {
            EntityGernericDefinition.setStartPageIfAbsent(EntityGernericDefinition.getValueFromQueryParamMap(myBatisPlusUtilConfig.getStarPagePropertyName()));
            EntityGernericDefinition.setIsAscIfAbsent(EntityGernericDefinition.getValueFromQueryParamMap(myBatisPlusUtilConfig.getIsAscPropertyName()));
            EntityGernericDefinition.setPageSizeIfAbsent(EntityGernericDefinition.getValueFromQueryParamMap(myBatisPlusUtilConfig.getPageSizePropertyName()));
            EntityGernericDefinition.setPageNumIfAbsent(EntityGernericDefinition.getValueFromQueryParamMap(myBatisPlusUtilConfig.getPageNumPropertyName()));
            EntityGernericDefinition.setOrderByColumnIfAbsent(EntityGernericDefinition.getValueFromQueryParamMap(myBatisPlusUtilConfig.getOrderByColumnPropertyName()));
            EntityGernericDefinition.setReasonableIfAbsent(EntityGernericDefinition.getValueFromQueryParamMap(myBatisPlusUtilConfig.getReasonablePropertyName()));
            EntityGernericDefinition.removeParamFromQueryParamMap(myBatisPlusUtilConfig.getStarPagePropertyName(),
                    myBatisPlusUtilConfig.getIsAscPropertyName(),
                    myBatisPlusUtilConfig.getPageSizePropertyName(),
                    myBatisPlusUtilConfig.getPageNumPropertyName(),
                    myBatisPlusUtilConfig.getOrderByColumnPropertyName(),
                    myBatisPlusUtilConfig.getStarPagePropertyName());
        }


    }


    /**
     * 检查是否排序
     *
     * @param clazz 类
     * @author HeathCHEN
     */
    public static void checkColumnOrderOnClass(Class<?> clazz) {
        //清除分页插件的排序参数 使用该注解分页
        Boolean startPage = EntityGernericDefinition.getStartPage();
        PageHelper.clearPage();
        if (startPage) {
            com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
            if (ObjectUtil.isNotNull(localPage)) {
                PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());
            } else {
                Integer pageSize = EntityGernericDefinition.getPageSize();
                Integer pageNum = EntityGernericDefinition.getPageNum();
                if (ObjectUtil.isNotNull(pageSize) && ObjectUtil.isNotNull(pageNum)) {
                    PageHelper.startPage(pageNum, pageSize);
                }
            }

            while (!clazz.isAnnotationPresent(QueryConfig.class)) {
                Class<?> superclass = clazz.getSuperclass();
                if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                    break;
                } else {
                    clazz = superclass;
                }
            }
            if (!clazz.isAnnotationPresent(QueryConfig.class)) {
                return;
            }
            QueryConfig QueryConfig = clazz.getDeclaredAnnotation(QueryConfig.class);
            String[] columns = QueryConfig.orderColumnNames();
            OrderType[] orderTypes = QueryConfig.orderTypes();
            boolean orderColumn = QueryConfig.orderColumn();

            EntityGernericDefinition.setOrderColumnIfAbsent(orderColumn);

            if (ArrayUtil.isNotEmpty(columns) && orderColumn) {
                for (int i = 0; i < columns.length; i++) {
                    OrderDto OrderDto = new OrderDto();
                    if (StrUtil.isBlank(columns[i])) {
                        continue;
                    }
                    OrderDto.setTableColumnName(columns[i].toUpperCase());
                    if (ObjectUtil.isNull(orderTypes[i])) {
                        continue;
                    }

                    OrderDto.setOrderType(orderTypes[i]);
                    OrderDto.setOrderPriority(i + 1);
                    EntityGernericDefinition.putOrderDtoIntoOrderList(OrderDto);
                }

            }
        }
    }


    /**
     * 检查是否使用排序
     *
     * @param entityGernericDefinition 查询上下文
     * @author HeathCHEN
     */
    public static <T, E> void checkColumnOrderOnField(EntityGernericDefinition<T, E> entityGernericDefinition) {
        String tableColumnName = entityGernericDefinition.getTableColumnName();
        Class<E> clazz = entityGernericDefinition.getParamClass();
        Field field = entityGernericDefinition.getField();

        if (OrderType.NONE.equals(entityGernericDefinition.getOrderType())) {
            return;
        }
        OrderDto OrderDto = new OrderDto();
        OrderDto.setTableColumnName(tableColumnName.toUpperCase());
        OrderDto.setOrderPriority(entityGernericDefinition.getOrderPriority());
        OrderDto.setOrderType(entityGernericDefinition.getOrderType());
        OrderDto.setField(field);
        OrderDto.setClazz(clazz);
        EntityGernericDefinition.putIntoOrderListIfOrderDtoAbsent(OrderDto);


    }

    /**
     * 构筑排序
     *
     * @param queryWrapper 查询queryWrapper
     * @author HeathCHEN
     */
    public static void buildQueryOrder(QueryWrapper<?> queryWrapper) {

        Boolean orderColumn = EntityGernericDefinition.getOrderColumn();
        if (!orderColumn) {
            return;
        }

        String orderByColumn = EntityGernericDefinition.getOrderByColumn();
        Boolean isAsc = EntityGernericDefinition.getIsAsc();

        if (StrUtil.isNotBlank(orderByColumn)) {
            OrderDto OrderDto = new OrderDto();
            if (isAsc) {
                OrderDto.setOrderType(OrderType.ASC);
            } else {
                OrderDto.setOrderType(OrderType.DESC);
            }
            OrderDto.setTableColumnName(TableUtil.checkOrColumnName(orderByColumn).toUpperCase());
            OrderDto.setOrderPriority(-1);
            EntityGernericDefinition.putOrderDtoIntoOrderList(OrderDto);
        }

        List<OrderDto> orderList = EntityGernericDefinition.getOrderList();

        //对查询进行排序
        if (CollectionUtil.isNotEmpty(orderList)) {
            for (OrderDto OrderDto : orderList) {
                String tableColumnName = OrderDto.getTableColumnName();
                OrderType orderType = OrderDto.getOrderType();
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

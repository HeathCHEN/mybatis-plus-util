package io.github.heathchen.mybatisplus.util.utils;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import io.github.heathchen.mybatisplus.util.annotation.CustomerCacheTableField;
import io.github.heathchen.mybatisplus.util.annotation.CustomerCacheTableId;
import io.github.heathchen.mybatisplus.util.annotation.CustomerQuery;
import io.github.heathchen.mybatisplus.util.consts.PageConst;
import io.github.heathchen.mybatisplus.util.domain.CustomerOrder;
import io.github.heathchen.mybatisplus.util.enums.OrderType;
import io.github.heathchen.mybatisplus.util.strategy.AccurateMatchingQueryTypeStrategy;
import io.github.heathchen.mybatisplus.util.strategy.QueryTypeStrategyManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MyBatisPlus工具类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class MyBatisPlusUtil {


    /**
     * 补全时间到当天最后时分秒毫秒
     *
     * @param date 时间
     * @return {@link Date } 时间
     * @author HeathCHEN
     */
    public static Date getNewDateTo235959999FromDate(Date date) {
        DateTime dateTime = DateUtil.dateNew(date);
        dateTime.setField(DateField.HOUR_OF_DAY, 23);
        dateTime.setField(DateField.SECOND, 59);
        dateTime.setField(DateField.MINUTE, 59);
        dateTime.setField(DateField.MILLISECOND, 999);
        return dateTime.toJdkDate();
    }

    /**
     * 置空时间到当天最开始时分秒毫秒
     *
     * @param date 时间
     * @return {@link Date } 时间
     * @author HeathCHEN
     */
    public static Date getNewDateTimeTo000000000FromDate(Date date) {
        DateTime dateTime = DateUtil.dateNew(date);
        dateTime.setField(DateField.SECOND, 0);
        dateTime.setField(DateField.MINUTE, 0);
        dateTime.setField(DateField.HOUR_OF_DAY, 0);
        dateTime.setField(DateField.MILLISECOND, 0);
        return dateTime.toJdkDate();
    }

    /**
     * 反射构筑Query后获取Bean查询再转成对应类型
     *
     * @param <T>   查询结果的返回类型
     * @param o     查询参数
     * @param clazz 返回类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T> List<T> queryByReflect(Object o, Class<T> clazz) {
        Class<?> tClazz = o.getClass();
        while (!clazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                tClazz = o.getClass();
                break;
            } else {
                tClazz = superclass;
            }
        }
        if (tClazz.equals(clazz)) {
            return ((List<T>) queryByReflect(o));
        } else {
            List<?> list = queryByReflect(o);
            return BeanUtil.copyToList(list, clazz);
        }
    }

    /**
     * 反射构筑Query后获取Bean查询
     *
     * @param t 查询参数
     * @param <T> 查询参数dto或实体类的类型
     * @return {@link List } 查询结果
     * @author HeathCHEN
     */
    public static <T> List<T> queryByReflect(T t) {
        QueryWrapper query = getQuery(t);
        BaseMapper<?> baseMapper = ApplicationContextUtil.getMapperBean(t.getClass());
        if (ObjectUtil.isNotNull(baseMapper)) {
            return baseMapper.selectList(query);
        } else {
            throw new RuntimeException("can not find Mapper");
        }

    }

    /**
     * 根据CustomerQuery注解构筑单表通用查询
     *
     * @param t 查询参数
     * @param <T> 查询参数dto或实体类的类型
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> QueryWrapper<?> getQuery(T t) {

        ParamThreadLocal.setObjectMap(BeanUtil.beanToMap(t, false, true));
        Boolean startPage = (Boolean) ParamThreadLocal.getValueFromObjectMap(PageConst.START_PAGE);
        ParamThreadLocal.removeParamFromObjectMap(PageConst.START_PAGE);
        Class<?> clazz = t.getClass();
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        //创建排序的集合
        List<CustomerOrder> orderList = new ArrayList<>();
        //从类上注解获取排序字段(不参与筛选,但参与排序的字段)
        checkColumnOrderOnClass(clazz, orderList);
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = buildQueryByReflect(clazz, queryWrapper, orderList);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingQueryTypeStrategy.buildQuery(queryWrapper);
        //构筑排序
        buildQueryOrder(queryWrapper, startPage, orderList);

        return queryWrapper;
    }


    /**
     * 递归反射获取注解构建单表查询
     *
     * @param <T> 查询参数dto或实体类的类型
     * @param clazz        clazz
     * @param queryWrapper 查询包装
     * @param orderList    订单列表
     * @return {@link QueryWrapper } 查询queryWrapper
     * @author HeathCHEN
     */
    public static <T> QueryWrapper<T> buildQueryByReflect(Class<?> clazz, QueryWrapper<T> queryWrapper, List<CustomerOrder> orderList) {

        //如果父类为空,则不再递归
        if (ObjectUtil.isNull(clazz) || ObjectUtil.equals(clazz, Object.class)) {
            return queryWrapper;
        }

        //创建用于剔除已被使用的字段的集合

        Field[] clazzDeclaredFields = clazz.getDeclaredFields();
        if (ArrayUtil.isNotEmpty(clazzDeclaredFields)) {
            for (Field clazzDeclaredField : clazzDeclaredFields) {
                Field field = clazzDeclaredField;
                try {
                    //如果没有注解就暂且跳过,留到父类看看能否匹配,如果都没有匹配,最后做eq匹配
                    if (!field.isAnnotationPresent(CustomerQuery.class)) {
                        continue;
                    }
                    //获取属性上的注解
                    CustomerQuery customerQuery = field.getAnnotation(CustomerQuery.class);
                    String queryType = customerQuery.value().getCompareType();
                    //剔除不参与的参数
                    if (!customerQuery.exist()) {
                        ParamThreadLocal.removeParamFromObjectMap(field.getName());
                        continue;
                    }
                    //根据查询类型构建查询
                    QueryTypeStrategyManager.getQueryTypeStrategyToManager(queryType).buildQuery(customerQuery, clazz, field, queryWrapper);
                    //检查是否使用排序
                    checkColumnOrderOnField(customerQuery, field, orderList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //如果已匹配全部则直接返回查询,否则继续迭代
        if (CollectionUtil.isNotEmpty(ParamThreadLocal.getObjectMap())) {
            return buildQueryByReflect(clazz.getSuperclass(), queryWrapper, orderList);
        } else {
            return queryWrapper;
        }
    }


    /**
     * 检查是否使用排序
     *
     * @param customerQuery 注解CustomerQuery
     * @param field         字段
     * @param orderList     排序List
     * @author HeathCHEN
     */
    private static void checkColumnOrderOnField(CustomerQuery customerQuery, Field field, List<CustomerOrder> orderList) {
        //检测是否启用排序
        if (customerQuery.orderColumn()) {
            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setOrderColumn(field.getName());
            customerOrder.setOrderPriority(customerQuery.orderPriority());
            customerOrder.setOrderType(customerQuery.orderType());
            orderList.add(customerOrder);

        }
    }


    /**
     * 检查是否排序
     *
     * @param clazz     类
     * @param orderList 排序List
     * @author HeathCHEN
     */
    private static void checkColumnOrderOnClass(Class<?> clazz, List<CustomerOrder> orderList) {
        CustomerQuery customerQuery = clazz.getDeclaredAnnotation(CustomerQuery.class);
        if (ObjectUtil.isNotNull(customerQuery)) {
            String[] columns = customerQuery.orderColumns();
            OrderType[] orderTypes = customerQuery.orderTypes();

            if (ArrayUtil.isNotEmpty(columns)) {
                for (int i = 0; i < columns.length; i++) {
                    CustomerOrder customerOrder = new CustomerOrder();
                    customerOrder.setOrderColumn(columns[i]);
                    customerOrder.setOrderType(orderTypes[i]);
                    customerOrder.setOrderPriority(i + 1);
                    orderList.add(customerOrder);
                }

                //清除分页插件的排序参数 使用该注解分页
                com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
                PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());

            }
        }


    }


    /**
     * 构筑排序
     *
     * @param queryWrapper 查询queryWrapper
     * @param startPage    是否开启分页
     * @param orderList    排序List
     * @author HeathCHEN
     */
    private static void buildQueryOrder(QueryWrapper<?> queryWrapper, Boolean startPage, List<CustomerOrder> orderList) {
        if (ObjectUtil.isNull(startPage)) {
            startPage = true;
        }
        //对查询进行排序
        if (startPage && CollectionUtil.isNotEmpty(orderList)) {

            List<CustomerOrder> customerOrders = orderList.stream().sorted(Comparator.comparingInt(CustomerOrder::getOrderPriority)).collect(Collectors.toList());
            for (CustomerOrder customerOrder : customerOrders) {
                String underlineCase = StrUtil.toUnderlineCase(customerOrder.getOrderColumn());
                if (customerOrder.getOrderType().equals(OrderType.ASC)) {
                    queryWrapper.orderByAsc(underlineCase);
                } else {
                    queryWrapper.orderByDesc(underlineCase);
                }
            }

        }

    }

    /**
     * 利用注解标注字段更新冗余字段
     *
     * @param clazz              类
     * @param associationKey     关联键
     * @param newCacheFieldValue 新的冗余值
     * @author HeathCHEN
     */
    public static <T> void updateCacheField(Class<T> clazz, Object associationKey, Object newCacheFieldValue) {
        try {


            Field[] declaredFields = clazz.getDeclaredFields();

            CustomerCacheTableField customerCacheTableField = null;
            String tableField = null;
            CustomerCacheTableId customerCacheTableId = null;
            String tableId = null;

            if (ArrayUtil.isNotEmpty(declaredFields)) {
                for (Field field : declaredFields) {
                    if (ObjectUtil.isNull(customerCacheTableField)) {
                        customerCacheTableField = field.getAnnotation(CustomerCacheTableField.class);
                        if (ObjectUtil.isNotNull(customerCacheTableField)) {
                            tableField = field.getName();
                            continue;
                        }
                    }
                    if (ObjectUtil.isNull(customerCacheTableId)) {
                        customerCacheTableId = field.getAnnotation(CustomerCacheTableId.class);
                        if (ObjectUtil.isNotNull(customerCacheTableId)) {
                            TableField tableFieldAnnotation = field.getAnnotation(TableField.class);
                            tableId = tableFieldAnnotation.value();
                            if (StrUtil.isBlank(tableField)) {
                                TableId tableAnnotation = field.getAnnotation(TableId.class);
                                tableField = tableAnnotation.value();
                            }
                            if (StrUtil.isBlank(tableField)) {
                                tableField = StrUtil.toUnderlineCase(field.getName());
                            }
                            continue;
                        }
                    }
                }
            }
            if (ObjectUtil.isNull(customerCacheTableId) || ObjectUtil.isNull(customerCacheTableField)) {
                return;
            }


            BaseMapper mapperBean = ApplicationContextUtil.getMapperBean(clazz);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq(tableId, associationKey);
            Constructor<?> constructor = clazz.getConstructor();

            Object o = constructor.newInstance();
            ReflectUtil.setFieldValue(o, tableField, newCacheFieldValue);

            mapperBean.update(o, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 批量更新
     *
     * @param clazzArr
     * @param associationKey
     * @param newCacheFieldValue
     * @author HeathCHEN
     */
    public static void updateCacheField(Class<?>[] clazzArr, Object associationKey, Object newCacheFieldValue) {
        if (ArrayUtil.isNotEmpty(clazzArr)) {
            for (Class<?> clazz : clazzArr) {
                updateCacheField(clazz, associationKey, newCacheFieldValue);
            }

        }


    }

}

package cn.heath.mybatisplus.util.utils;


import cn.heath.mybatisplus.util.annotation.CustomerQuery;
import cn.heath.mybatisplus.util.consts.PageConst;
import cn.heath.mybatisplus.util.domain.CustomerOrder;
import cn.heath.mybatisplus.util.enums.OrderType;
import cn.heath.mybatisplus.util.strategy.AccurateMatchingStrategy;
import cn.heath.mybatisplus.util.strategy.QueryTypeStrategy;
import cn.heath.mybatisplus.util.strategy.QueryTypeStrategyManager;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageHelper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MyBatisPlusUtil {

    /**
     * 补全时间到当天最后时分秒毫秒
     *
     * @param date
     * @return {@link Date }
     * @author HeathCHEN
     * 2023/10/20
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
     * @param date
     * @return {@link Date }
     * @author HeathCHEN
     * 2023/10/20
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
     * @param o
     * @param clazz
     * @return {@link List }<{@link T }>
     * @author HeathCHEN
     * 2024/02/20
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
            List<T> list = queryByReflect(o);
            return list;
        } else {
            List list = queryByReflect(o);
            return BeanUtil.copyToList(list, clazz);
        }
    }

    /**
     * 反射构筑Query后获取Bean查询
     *
     * @param o
     * @return {@link List }
     * @author HeathCHEN
     * 2024/02/20
     */
    public static List queryByReflect(Object o) {
        QueryWrapper query = getQuery(o);
        Class<?> clazz = o.getClass();
        while (!clazz.isAnnotationPresent(TableName.class)) {
            Class<?> superclass = clazz.getSuperclass();
            if (ObjectUtil.isNull(superclass) || ObjectUtil.equals(superclass, Object.class)) {
                clazz = o.getClass();
                break;
            } else {
                clazz = superclass;
            }
        }
        IService service = ApplicationContextUtil.getIServiceBean(clazz);
        return service.list(query);
    }

    /**
     * 根据CustomerQuery注解构筑单表通用查询
     *
     * @param o o
     * @return {@link QueryWrapper }
     * @author HeathCHEN
     * 2023/08/03
     */
    public static QueryWrapper getQuery(Object o) {

        ParamThreadLocal.setObjectMap(BeanUtil.beanToMap(o, false, true));
        Boolean startPage = (Boolean) ParamThreadLocal.getValueFromObjectMap(PageConst.START_PAGE);
        ParamThreadLocal.removeParamFromObjectMap(PageConst.START_PAGE);
        Class<?> clazz = o.getClass();
        QueryWrapper queryWrapper = new QueryWrapper();
        //创建排序的集合
        List<CustomerOrder> orderList = new ArrayList<>();
        //从类上注解获取排序字段(不参与筛选,但参与排序的字段)
        checkColumnOrderOnClass(clazz, orderList);
        //遍历map然后从子级逐级反射获得注解判断比较类型
        queryWrapper = buildQueryByReflect(clazz, queryWrapper, orderList);
        //获取不到注解的,默认做精确匹配
        AccurateMatchingStrategy.buildQuery(queryWrapper);
        //构筑排序
        buildQueryOrder(queryWrapper, startPage, orderList);

        return queryWrapper;
    }


    /**
     * 递归反射获取注解构建单表查询
     *
     * @param clazz        clazz
     * @param queryWrapper 查询包装
     * @param orderList    订单列表
     * @return {@link QueryWrapper }
     * @author HeathCHEN
     * 2023/08/03
     */
    public static <T> QueryWrapper buildQueryByReflect(Class<?> clazz, QueryWrapper<T> queryWrapper, List<CustomerOrder> orderList) {

        //如果父类为空,则不再递归
        if (ObjectUtil.isNull(clazz) || ObjectUtil.equals(clazz, Object.class)) {
            return queryWrapper;
        }

        //创建用于剔除已被使用的字段的集合
        List<String> usedProperties = new ArrayList<>();
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
                        usedProperties.add(field.getName());
                        continue;
                    }

                    QueryTypeStrategy queryTypeStrategy = QueryTypeStrategyManager.getQueryTypeStrategyToManager(queryType);
                    //根据查询类型构建查询
                    queryTypeStrategy.buildQuery(customerQuery, field, queryWrapper);
                    //检查是否使用排序
                    checkColumnOrderOnField(customerQuery, field, orderList);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //如果已匹配全部则直接返回查询,否则继续迭代
        if (CollectionUtil.isNotEmpty(ParamThreadLocal.getObjectMap())) {
            return buildQueryByReflect(clazz.getSuperclass(), queryWrapper,orderList);
        } else {
            return queryWrapper;
        }
    }


    /**
     * 检查是否使用排序
     *
     * @param customerQuery
     * @param field
     * @param orderList
     */
    private static <T> void checkColumnOrderOnField(CustomerQuery customerQuery, Field field, List<CustomerOrder> orderList) {
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
     * 构筑排序
     *
     * @param clazz
     * @param orderList
     */
    private static <T> void checkColumnOrderOnClass(Class<?> clazz, List<CustomerOrder> orderList) {
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
     * @param queryWrapper
     * @param startPage
     * @param orderList
     */
    private static <T> void buildQueryOrder(QueryWrapper<T> queryWrapper, Boolean startPage, List<CustomerOrder> orderList) {
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
     * 获取表字段命
     *
     * @param field
     * @return {@link String}
     */
    public static String getTableColumnName(Field field) {

        String columnName = "";
        if (field.isAnnotationPresent(CustomerQuery.class)) {
            CustomerQuery customerQuery = field.getAnnotation(CustomerQuery.class);
            columnName = customerQuery.columnName();
            if (StrUtil.isNotBlank(columnName)) {
                return columnName;
            }
        }

        if (field.isAnnotationPresent(TableField.class)) {
            TableField tableField = field.getAnnotation(TableField.class);
            columnName = tableField.value();
            if (StrUtil.isNotBlank(columnName)) {
                return columnName;
            }
        }

        columnName = StrUtil.toUnderlineCase(field.getName());


        return columnName;
    }


//    /**
//     * 根据CustomerQuery注解构筑多表联查
//     *
//     * @param o o
//     * @return {@link QueryWrapper }
//     * @author HeathCHEN
//     * 2023/08/03
//     */
//    public static <T> MPJQueryWrapper getTableConnectionQuery(Class<T> dto, Object o) {
//        Map<String, Object> map = BeanUtil.beanToMap(o, false, true);
//
//        Boolean startPage = (Boolean) map.get("startPage");
//        map.remove("startPage");
//
//        Class<?> clazz = o.getClass();
//
//
//        MPJQueryWrapper mpjQueryWrapper = new MPJQueryWrapper();
//        mpjQueryWrapper.selectAll(dto);
//
//        //创建排序的集合
//        ArrayList<CustomerOrder> orderList = new ArrayList<>();
//
//        //从类上注解获取排序字段(不参与筛选,但参与排序的字段)
//        CustomerQuery customerQuery = clazz.getDeclaredAnnotation(CustomerQuery.class);
//        if (customerQuery != null) {
//            String[] columns = customerQuery.orderColumns();
//            OrderType[] orderTypes = customerQuery.orderTypes();
//
//            if (columns.length > 0) {
//                for (int i = 0; i < columns.length; i++) {
//                    CustomerOrder customerOrder = new CustomerOrder();
//                    customerOrder.setOrderColumn(columns[i]);
//                    customerOrder.setOrderType(orderTypes[i]);
//                    customerOrder.setOrderPriority(i + 1);
//                    orderList.add(customerOrder);
//                }
//                //清除分页插件的排序参数 使用该注解分页
//                com.github.pagehelper.Page<Object> localPage = PageHelper.getLocalPage();
//                PageHelper.startPage(localPage.getPageNum(), localPage.getPageSize());
//            }
//        }
//
//
//        //遍历map然后从子级逐级反射获得注解判断比较类型
//        mpjQueryWrapper = buildTableConnectionQueryByReflect(clazz, mpjQueryWrapper, map, orderList);
//
//
//        //获取不到注解的,默认做精确匹配
//        if (map.size() > 0) {
//            Set<Map.Entry<String, Object>> entries = map.entrySet();
//            for (Map.Entry<String, Object> entry : entries) {
//                mpjQueryWrapper.eq("t."+ StrUtil.toUnderlineCase(entry.getKey()), entry.getValue());
//            }
//        }
//
//
//        if (startPage == null) {
//            startPage = true;
//        }
//
//        //对查询进行排序
//        if (startPage && orderList.size() > 0) {
//
//            List<CustomerOrder> customerOrders = orderList.stream().sorted(Comparator.comparingInt(CustomerOrder::getOrderPriority)).collect(Collectors.toList());
//            for (CustomerOrder customerOrder : customerOrders) {
//                String underlineCase = StrUtil.toUnderlineCase(customerOrder.getOrderColumn());
//                if (customerOrder.getOrderType().equals(OrderType.ASC)) {
//                    mpjQueryWrapper.orderByAsc(underlineCase);
//                } else {
//                    mpjQueryWrapper.orderByDesc(underlineCase);
//                }
//            }
//
//        }
//
//
//        return mpjQueryWrapper;
//    }


//
//    /**
//     * 递归反射获取注解构建单表查询
//     *
//     * @param clazz           clazz
//     * @param mpjQueryWrapper 查询包装
//     * @param map             地图
//     * @param orderList       订单列表
//     * @return {@link QueryWrapper }
//     * @author HeathCHEN
//     * 2023/08/03
//     */
//    public static <T> MPJQueryWrapper buildTableConnectionQueryByReflect(Class<?> clazz, MPJQueryWrapper<T> mpjQueryWrapper, Map<String, Object> map, ArrayList<CustomerOrder> orderList) {
//
//        //如果父类为空,则不再递归
//        if (clazz == null || clazz == Object.class) {
//            return mpjQueryWrapper;
//        }
//
//
//        //创建用于剔除已被使用的字段的集合
//        ArrayList<String> usedProperties = new ArrayList<>();
//
//        Field[] clazzDeclaredFields = clazz.getDeclaredFields();
//
//        if (clazzDeclaredFields.length != 0) {
//            for (Field clazzDeclaredField : clazzDeclaredFields) {
//
//
//                Field field = clazzDeclaredField;
//                try {
//
//                    String columnName = "";
//
//                    //获取属性上的注解
//                    CustomerQuery customerQuery = field.getAnnotation(CustomerQuery.class);
//
//                    TableField tableField = field.getAnnotation(TableField.class);
//
//                    if (tableField != null) {
//                        columnName = tableField.value();
//                    }
//
//                    TableId tableId = field.getAnnotation(TableId.class);
//
//                    if (tableId != null) {
//                        columnName = tableId.value();
//                    }
//
//
//                    //如果没有注解就暂且跳过,留到父类看看能否匹配,如果都没有匹配,最后做eq匹配
//                    if (customerQuery == null && customerQuery.joinEntityClass() == Object.class) {
//                        continue;
//                    }
//                    //获取dto中参数
//                    Object o = map.get(field.getName());
//                    String queryType = customerQuery.value().getCompareType();
//
//                    //判断是否BETWEEN判断
//                    if (!customerQuery.value().equals(QueryType.BETWEEN) && !customerQuery.value().equals(QueryType.NOT_BETWEEN)) {
//                        if (o == null && customerQuery.joinEntityClass() == Object.class) {
//                            continue;
//                        }
//                    }
//
//
//                    //剔除不参与的参数
//                    if (!customerQuery.exist()) {
//                        usedProperties.add(field.getName());
//                        continue;
//                    }
//
//                    //将属性转为下划线格式
//                    String underlineCase = StrUtil.toUnderlineCase(field.getName());
//                    //如果自定义了字段名,直接采用
//                    if (StrUtil.isNotBlank(customerQuery.column())) {
//                        underlineCase = customerQuery.column();
//                    }
//
//
//                    Class<?> joinEntityClass = customerQuery.joinEntityClass();
//
//                    if (joinEntityClass != Object.class) {
//
//                        try {
//
//                            TableInfo tableInfo = SqlHelper.table(clazz);
//                            String tableName = tableInfo.getTableName();
//                            TableInfo joinTableInfo = SqlHelper.table(joinEntityClass);
//                            String joinTableName = joinTableInfo.getTableName();
//                            String joinTableId = joinTableInfo.getKeyColumn();
//
//                            List<TableFieldInfo> fieldList = joinTableInfo.getFieldList();
//
//                            if (fieldList.size() > 0) {
//                                for (TableFieldInfo tableFieldInfo : fieldList) {
//
//                                    mpjQueryWrapper.select(tableName + "." + tableFieldInfo.getColumn());
//                                }
//                            }
//
//
//                            StringBuffer stringBuffer = new StringBuffer();
//
//                            stringBuffer.append(joinTableName);
//                            stringBuffer.append(" ");
//                            stringBuffer.append(" on ");
//                            stringBuffer.append(tableName);
//                            stringBuffer.append(".");
//                            stringBuffer.append(columnName);
//                            stringBuffer.append(" = ");
//                            stringBuffer.append(joinTableName);
//                            stringBuffer.append(".");
//                            stringBuffer.append(joinTableId);
//
//                            String joinSql = stringBuffer.toString();
//                            if (customerQuery.joinType().equals(JoinType.LEFT_JOIN)) {
//                                mpjQueryWrapper.leftJoin(joinSql);
//                            } else if (customerQuery.joinType().equals(JoinType.RIGHT_JOIN)) {
//                                mpjQueryWrapper.rightJoin(joinSql);
//                            } else if (customerQuery.joinType().equals(JoinType.INNER_JOIN)) {
//                                mpjQueryWrapper.innerJoin(joinSql);
//                            } else if (customerQuery.joinType().equals(JoinType.FULL_JOIN)) {
//                                mpjQueryWrapper.fullJoin(joinSql);
//                            }
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                            log.info(e.getMessage());
//                        }
//
//
//                    }
//
//                    String[] orColumns = customerQuery.orColumn();
//
//                    //根据查询类型构建查询
//                    if (queryType.equals(QueryType.EQ.getCompareType())) {
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.eq(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.eq(orColumn, o);
//                                        }
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.eq(underlineCase, o);
//                            }
//                        }
//
//                    } else if (queryType.equals(QueryType.NOT_EQUAL.getCompareType())) {
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.ne(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.ne(orColumn, o);
//                                        }
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.ne(underlineCase, o);
//                            }
//                        }
//                    } else if (queryType.equals(QueryType.LIKE.getCompareType())) {
//
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.like(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.like(orColumn, o);
//                                        }
//
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.like(underlineCase, o);
//                            }
//                        }
//
//                    } else if (queryType.equals(QueryType.NOT_LIKE.getCompareType())) {
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.notLike(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.notLike(orColumn, o);
//                                        }
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.notLike(underlineCase, o);
//                            }
//                        }
//                    } else if (queryType.equals(QueryType.LIKE_LEFT.getCompareType())) {
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.likeLeft(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.likeLeft(orColumn, o);
//                                        }
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.likeLeft(underlineCase, o);
//                            }
//                        }
//
//                    } else if (queryType.equals(QueryType.LIKE_RIGHT.getCompareType())) {
//
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.likeRight(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.likeRight(orColumn, o);
//                                        }
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.likeRight(underlineCase, o);
//                            }
//                        }
//
//                    } else if (queryType.equals(QueryType.BETWEEN.getCompareType())) {
//
//                        Object startValue = map.get(customerQuery.betweenVal1());
//                        Object endValue = map.get(customerQuery.betweenVal2());
//
//                        if (startValue != null) {
//                            mpjQueryWrapper.ge(underlineCase, startValue);
//                            usedProperties.add(customerQuery.betweenVal1());
//                        }
//                        if (endValue != null) {
//                            mpjQueryWrapper.le(underlineCase, endValue);
//                            usedProperties.add(customerQuery.betweenVal2());
//                        }
//
//
//                    } else if (queryType.equals(QueryType.NOT_BETWEEN.getCompareType())) {
//
//                        Object startValue = map.get(customerQuery.betweenVal1());
//                        Object endValue = map.get(customerQuery.betweenVal2());
//
//                        if (startValue != null) {
//                            mpjQueryWrapper.le(underlineCase, startValue);
//                            usedProperties.add(customerQuery.betweenVal1());
//                        }
//                        if (endValue != null) {
//                            mpjQueryWrapper.ge(underlineCase, endValue);
//                            usedProperties.add(customerQuery.betweenVal2());
//                        }
//
//
//                    } else if (queryType.equals(QueryType.LESS_THAN.getCompareType())) {
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.eq(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.eq(orColumn, o);
//                                        }
//
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.eq(underlineCase, o);
//                            }
//                        }
//
//                        if (ObjectUtil.isNotNull(o)) {
//                            mpjQueryWrapper.lt(underlineCase, o);
//                        }
//                    } else if (queryType.equals(QueryType.LESS_EQUAL.getCompareType())) {
//
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.le(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.le(orColumn, o);
//                                        }
//
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.le(underlineCase, o);
//                            }
//                        }
//                    } else if (queryType.equals(QueryType.GREATER_THAN.getCompareType())) {
//
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.gt(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.gt(orColumn, o);
//                                        }
//
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.gt(underlineCase, o);
//                            }
//                        }
//
//                    } else if (queryType.equals(QueryType.GREATER_EQUAL.getCompareType())) {
//
//                        if (ArrayUtil.isNotEmpty(orColumns)) {
//                            String tempUnderlineCase = underlineCase;
//                            mpjQueryWrapper.and(tQueryWrapper -> {
//                                        tQueryWrapper.ge(tempUnderlineCase, o);
//                                        for (String orColumn : orColumns) {
//                                            tQueryWrapper.or();
//                                            tQueryWrapper.ge(orColumn, o);
//                                        }
//                                    }
//                            );
//                        } else {
//                            if (ObjectUtil.isNotNull(o)) {
//                                mpjQueryWrapper.ge(underlineCase, o);
//                            }
//                        }
//
//                    } else if (queryType.equals(QueryType.IN.getCompareType())) {
//
//                        if (ObjectUtil.isNotNull(o)) {
//                            if (o instanceof Collection) {
//                                Collection<?> value = (Collection<?>) o;
//                                mpjQueryWrapper.in(underlineCase, value);
//                            }
//
//                            if (o instanceof Object[]) {
//                                Object[] value = (Object[]) o;
//                                mpjQueryWrapper.in(underlineCase, value);
//                            }
//                        }
//
//
//                    } else if (queryType.equals(QueryType.NOT_IN.getCompareType())) {
//                        if (ObjectUtil.isNotNull(o)) {
//                            if (o instanceof Collection) {
//                                Collection<?> value = (Collection<?>) o;
//                                mpjQueryWrapper.notIn(underlineCase, value);
//                            }
//
//                            if (o instanceof Object[]) {
//                                Object[] value = (Object[]) o;
//                                mpjQueryWrapper.notIn(underlineCase, value);
//                            }
//                        }
//                    } else if (queryType.equals(QueryType.SQL.getCompareType())) {
//                        if (ObjectUtil.isNotNull(o)) {
//                            mpjQueryWrapper.apply(customerQuery.sql(), o);
//                        }
//                    }
//
//
//                    //检测是否启用排序
//                    if (customerQuery.orderColumn()) {
//                        CustomerOrder customerOrder = new CustomerOrder();
//                        customerOrder.setOrderColumn(field.getName());
//                        customerOrder.setOrderPriority(customerQuery.orderPriority());
//                        customerOrder.setOrderType(customerQuery.orderType());
//                        orderList.add(customerOrder);
//
//                    }
//                    //加入需要剔除的已匹配的值
//                    usedProperties.add(field.getName());
//
//                } catch (Exception e) {
//
//                }
//
//            }
//        }
//
//        if (usedProperties.size() > 0) {
//            //剔除已匹配的值,已减少匹配遍历数和防止字段重复匹配
//            for (String key : usedProperties) {
//                map.remove(key);
//            }
//        }
//        //如果已匹配全部则直接返回查询,否则继续迭代
//        if (map.size() > 0) {
//            return buildTableConnectionQueryByReflect(clazz.getSuperclass(), mpjQueryWrapper, map, orderList);
//        } else {
//            return mpjQueryWrapper;
//        }
//    }


}

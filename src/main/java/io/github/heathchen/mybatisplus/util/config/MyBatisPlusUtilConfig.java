package io.github.heathchen.mybatisplus.util.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;
import io.github.heathchen.mybatisplus.util.strategy.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * MyBatisPlusUtil配置类
 *
 * @author Administrator
 * @since 2024/02/22
 */

public class MyBatisPlusUtilConfig {
    /**
     * 全局匹配模式
     */
    @Value("${myBatisPlusUtil.globalMatchMode:allMatchMode}")
    public String globalMatchMode;

    /**
     * 分页参数:是否开启分页默认属性名
     */
    @Value("${myBatisPlusUtil.starPagePropertyName:starPage}")
    public String starPagePropertyName;

    /**
     * 分页参数:是否升序(自然排序)默认属性名
     */
    @Value("${myBatisPlusUtil.isAscPropertyName:isAsc}")
    public String isAscPropertyName;

    /**
     * 分页参数:页面大小默认属性名
     */
    @Value("${myBatisPlusUtil.pageSizePropertyName:pageSize}")
    public String pageSizePropertyName;
    /**
     * 分页参数:页码大小默认属性名
     */
    @Value("${myBatisPlusUtil.pageNumPropertyName:pageNum}")
    public String pageNumPropertyName;

    /**
     * 分页参数:排序字段默认属性名
     */
    @Value("${myBatisPlusUtil.orderByColumnPropertyName:orderByColumn}")
    public String orderByColumnPropertyName;

    /**
     * 分页参数:分页合理化默认属性名
     */
    @Value("${myBatisPlusUtil.reasonablePropertyName:reasonable}")
    public String reasonablePropertyName;

    /**
     * 分页参数:是否开启分页默认值 开启分页
     */
    @Value("${myBatisPlusUtil.starPagePropertyDefaultValue:true}")
    public Boolean starPagePropertyDefaultValue;


    /**
     * 分页参数:是否升序(自然排序)默认值 自然排序
     */
    @Value("${myBatisPlusUtil.isAscPropertyDefaultValue:true}")
    public Boolean isAscPropertyDefaultValue;

    /**
     * 分页参数:页面大小默认值 15
     */
    @Value("${myBatisPlusUtil.pageSizePropertyName:15}")
    public Integer pageSizePropertyDefaultValue;
    /**
     * 分页参数:页码大小默认值 1
     */
    @Value("${myBatisPlusUtil.pageNumPropertyDefaultValue:1}")
    public Integer pageNumPropertyDefaultValue;

    /**
     * 分页参数:排序字段默认值
     */
    @Value("${myBatisPlusUtil.orderByColumnPropertyDefaultValue:create_time}")
    public String orderByColumnPropertyDefaultValue;


    /**
     * 分页参数:分页合理化默认值 开启分页合理化
     */
    @Value("${myBatisPlusUtil.reasonablePropertyDefaultValue:true}")
    public Boolean reasonablePropertyDefaultValue;

    public MyBatisPlusUtilConfig() {
        super();
        //初始化策略类
        initStrategy();
        //设置全局的日志工厂
        if (ObjectUtil.isNull(GlobalLogFactory.get())) {
            GlobalLogFactory.set(Slf4jLogFactory.class);
        }

    }

    public MyBatisPlusUtilConfig(String globalMatchMode) {
        this();
        this.globalMatchMode = globalMatchMode;
    }

    /**
     * 初始化策略类
     *
     * @author HeathCHEN
     */
    public void initStrategy() {
        new BetweenQueryTypeStrategy();
        new EqQueryTypeStrategy();
        new GreaterEqualQueryTypeStrategy();
        new GreaterThanQueryTypeStrategy();
        new InQueryTypeStrategy();
        new LessEqualQueryTypeStrategy();
        new LessThanQueryTypeStrategy();
        new LikeLeftQueryTypeStrategy();
        new LikeRightQueryTypeStrategy();
        new LikeQueryTypeStrategy();
        new NotBetweenQueryTypeStrategy();
        new NotEqQueryTypeStrategy();
        new NotInQueryTypeStrategy();
        new NotLikeQueryTypeStrategy();
        new SqlQueryTypeStrategy();
    }

    public String getGlobalMatchMode() {
        return globalMatchMode;
    }

    public void setGlobalMatchMode(String globalMatchMode) {
        this.globalMatchMode = globalMatchMode;
    }

    public String getStarPagePropertyName() {
        return starPagePropertyName;
    }

    public void setStarPagePropertyName(String starPagePropertyName) {
        this.starPagePropertyName = starPagePropertyName;
    }

    public String getIsAscPropertyName() {
        return isAscPropertyName;
    }

    public void setIsAscPropertyName(String isAscPropertyName) {
        this.isAscPropertyName = isAscPropertyName;
    }

    public String getPageSizePropertyName() {
        return pageSizePropertyName;
    }

    public void setPageSizePropertyName(String pageSizePropertyName) {
        this.pageSizePropertyName = pageSizePropertyName;
    }

    public String getPageNumPropertyName() {
        return pageNumPropertyName;
    }

    public void setPageNumPropertyName(String pageNumPropertyName) {
        this.pageNumPropertyName = pageNumPropertyName;
    }

    public String getReasonablePropertyName() {
        return reasonablePropertyName;
    }

    public void setReasonablePropertyName(String reasonablePropertyName) {
        this.reasonablePropertyName = reasonablePropertyName;
    }

    public String getOrderByColumnPropertyName() {
        return orderByColumnPropertyName;
    }

    public void setOrderByColumnPropertyName(String orderByColumnPropertyName) {
        this.orderByColumnPropertyName = orderByColumnPropertyName;
    }

    public Boolean getStarPagePropertyDefaultValue() {
        return starPagePropertyDefaultValue;
    }

    public void setStarPagePropertyDefaultValue(Boolean starPagePropertyDefaultValue) {
        this.starPagePropertyDefaultValue = starPagePropertyDefaultValue;
    }

    public Boolean getAscPropertyDefaultValue() {
        return isAscPropertyDefaultValue;
    }

    public void setAscPropertyDefaultValue(Boolean ascPropertyDefaultValue) {
        isAscPropertyDefaultValue = ascPropertyDefaultValue;
    }

    public Integer getPageSizePropertyDefaultValue() {
        return pageSizePropertyDefaultValue;
    }

    public void setPageSizePropertyDefaultValue(Integer pageSizePropertyDefaultValue) {
        this.pageSizePropertyDefaultValue = pageSizePropertyDefaultValue;
    }

    public Integer getPageNumPropertyDefaultValue() {
        return pageNumPropertyDefaultValue;
    }

    public void setPageNumPropertyDefaultValue(Integer pageNumPropertyDefaultValue) {
        this.pageNumPropertyDefaultValue = pageNumPropertyDefaultValue;
    }

    public String getOrderByColumnPropertyDefaultValue() {
        return orderByColumnPropertyDefaultValue;
    }

    public void setOrderByColumnPropertyDefaultValue(String orderByColumnPropertyDefaultValue) {
        this.orderByColumnPropertyDefaultValue = orderByColumnPropertyDefaultValue;
    }

    public Boolean getReasonablePropertyDefaultValue() {
        return reasonablePropertyDefaultValue;
    }

    public void setReasonablePropertyDefaultValue(Boolean reasonablePropertyDefaultValue) {
        this.reasonablePropertyDefaultValue = reasonablePropertyDefaultValue;
    }


}

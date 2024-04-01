package io.github.heathchen.mybatisplus.util.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.dialect.slf4j.Slf4jLogFactory;
import io.github.heathchen.mybatisplus.util.consts.PageAndOrderConst;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;
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
    @Value("${myBatisPlusUtil.globalMatchMode:}")
    public String globalMatchMode;

    /**
     * 分页参数:是否开启分页默认属性名
     */
    @Value("${myBatisPlusUtil.starPagePropertyName:}")
    public String starPagePropertyName;

    /**
     * 分页参数:是否升序(自然排序)默认属性名
     */
    @Value("${myBatisPlusUtil.isAscPropertyName:}")
    public String isAscPropertyName;

    /**
     * 分页参数:页面大小默认属性名
     */
    @Value("${myBatisPlusUtil.pageSizePropertyName:}")
    public String pageSizePropertyName;
    /**
     * 分页参数:页码大小默认属性名
     */
    @Value("${myBatisPlusUtil.pageNumPropertyName:}")
    public String pageNumPropertyName;

    /**
     * 分页参数:排序字段默认属性名
     */
    @Value("${myBatisPlusUtil.orderByColumnPropertyName:}")
    public String orderByColumnPropertyName;

    /**
     * 分页参数:分页合理化默认属性名
     */
    @Value("${myBatisPlusUtil.reasonablePropertyName:}")
    public String reasonablePropertyName;

    /**
     * 分页参数:是否开启分页默认值 开启分页
     */
    @Value("${myBatisPlusUtil.starPagePropertyDefaultValue:}")
    public Boolean starPagePropertyDefaultValue;


    /**
     * 分页参数:是否升序(自然排序)默认值 自然排序
     */
    @Value("${myBatisPlusUtil.isAscPropertyDefaultValue:}")
    public Boolean isAscPropertyDefaultValue;

    /**
     * 分页参数:页面大小默认值 15
     */
    @Value("${myBatisPlusUtil.pageSizePropertyName:}")
    public Integer pageSizePropertyDefaultValue;
    /**
     * 分页参数:页码默认值 1
     */
    @Value("${myBatisPlusUtil.pageNumPropertyDefaultValue:}")
    public Integer pageNumPropertyDefaultValue;

    /**
     * 分页参数:排序字段默认值
     */
    @Value("${myBatisPlusUtil.orderByColumnPropertyDefaultValue:}")
    public String orderByColumnPropertyDefaultValue;


    /**
     * 分页参数:是否排序默认值
     */
    @Value("${myBatisPlusUtil.orderColumnPropertyDefaultValue:}")
    public Boolean orderColumnPropertyDefaultValue;

    /**
     * 分页参数:分页合理化默认值 开启分页合理化
     */
    @Value("${myBatisPlusUtil.reasonablePropertyDefaultValue:}")
    public Boolean reasonablePropertyDefaultValue;

    public MyBatisPlusUtilConfig() {
        super();
        //设置全局的日志工厂
        if (ObjectUtil.isNull(GlobalLogFactory.get())) {
            GlobalLogFactory.set(Slf4jLogFactory.class);
        }

    }


    public String getGlobalMatchMode() {

        if (StrUtil.isBlank(globalMatchMode)) {
            return MatchMode.ALL_MATCH_MODE.getName();
        }
        return globalMatchMode;
    }

    public void setGlobalMatchMode(String globalMatchMode) {
        this.globalMatchMode = globalMatchMode;
    }

    public String getStarPagePropertyName() {

        if (StrUtil.isBlank(starPagePropertyName)) {
            return PageAndOrderConst.START_PAGE_DEFAULT_NAME;
        }

        return starPagePropertyName;
    }

    public void setStarPagePropertyName(String starPagePropertyName) {
        this.starPagePropertyName = starPagePropertyName;
    }

    public String getIsAscPropertyName() {


        if (StrUtil.isBlank(isAscPropertyName)) {
            return PageAndOrderConst.IS_ASC_DEFAULT_NAME;
        }

        return isAscPropertyName;
    }

    public void setIsAscPropertyName(String isAscPropertyName) {
        this.isAscPropertyName = isAscPropertyName;
    }

    public String getPageSizePropertyName() {

        if (StrUtil.isBlank(pageSizePropertyName)) {
            return PageAndOrderConst.PAGE_SIZE_DEFAULT_NAME;
        }

        return pageSizePropertyName;
    }

    public void setPageSizePropertyName(String pageSizePropertyName) {
        this.pageSizePropertyName = pageSizePropertyName;
    }

    public String getPageNumPropertyName() {


        if (StrUtil.isBlank(pageNumPropertyName)) {
            return PageAndOrderConst.PAGE_NUM_DEFAULT_NAME;
        }

        return pageNumPropertyName;
    }

    public void setPageNumPropertyName(String pageNumPropertyName) {
        this.pageNumPropertyName = pageNumPropertyName;
    }

    public String getReasonablePropertyName() {

        if (StrUtil.isBlank(reasonablePropertyName)) {
            return PageAndOrderConst.REASONABLE_DEFAULT_NAME;
        }
        return reasonablePropertyName;
    }

    public void setReasonablePropertyName(String reasonablePropertyName) {
        this.reasonablePropertyName = reasonablePropertyName;
    }

    public String getOrderByColumnPropertyName() {


        if (StrUtil.isBlank(orderByColumnPropertyName)) {
            return PageAndOrderConst.ORDER_BY_COLUMN_DEFAULT_NAME;
        }
        return orderByColumnPropertyName;
    }

    public void setOrderByColumnPropertyName(String orderByColumnPropertyName) {
        this.orderByColumnPropertyName = orderByColumnPropertyName;
    }

    public Boolean getStarPagePropertyDefaultValue() {
        if (ObjectUtil.isNull(starPagePropertyDefaultValue)) {
            return PageAndOrderConst.START_PAGE_DEFAULT_VALUE;
        }

        return starPagePropertyDefaultValue;
    }

    public void setStarPagePropertyDefaultValue(Boolean starPagePropertyDefaultValue) {
        this.starPagePropertyDefaultValue = starPagePropertyDefaultValue;
    }

    public Boolean getAscPropertyDefaultValue() {

        if (ObjectUtil.isNull(isAscPropertyDefaultValue)) {
            return PageAndOrderConst.IS_ASC_DEFAULT_VALUE;
        }

        return isAscPropertyDefaultValue;
    }

    public void setAscPropertyDefaultValue(Boolean ascPropertyDefaultValue) {
        isAscPropertyDefaultValue = ascPropertyDefaultValue;
    }

    public Integer getPageSizePropertyDefaultValue() {
        if (ObjectUtil.isNull(pageSizePropertyDefaultValue)) {
            return PageAndOrderConst.PAGE_SIZE_DEFAULT_VALUE;
        }
        return pageSizePropertyDefaultValue;
    }

    public void setPageSizePropertyDefaultValue(Integer pageSizePropertyDefaultValue) {
        this.pageSizePropertyDefaultValue = pageSizePropertyDefaultValue;
    }

    public Integer getPageNumPropertyDefaultValue() {
        if (ObjectUtil.isNull(pageNumPropertyDefaultValue)) {
            return PageAndOrderConst.PAGE_NUM_DEFAULT_VALUE;
        }
        return pageNumPropertyDefaultValue;
    }

    public void setPageNumPropertyDefaultValue(Integer pageNumPropertyDefaultValue) {
        this.pageNumPropertyDefaultValue = pageNumPropertyDefaultValue;
    }

    public String getOrderByColumnPropertyDefaultValue() {

        if (StrUtil.isBlank(orderByColumnPropertyDefaultValue)) {
            return PageAndOrderConst.ORDER_BY_COLUMN_DEFAULT_VALUE;
        }
        return orderByColumnPropertyDefaultValue;
    }

    public void setOrderByColumnPropertyDefaultValue(String orderByColumnPropertyDefaultValue) {
        this.orderByColumnPropertyDefaultValue = orderByColumnPropertyDefaultValue;
    }

    public Boolean getReasonablePropertyDefaultValue() {

        if (ObjectUtil.isNull(reasonablePropertyDefaultValue)) {
            return PageAndOrderConst.REASONABLE_DEFAULT_VALUE;
        }
        return reasonablePropertyDefaultValue;
    }

    public void setReasonablePropertyDefaultValue(Boolean reasonablePropertyDefaultValue) {
        this.reasonablePropertyDefaultValue = reasonablePropertyDefaultValue;
    }

    public Boolean getOrderColumnPropertyDefaultValue() {

        if (ObjectUtil.isNull(orderColumnPropertyDefaultValue)) {
            return PageAndOrderConst.ORDER_COLUMN_DEFAULT_VALUE;
        }
        return orderColumnPropertyDefaultValue;
    }

    public void setOrderColumnPropertyDefaultValue(Boolean orderColumnPropertyDefaultValue) {
        this.orderColumnPropertyDefaultValue = orderColumnPropertyDefaultValue;
    }
}

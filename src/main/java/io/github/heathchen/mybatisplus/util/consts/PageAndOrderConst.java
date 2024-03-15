package io.github.heathchen.mybatisplus.util.consts;

/**
 * 分页插件常量类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/26
 */
public class PageAndOrderConst {

    /**
     * 默认排序列表名
     */
    public static final String START_PAGE_DEFAULT_NAME = "startPage";

    /**
     * 默认排序列表名
     */
    public static final String ORDER_MAP_DEFAULT_NAME = "orderMap";
    /**
     * 默认是否升序名(自然排序)
     */
    public static final String IS_ASC_DEFAULT_NAME = "isAsc";

    /**
     * 默认页面大小名
     */
    public static final String PAGE_SIZE_DEFAULT_NAME = "pageSize";

    /**
     * 默认页码名
     */
    public static final String PAGE_NUM_DEFAULT_NAME = "pageNum";

    /**
     * 默认排序字段名
     */
    public static final String ORDER_BY_COLUMN_DEFAULT_NAME = "orderByColumn";

    /**
     * 默认开启排序名
     */
    public static final String ORDER_COLUMN_DEFAULT_NAME = "orderColumn";
    /**
     * 默认分页合理化名
     */
    public static final String REASONABLE_DEFAULT_NAME = "reasonable";


    /**
     * 默认排序列表值
     */
    public static final Boolean START_PAGE_DEFAULT_VALUE = Boolean.FALSE;

    /**
     * 默认是否升序值(自然排序)
     */
    public static final Boolean IS_ASC_DEFAULT_VALUE = Boolean.FALSE;

    /**
     * 默认页面大小值
     */
    public static final Integer PAGE_SIZE_DEFAULT_VALUE =  15;

    /**
     * 默认页码值
     */
    public static final Integer PAGE_NUM_DEFAULT_VALUE = 1;

    /**
     * 默认排序字段值
     */
    public static final String ORDER_BY_COLUMN_DEFAULT_VALUE = "CREATE_TIME";

    /**
     * 默认开启排序值
     */
    public static final Boolean ORDER_COLUMN_DEFAULT_VALUE = Boolean.FALSE;

    /**
     * 默认分页合理化名
     */
    public static final Boolean REASONABLE_DEFAULT_VALUE = Boolean.TRUE;
}

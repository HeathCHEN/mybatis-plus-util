package io.github.heathchen.mybatisplus.util.enums;

/**
 * 
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/02/29
 */
public enum MatchMode {
    /**
     * 对全部查询参数做数据过滤,如果字段上有@QueryField
     * 则按字段上的标注的查询类型做数据过滤
     *
     */
    ALL_MATCH_MODE("全部匹配模式"),
    /**
     *  只对标记了@QueryField的字段做数据过滤
     */
    ACCURATE_MATCH_MODE("精确匹配模式");


    /**
     * 描述
     */
    private final String description;

    MatchMode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

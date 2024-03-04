package io.github.heathchen.mybatisplus.util.enums;

/**
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/04
 */
public enum MatchMode {
    /**
     * 对全部查询参数做数据过滤,如果字段上有@QueryField
     * 则按字段上的标注的查询类型做数据过滤
     */
    ALL_MATCH_MODE("allMatchMode", "全部匹配模式"),
    /**
     * 只对标记了@QueryField的字段做数据过滤
     */
    ACCURATE_MATCH_MODE("accurateMatchMode", "精确匹配模式"),
    /**
     * 按照全局配置的匹配模式做匹配,默认是ALL_MATCH_MODE
     */
    USING_GLOBAL_MODE("usingGlobalMode", "全局配置的模式");


    /**
     * 描述
     */
    private final String description;
    /**
     * 名词
     */
    private final String name;

    MatchMode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}

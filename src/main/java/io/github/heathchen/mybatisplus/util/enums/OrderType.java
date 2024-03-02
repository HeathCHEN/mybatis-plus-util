package io.github.heathchen.mybatisplus.util.enums;

/**
 * 排序枚举类
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2023/10/21
 */
public enum OrderType {

    ASC("正序"),
    DESC("倒序"),
    NONE("不参与排序");
    /**
     * 描述
     */
    private final String description;

    OrderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

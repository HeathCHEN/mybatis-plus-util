package io.github.heathchen.mybatisplus.util.enums;

/**
 * 表连接枚举类
 * @author HeathCHEN
 * @version 1.0
 * 2023/10/21
 */
public enum JoinType {

    LEFT_JOIN("左连接"),
    RIGHT_JOIN("右连接"),
    INNER_JOIN("内连接"),
    FULL_JOIN("全连接");

    private final String description;
    JoinType(String description) {
        this.description = description;
    }


}

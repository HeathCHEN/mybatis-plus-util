package cn.heath.mybatisplus.util.enums;

/**
 * @author HeathCHEN
 * @version 1.0
 * 2023/10/21
 */
public enum OrderType {

    ASC("正序"),
    DESC("倒序");
    /**
     * 描述
     */
    private final String description;

    OrderType(String description) {
        this.description = description;
    }


}

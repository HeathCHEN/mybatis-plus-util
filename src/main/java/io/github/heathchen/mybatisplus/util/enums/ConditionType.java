package io.github.heathchen.mybatisplus.util.enums;

/**
 * 字段值枚举类
 *
 * @author HeathCHEN
 * @since 2024/02/29
 */
public enum ConditionType {
    /**
     * 当查询参数对应字段值是空时参与查询,会拼接对应字段的SQL: TABLE_COLUMN is null
     */
    TABLE_COLUMN_IS_NULL("当Field为空时,不忽略NULL值,作 TABLE_COLUMN is null"),
    /**
     * 当查询参数对应字段值是空时参与查询,会拼接对应字段的SQL: TABLE_COLUMN is not null
     */
    TABLE_COLUMN_IS_NOT_NULL("当Field为空时,不忽略NULL值,作 TABLE_COLUMN is not null"),
    /**
     * 当查询参数对应字段值是空时,不参与查询
     */
    IGNORE_NULL_VALUE("当Field为空时,忽略NULL值,不作 TABLE_COLUMN is null");


    /**
     * 描述
     */
    private final String description;

    ConditionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

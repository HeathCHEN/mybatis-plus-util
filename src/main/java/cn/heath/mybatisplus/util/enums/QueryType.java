package cn.heath.mybatisplus.util.enums;

/**
 *  查询类型 与myBatis-plus对应
 * @author HeathCHEN
 * @version 1.0
 * 2023/07/24
 */
public enum QueryType {

    EQ("eq","等于"),
    NOT_EQUAL("ne","不等于"),
    LIKE("like","LIKE '%值%'"),
    NOT_LIKE("notLike","NOT LIKE '%值%'"),
    LIKE_LEFT("likeLeft","LIKE '%值'"),
    LIKE_RIGHT("likeRight","LIKE '值%'"),
    BETWEEN("between","BETWEEN 值1 AND 值2"),
    NOT_BETWEEN("notBetween","NOT BETWEEN 值1 AND 值2"),
    LESS_THAN("lt","小于 <"),
    LESS_EQUAL("le","小于等于 <="),
    GREATER_THAN("gt","大于 >"),
    GREATER_EQUAL("ge","大于等于 >="),
    IN("in","字段 IN (v0, v1, ...)"),
    NOT_IN("notIn","字段 NOT IN (value.get(0), value.get(1), ...)"),
    SQL("sql","SQL语句"),
    ;

    /**
     * 比较类型
     */
    private final String compareType;

    /**
     * 描述
     */
    private final String description;

    QueryType(String compareType,String description) {
        this.compareType = compareType;
        this.description = description;
    }

    public String getCompareType() {
        return compareType;
    }

    public String getDescription() {
        return description;
    }
}

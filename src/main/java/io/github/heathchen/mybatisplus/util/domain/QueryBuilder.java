package io.github.heathchen.mybatisplus.util.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;
import io.github.heathchen.mybatisplus.util.utils.MyBatisPlusUtil;

import java.util.List;
import java.util.function.Consumer;

/**
 * 查询配置建造器
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/04
 */
public class QueryBuilder<E, T> {

    /**
     * QueryWrapper消费者
     */
    Consumer<QueryWrapper<?>> consumer;
    /**
     * 查询参数
     */
    private E e;
    /**
     * 匹配模式 默认全部匹配模式
     */
    private MatchMode matchMode = MatchMode.ALL_MATCH_MODE;
    /**
     * 返回结果类型
     */
    private Class<T> clazz;
    /**
     * 返回结果类型
     */
    private String[] groupIds;
    /**
     * 忽略参数名
     */
    private String[] ignoreParams;

    public QueryBuilder() {
    }

    public E getE() {
        return e;
    }

    public QueryBuilder setE(E e) {
        this.e = e;
        return this;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public QueryBuilder setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
        return this;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public QueryBuilder setClazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String[] getGroupIds() {
        return groupIds;
    }

    public QueryBuilder setGroupIds(String... groupIds) {
        this.groupIds = groupIds;
        return this;
    }

    public Consumer<QueryWrapper<?>> getConsumer() {
        return consumer;
    }

    public QueryBuilder setConsumer(Consumer<QueryWrapper<?>> consumer) {
        this.consumer = consumer;
        return this;
    }

    public String[] getIgnoreParams() {
        return ignoreParams;
    }

    public QueryBuilder setIgnoreParams(String... ignoreParams) {
        this.ignoreParams = ignoreParams;
        return this;
    }


    public <T> List<T> doQuery() {
        return (List<T>) MyBatisPlusUtil.queryByReflect(this);
    }

}

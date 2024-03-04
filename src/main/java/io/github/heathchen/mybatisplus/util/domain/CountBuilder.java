package io.github.heathchen.mybatisplus.util.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.enums.MatchMode;
import io.github.heathchen.mybatisplus.util.utils.MyBatisPlusUtil;

import java.util.function.Consumer;

/**
 * 计数查询建造者
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/04
 */
public class CountBuilder<E> {

    /**
     * QueryWrapper消费者
     */
    Consumer<QueryWrapper<E>> consumer;
    /**
     * 查询参数
     */
    private E e;
    /**
     * 匹配模式 默认全部匹配模式
     */
    private MatchMode matchMode = MatchMode.ALL_MATCH_MODE;
    /**
     * 忽略属性
     */
    private String[] ignoreParams;

    /**
     * 返回结果类型
     */
    private String[] groupIds;

    public E getE() {
        return e;
    }

    public CountBuilder setE(E e) {
        this.e = e;
        return this;
    }

    public Consumer<QueryWrapper<E>> getConsumer() {
        return consumer;
    }

    public CountBuilder setConsumer(Consumer<QueryWrapper<E>> consumer) {
        this.consumer = consumer;
        return this;
    }

    public String[] getIgnoreParams() {
        return ignoreParams;
    }

    public CountBuilder setIgnoreParams(String[] ignoreParams) {
        this.ignoreParams = ignoreParams;
        return this;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public CountBuilder setMatchMode(MatchMode matchMode) {
        this.matchMode = matchMode;
        return this;
    }

    public String[] getGroupIds() {
        return groupIds;
    }

    public CountBuilder setGroupIds(String[] groupIds) {
        this.groupIds = groupIds;
        return this;
    }

    public Integer doCountByReflect() {
        return MyBatisPlusUtil.countByReflect(this);
    }
}

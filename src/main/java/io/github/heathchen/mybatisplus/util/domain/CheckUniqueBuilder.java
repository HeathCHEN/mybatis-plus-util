package io.github.heathchen.mybatisplus.util.domain;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.heathchen.mybatisplus.util.utils.MyBatisPlusUtil;

import java.util.function.Consumer;

/**
 * 检查唯一查询构造器
 *
 * @author HeathCHEN
 * @version 1.0
 * @since 2024/03/04
 */
public class CheckUniqueBuilder<E> {

    /**
     * QueryWrapper消费者
     */
    Consumer<QueryWrapper<?>> consumer;
    /**
     * 查询参数
     */
    private E e;
    /**
     * 最多个数
     */
    private Integer limit;
    /**
     * 最多个数
     */
    private String groupIds;


    public CheckUniqueBuilder() {
    }

    public E getE() {
        return e;
    }

    public CheckUniqueBuilder setE(E e) {
        this.e = e;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public CheckUniqueBuilder setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Consumer<QueryWrapper<?>> getConsumer() {
        return consumer;
    }

    public CheckUniqueBuilder setConsumer(Consumer<QueryWrapper<?>> consumer) {
        this.consumer = consumer;
        return this;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public CheckUniqueBuilder setGroupIds(String groupIds) {
        this.groupIds = groupIds;
        return this;
    }


    public Boolean doCheckUniqueQueryWrapper() {
        return MyBatisPlusUtil.checkUniqueByReflect(this);
    }
}

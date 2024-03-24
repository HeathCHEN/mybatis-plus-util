package io.github.heathchen.mybatisplus.util.factory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface QueryWrapperFactory {

    public QueryWrapper<?> getQueryWrapper();


}

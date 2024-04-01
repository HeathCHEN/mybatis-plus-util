package io.github.heathchen.mybatisplus.util.context;

import io.github.heathchen.mybatisplus.util.factory.DefaultQueryWrapperFactory;

public class GenericQueryContext {

    private DefaultQueryWrapperFactory queryWrapperFactory;

    public GenericQueryContext() {
        this.queryWrapperFactory = new DefaultQueryWrapperFactory();
    }
}

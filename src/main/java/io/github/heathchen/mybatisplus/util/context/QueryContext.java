package io.github.heathchen.mybatisplus.util.context;

import io.github.heathchen.mybatisplus.util.config.MyBatisPlusUtilConfig;
import io.github.heathchen.mybatisplus.util.definiton.AnnotationReader;
import io.github.heathchen.mybatisplus.util.definiton.EntityDefinition;
import io.github.heathchen.mybatisplus.util.definiton.EntityDefinitionRegistry;
import io.github.heathchen.mybatisplus.util.factory.DefaultQueryWrapperFactory;

public class QueryContext extends GenericQueryContext implements EntityDefinitionRegistry {

    private AnnotationReader annotationReader;
    private MyBatisPlusUtilConfig config;

    private DefaultQueryWrapperFactory factory;

    public QueryContext() {
        this.annotationReader = new AnnotationReader(this);
        this.factory = new DefaultQueryWrapperFactory();
    }

    public QueryContext(MyBatisPlusUtilConfig myBatisPlusUtilConfig) {
        this.config = myBatisPlusUtilConfig;
    }


    @Override
    public void registerBeanDefinition(String entityName, EntityDefinition entityDefinition) {

    }
}

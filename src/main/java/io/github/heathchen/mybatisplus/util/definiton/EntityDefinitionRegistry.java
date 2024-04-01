package io.github.heathchen.mybatisplus.util.definiton;

public interface EntityDefinitionRegistry {


    void registerBeanDefinition(String entityName,
                                EntityDefinition entityDefinition);

}

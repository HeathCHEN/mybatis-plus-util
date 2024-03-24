package io.github.heathchen.mybatisplus.util.definiton;

public class EntityDefinitionUtil {


    public static void registerEntityDefinition(EntityDefinition entityDefinition, EntityDefinitionRegistry registry) {
        String name = ((EntityGernericDefinition) entityDefinition).getParamClass().getSimpleName();
        registry.registerBeanDefinition(name, entityDefinition);

    }
}

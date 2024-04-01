package io.github.heathchen.mybatisplus.util.definiton;

public class AnnotationReader {

    private EntityDefinitionRegistry registry;

    public AnnotationReader(EntityDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?> paramClass){
        EntityGenericDefinition<Object, Object> entityDefinition = new EntityGenericDefinition<>();





        EntityDefinitionUtil.registerEntityDefinition(entityDefinition,this.registry);
    }
}

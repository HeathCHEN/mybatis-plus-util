package io.github.heathchen.mybatisplus.util.definiton;

public class AnnotationReader {

    private EntityDefinitionRegistry registry;

    public AnnotationReader(EntityDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void register(Class<?> paramClass){
        EntityGernericDefinition<Object, Object> entityDefinition = new EntityGernericDefinition<>();





        EntityDefinitionUtil.registerEntityDefinition(entityDefinition,this.registry);
    }
}

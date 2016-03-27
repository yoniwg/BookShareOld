package hgyw.com.bookshare.dataAccess;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.reflection.Property;
import hgyw.com.bookshare.entities.reflection.ReflectionProperties;

/**
 * Created by Yoni on 3/17/2016.
 */
class ListsCrudImpl implements Crud {

    protected ListsCrudImpl() {}

    private Map<Class<? extends Entity>, Long> entitiesIdMap = new HashMap<>();
    private Map<Class<? extends Entity>, List<Entity>> entitiesMap = new HashMap<>();

    @Override
    public void createEntity(Entity item) {
        List<Entity> entityList = getListOrCreate(item.getClass());

        if (item.getId() != 0) {
            throw new IllegalArgumentException("ID must be 0");
        }
        generateNewId(item);
        entityList.add(item.clone());
    }

    private List<Entity> getListOrCreate(Class<? extends Entity> clazz) {
        List<Entity> entityList = entitiesMap.get(clazz);
        if (entityList == null) {
            entitiesMap.put(clazz, entityList = new ArrayList<Entity>());
            entitiesIdMap.put(clazz, 0L);
        }
        return entityList;
    }

    private void generateNewId(Entity entity) {
        long entityId = entitiesIdMap.get(entity.getClass()) + 1;
        entity.setId(entityId);
        entitiesIdMap.put(entity.getClass(), entityId);
    }

    @Override
    public void updateEntity(Entity item) {
        deleteEntity(item);
        getListOrCreate(item.getClass()).add(item);
    }

    @Override
    public void deleteEntity(Entity item) {
        List<Entity> entityList = entitiesMap.get(item.getClass());
        if (entityList == null || !entityList.remove(item)) {
            throw new NoSuchElementException("No such entity with such ID");
        }
    }

    public <T extends Entity> Stream<T> streamAll(Class<? extends T> entityType) {
        List<Entity> entityList = entitiesMap.get(entityType);
        if (entityList != null) return Stream.of(entityList).map(e -> (T) e);
        return Stream.empty();
    }

    @Override
    public <T extends Entity> T retrieveEntity(Class<? extends T> entityClass, long id) {
        return (T) cloneNestedEntities(retrieveOriginalEntity(entityClass, id));
    }


    protected <T extends Entity> Collection<T> findEntityByProperty(Property p, Object propertyValue) {
        return this.streamAll((Class<? extends T>) p.getReflectedClass())
                .filter(e -> {
                    try {
                        return p.get(e).equals(propertyValue);
                    } catch (InvocationTargetException ex) { return false; }
                }).collect(Collectors.toList());
    }

    @Override
    public <T extends Entity> T retrieveEntity(T item) {
        return retrieveEntity((Class<? extends T>) item.getClass(), item.getId());
    }

    private <T extends Entity> T retrieveOriginalEntity(Class<? extends T> entityClass, long id) {
        Optional<T> entity = streamAll(entityClass)
                .filter(e -> e.getId() == id)
                .findFirst();
        if (entity.isPresent()) return entity.get();
        throw new NoSuchElementException("No such entity with such ID");
    }

    public Entity cloneNestedEntities(Entity entity) {
        entity = entity.clone();
        for (Property p : ReflectionProperties.getPropertiesMap(entity.getClass()).values()) {
            if (p.canWrite() && Entity.class.isAssignableFrom(p.getPropertyClass())) {
                try {
                    Entity currentNestedEntity = (Entity) p.get(entity);
                    Class<? extends Entity> nestedEntityClass = (Class<? extends Entity>) p.getPropertyClass();
                    Entity originalNestedEntity = retrieveOriginalEntity(nestedEntityClass, currentNestedEntity.getId());
                    p.set(entity, originalNestedEntity);
                } catch (InvocationTargetException e) {
                    throw new InternalError(); // unreached code
                }
            }
        }
        return entity;
    }

}

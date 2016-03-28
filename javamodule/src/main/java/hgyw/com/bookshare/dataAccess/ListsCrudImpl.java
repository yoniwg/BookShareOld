package hgyw.com.bookshare.dataAccess;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.reflection.Property;
import hgyw.com.bookshare.entities.reflection.PropertiesReflection;

/**
 * Created by Yoni on 3/17/2016.
 */
class ListsCrudImpl implements Crud {

    protected ListsCrudImpl() {}

    private Map<Class<? extends Entity>, Long> entitiesIdMap = new HashMap<>();
    private Map<Class<? extends Entity>, List<Entity>> entitiesMap = new HashMap<>();

    @Override
    public void createEntity(Entity item) {
        item.setDeleted(false);
        List<Entity> entityList = getListOrCreate(item.getClass());

        if (item.getId() != 0) {
            throw new IllegalArgumentException("ID must be 0");
        }
        generateNewId(item);
        entityList.add(deepCloneByDatabase(item));
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
        item.setDeleted(false);
        List<Entity> entityList = entitiesMap.get(item.getClass());
        if (entityList == null || !entityList.remove(item)) throw createNoSuchEntityException(item.getClass(), item.getId());
        entityList.add(deepCloneByDatabase(item));
    }

    @Override
    public void deleteEntity(Entity item) {
        List<Entity> entityList = entitiesMap.get(item.getClass());
        if (entityList == null) throw createNoSuchEntityException(item.getClass(), item.getId());
        if (hasReferenceTo(item.getClass(), item.getId())) {
            if (entityList == null || !entityList.remove(item)) throw createNoSuchEntityException(item.getClass(), item.getId());
            item.setDeleted(true);
            entityList.add(deepCloneByDatabase(item));
        }
        else {
            if (!entityList.remove(item))  throw createNoSuchEntityException(item.getClass(), item.getId());
        }
    }

    private boolean hasReferenceTo(Class<? extends Entity> entityClass, long id) {
        Entity item = retrieveOriginalEntity(entityClass, id); // throw if not found
        return true; // TODO optimization/implementation
    }

    public <T extends Entity> Stream<T> streamAll(Class<? extends T> entityType) {
        List<Entity> entityList = entitiesMap.get(entityType);
        if (entityList != null) return Stream.of(entityList).map(e -> (T) e.deepClone());
        return Stream.empty();
    }

    @Override
    public <T extends Entity> T retrieveEntity(Class<? extends T> entityClass, long id) {
        return (T) retrieveOriginalEntity(entityClass, id).deepClone();
    }


    @Override
    public <T extends Entity> T retrieveEntity(T item) {
        return retrieveEntity((Class<? extends T>) item.getClass(), item.getId());
    }

    private <T extends Entity> T retrieveOriginalEntity(Class<? extends T> entityClass, long id) {
        return streamAll(entityClass)
                .filter(e -> e.getId() == id)
                .findFirst().orElseThrow(() -> createNoSuchEntityException(entityClass, id));

    }

    private static NoSuchElementException createNoSuchEntityException(Class<?> entityClass, long id) {
        return new NoSuchElementException("No entity " + entityClass.getSimpleName() + " with ID " + id);
    }

    public Entity deepCloneByDatabase(Entity entity) {
        entity = entity.clone();
        for (Property p : PropertiesReflection.getPropertiesMap(entity.getClass()).values()) {
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

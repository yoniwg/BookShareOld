package hgyw.com.bookshare.dataAccess;

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
        Entity retrievedItem = retrieveNonDeletedOriginalEntity(item);
        assignAndSetReferencesByDatabase(retrievedItem, item);
    }

    @Override
    public void deleteEntity(Entity item) {
        Entity retrievedItem = retrieveNonDeletedOriginalEntity(item);
        retrievedItem.setDeleted(true);
    }

    private Entity retrieveNonDeletedOriginalEntity(Entity item) {
        Entity retrievedItem = retrieveOriginalEntity(item.getClass(), item.getId());
        if (retrievedItem.isDeleted()) throw createNoSuchEntityException(item.getClass(), item.getId());
        return retrievedItem;
    }

    public <T extends Entity> Stream<T> streamAll(Class<? extends T> entityType) {
        List<Entity> entityList = entitiesMap.get(entityType);
        if (entityList != null) return Stream.of(entityList).map(e ->  (T) deepClone(e));
        return Stream.empty();
    }

    @Override
    public <T extends Entity> T retrieveEntity(Class<? extends T> entityClass, long id) {
        return (T) deepClone(retrieveOriginalEntity(entityClass, id));
    }


    @Override
    public <T extends Entity> T retrieveEntity(T item) {
        return retrieveEntity((Class<? extends T>) item.getClass(), item.getId());
    }

    private <T extends Entity> T retrieveOriginalEntity(Class<? extends T> entityClass, long id) {
        List<Entity> entityList = entitiesMap.get(entityClass);
        if (entityList == null) throw createNoSuchEntityException(entityClass, id);
        Entity item =  Stream.of(entityList)
                .filter(e -> e.getId() == id)
                .findFirst().orElseThrow(() -> createNoSuchEntityException(entityClass, id));
        return (T) item;

    }

    private static NoSuchElementException createNoSuchEntityException(Class<?> entityClass, long id) {
        return new NoSuchElementException("No entity " + entityClass.getSimpleName() + " with ID " + id);
    }

    private void assignAndSetReferencesByDatabase(Entity target, Entity source) {
        for (Property p : PropertiesReflection.getPropertiesMap(target.getClass()).values()) {
            if (!p.canWrite()) continue;
            try {
                Object value = p.get(source);
                if (value instanceof Entity) {
                    Entity nestedEntity = (Entity) value;
                    value = retrieveOriginalEntity(nestedEntity.getClass(), nestedEntity.getId());
                }
                p.set(target, value);
            } catch (InvocationTargetException e) {
                // do nothing - unreached code
            }
        }
    }

    public Entity deepCloneByDatabase(Entity entity) {
        entity = entity.clone();
        for (Property p : PropertiesReflection.getPropertiesMap(entity.getClass()).values()) {
            if (p.canWrite() && Entity.class.isAssignableFrom(p.getPropertyClass())) {
                try {
                    Entity currentNestedEntity = (Entity) p.get(entity);
                    Entity originalNestedEntity = retrieveOriginalEntity(currentNestedEntity.getClass(), currentNestedEntity.getId());
                    p.set(entity, originalNestedEntity);
                } catch (InvocationTargetException e) {
                    throw new InternalError(); // unreached code
                }
            }
        }
        return entity;
    }

    /**
     * Do deep clone on this entity - clone all nested entities, and all nested collections
     * contains entities.
     */
    public <T extends Entity> T deepClone(T item) {
        try {
            Entity newItem = item.clone();
            // deep cloning
            for (Property p : PropertiesReflection.getPropertiesMap(getClass()).values()) {
                if (p.canWrite()) {
                    // Clone entity references in this item
                    if (Entity.class.isAssignableFrom(p.getPropertyClass())) {
                        Entity value = (Entity) p.get(newItem);
                        if (value != null) value = value.clone();
                        p.set(newItem, value);
                    }
                    // Clone entity references in list in this item
                    else if (Collection.class.isAssignableFrom(p.getPropertyClass())) {
                        Collection<?> value = (Collection) p.get(newItem);
                        Collection<Object> newCollection = new ArrayList<>(value);
                        for (Object o : value) newCollection.add(o instanceof Entity ?((Entity)o).clone() : o);
                        p.set(newItem, newCollection);
                    }
                }
            }
            return (T) newItem;
        }
        catch (InvocationTargetException e) {
            throw new InternalError("Unreached code", e); // Unreached code
        }
    }

}

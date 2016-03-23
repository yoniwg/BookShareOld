package hgyw.com.bookshare.crud;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public enum ListsCrudImpl implements Crud {

    INSTANCE;

    private Map<Class<? extends Entity>, Long> entitiesIdMap = new HashMap<>();
    private Map<Class<? extends Entity>, List<Entity>> entitiesMap = new HashMap<>();

    @Override
    public void createEntity(Entity item) {
        List<Entity> entityList = getListOrCreate(item.getClass());

        if (item.getId() != 0) {
            throw new IllegalArgumentException("ID must be 0");
        }
        generateNewId(item);
        entityList.add((Entity) item.clone());
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

    @Override
    public <T extends Entity> Stream<T> streamAll(Class<T> entityType) {
        List<Entity> entityList = entitiesMap.get(entityType);
        if (entityList != null) return Stream.of(entityList).map(e -> (T) e);
        return Stream.empty();
    }

    @Override
    public <T extends Entity> T retrieveEntity(Class<T> entityClass, long id) {
        Optional<T> entity = streamAll(entityClass)
                .filter(e -> e.getId() == id)
                .findFirst();
        if (entity.isPresent()) return (T) entity.get().clone();
        throw new NoSuchElementException("No such entity with such ID");
    }

}
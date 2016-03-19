package hgyw.com.bookshare.backend;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public enum ListsBackendImpl implements Backend {

    INSTANCE;

    private Map<Class<? extends Entity>, Long> entitiesIdMap = new HashMap<>();
    private Map<Class<? extends Entity>, List<Entity>> entitiesMap = new HashMap<>();

    @Override
    public void createEntity(Entity entity){

        List<Entity> entityList = entitiesMap.get(entity.getClass());
        if (entityList == null){
            entitiesMap.put(entity.getClass(), entityList = new ArrayList <Entity>());
            entitiesIdMap.put(entity.getClass(), 0L);
        }

        if (entity.getId() != 0) {
            throw new IllegalArgumentException("ID must be 0");
        }

        entityList.add(generateNewEntity(entity));
    }

    private Entity generateNewEntity(Entity entity) {
        Entity newEntity = (Entity)entity.clone();
        long entityId = entitiesIdMap.get(newEntity.getClass()) + 1;
        newEntity.setId(entityId);
        entitiesIdMap.put(newEntity.getClass(), entityId);
        return newEntity;
    }

    @Override
    public void updateEntity(Entity entity) {
        deleteEntity(entity);
        entitiesMap.get(entity.getClass()).add(entity);
    }

    @Override
    public void deleteEntity(Entity entity) {
        List<Entity> entityList = entitiesMap.get(entity.getClass());
        entityList.remove(entity);
    }

    @Override
    public <T extends Entity> Stream<T> getStream(Class<T> entityType) {
        return Stream.of(entitiesMap.get(entityType)).map(e -> (T) e);
    }

}

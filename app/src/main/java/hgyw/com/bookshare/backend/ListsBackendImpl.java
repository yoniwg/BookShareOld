package hgyw.com.bookshare.backend;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public enum ListsBackendImpl implements Backend {

    INSTANCE;

    private Map<Class<? extends Entity>,List<Entity>> entitiesMap;

    @Override
    public void createEntity(Entity entity) {

        List<Entity> entityList = entitiesMap.get(entity.getClass());

        if (entityList == null){
            entitiesMap.put(entity.getClass(), new ArrayList<Entity>());
            createEntity(entity);
        }

        entityList.add(entity.getClass().cast(entity.clone()));
    }

    @Override
    public void updateEntity(Entity entity) {
        deleteEntity(entity);
        entitiesMap.get(entity.getClass()).add(entity);
    }

    @Override
    public void deleteEntity(Entity entity) {
        List<Entity> entityList = entitiesMap.get(entity.getClass());
        entityList.remove(getLocationById(entity.getId(), entityList));
    }

    @Override
    public Stream<Entity> getStream(Class<? extends Entity> entityType) {
        return Stream.of(entitiesMap.get(entityType));
    }

    private static int getLocationById(long entityId, List<Entity> entityList) {
        int location = 0;
        for (Entity e : entityList) {
            location++;
            if (e.getId() == entityId){
                break;
            }
        }
        return location;
    }
}

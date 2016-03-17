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
public class ListsBackendImpl implements Backend {

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
    public Entity readEntity(Class<? extends Entity> entityType, final long entityId) {
        return Stream.of(entitiesMap.get(entityType)).filter(new Predicate<Entity>() {
            @Override
            public boolean test(Entity value) {
                return value.getId() == entityId;
            }
        }).findFirst().get();
    }

    @Override
    public void updateEntity(Entity entity) {

    }

    @Override
    public void deleteEntity(Entity entity) {

    }

    @Override
    public Stream<Entity> getStreamOf(Class<? extends Entity> entityType) {
        return null;
    }
}

package hgyw.com.bookshare.backend;

import com.annimon.stream.Stream;

import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public class ListsBackendImpl implements Backend {

    private Map<Class<? extends Entity>,List<Entity>> EntitiesMap;

    @Override
    public void createEntity(Entity entity) {

    }

    @Override
    public Entity readEntity(Entity entity) {
        return null;
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

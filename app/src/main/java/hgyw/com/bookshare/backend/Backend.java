package hgyw.com.bookshare.backend;

import com.annimon.stream.Stream;

import java.util.List;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public interface Backend {

    void createEntity(Entity entity);
    Entity readEntity(Class<? extends Entity> entityType, long entityId);
    void updateEntity(Entity entity);
    void deleteEntity(Entity entity);

    Stream<Entity> getStreamOf(Class<? extends Entity> entityType);
}

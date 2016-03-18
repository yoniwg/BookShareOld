package hgyw.com.bookshare.backend;

import com.annimon.stream.Stream;

import java.util.List;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public interface Backend {

    void createEntity(Entity entity);
    void updateEntity(Entity entity);
    void deleteEntity(Entity entity);

    Stream<Entity> getStream(Class<? extends Entity> entityType);
}

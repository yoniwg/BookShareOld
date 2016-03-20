package hgyw.com.bookshare.backend;

import com.annimon.stream.Stream;

import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
public interface Crud {

    void createEntity(Entity entity);
    void updateEntity(Entity entity);
    void deleteEntity(Entity entity);

    <T extends Entity> Stream<T> getStream(Class<T> entityType);

    <T extends Entity> T retrieveEntity(Class<T> entityClass, long id);
}

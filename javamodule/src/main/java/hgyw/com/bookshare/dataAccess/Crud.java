package hgyw.com.bookshare.dataAccess;

import com.annimon.stream.Stream;

import java.util.Collection;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by Yoni on 3/17/2016.
 */
interface Crud {

    /**
     * Add new item to the data base. the id should be 0, it will be generated by the database.
     * The id in the entity will be updated to the a new id that generated by the database.
     * @param item The item to add
     * @throws IllegalArgumentException if item id is not 0.
     */
    void createEntity(Entity item);

    /**
     * Update item on the data base.
     * @param item The item to update
     * @throws java.util.NoSuchElementException if item of this entity with such id has not found.
     */
    void updateEntity(Entity item);

    /**
     * Delete an item from database. the method treats only to item type and id value.
     * @param item Item to delete
     * @throws java.util.NoSuchElementException if item of this entity with such id has not found.
     */
    void deleteEntity(Entity item);

    /**
     * Retrieve an entity from database.
     * @param entityClass The entity class
     * @param id Item id
     * @param <T> The type of entity
     * @return Entity item of class T
     * @throws java.util.NoSuchElementException If database doesn't contain item of this entity and id.
     */
    <T extends Entity> T retrieveEntity(Class<? extends T> entityClass, long id);

    /**
     * Equivalent to retrieveEntity(item.getClass(), item.getId()).
     * @param item The item to retrieve
     * @param <T> The type of entity
     * @return Entity item of class T
     * @throws java.util.NoSuchElementException If database doesn't contain item of this entity and id.
     */
    <T extends Entity> T retrieveEntity(T item);

    /**
     * Stream all items of specified entity.
     * @param <T> The type of entity
     * @return Stream of all entities.
     */
    <T extends Entity> Stream<T> streamAll(Class<? extends T> entityType);
}

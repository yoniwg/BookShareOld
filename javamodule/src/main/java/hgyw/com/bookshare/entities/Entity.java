package hgyw.com.bookshare.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import hgyw.com.bookshare.entities.reflection.Property;
import hgyw.com.bookshare.entities.reflection.ReflectionProperties;
import hgyw.com.bookshare.entities.reflection.ReflectionToString;

/**
 * Created by Yoni on 3/15/2016.
 */
public abstract class Entity implements Cloneable{
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Entity clone() {
        try {
            Entity newEntity = (Entity) super.clone();
            // deep cloning
            for (Property p : ReflectionProperties.getProperties(getClass())) {
                if (p.canWrite()) {
                    // Clone entity references in this item
                    if (Entity.class.isAssignableFrom(p.getPropertyClass())) {
                        Entity value = (Entity) p.get(newEntity);
                        if (value != null) value = value.clone();
                        p.set(newEntity, value);
                    }
                    // Clone entity references in list in this item
                    else if (Collection.class.isAssignableFrom(p.getPropertyClass())) {
                        Collection<?> value = (Collection) p.get(newEntity);
                        Collection<Object> newCollection = new ArrayList<>(value);
                        for (Object o : value) newCollection.add(o instanceof Entity ?((Entity)o).clone() : o);
                        p.set(newEntity, newCollection);
                    }

                }
            }
            return newEntity;
        }
        catch (IllegalAccessException | CloneNotSupportedException | InvocationTargetException e) {
            throw new InternalError("Unreached code", e); // Unreached code - isAccessible required below
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        Entity other = (Entity) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public String toString() {
        return ReflectionToString.publicGettersToString(this);
    }
}

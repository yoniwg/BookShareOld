package hgyw.com.bookshare.entities;

import java.lang.reflect.InvocationTargetException;

import hgyw.com.bookshare.entities.reflection.ReflectionProperties;

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
            // Clone entity references in this item:
            for (hgyw.com.bookshare.entities.reflection.ReflectionProperties.Property p : ReflectionProperties.getProperties(getClass())) {
                if (p.canWriten() && Entity.class.isAssignableFrom(p.getPropertyClass())) {
                    Entity value = (Entity) p.get(newEntity);
                    if (value != null) value = value.clone();
                    p.set(newEntity, value);
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
        return hgyw.com.bookshare.entities.reflection.Reflection.publicGettersToString(this);
    }
}

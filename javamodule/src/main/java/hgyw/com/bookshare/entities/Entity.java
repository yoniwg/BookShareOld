package hgyw.com.bookshare.entities;

import java.util.Map;

import hgyw.com.bookshare.entities.reflection.Property;
import hgyw.com.bookshare.entities.reflection.PropertiesReflection;

/**
 * Created by Yoni on 3/15/2016.
 */
public abstract class Entity implements Cloneable{
    private long id;
    private boolean isDeleted;

    /**
     * Get the id.
     * @return long value of id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id new long value of id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Do shallow copy on this entity
     */
    @Override
    public Entity clone() {
        try {
            return (Entity) super.clone();
        }
        catch (CloneNotSupportedException  e) {
            throw new InternalError();
        }
    }


    /**
     * Returns true if and only id the object o is of the same class of this, and the id's are equals.
     * @param o the object to equal.
     * @return boolean value indicates whether the objects are equals.
     */
    @Override
    public final boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) return false;
        Entity other = (Entity) o;
        return this.getClass() == other.getClass() && this.getId() == other.getId();
    }

    /**
     * Hash code method.
     * @return hash code.
     */
    @Override
    public final int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    /**
     * Prints public getters of this object.
     * @return String described the this object.
     */
    @Override
    public String toString() {
        Map<String, Property> map = PropertiesReflection.getPropertiesMap(this.getClass());
        Property idProperty = map.remove("id");
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Property> e : map.entrySet()) {
            Object value = e.getValue().get(this);
            if (value instanceof String) value = "'" + value + "'";
            if (value instanceof Entity)
                value = "(" + ((Entity) value).shortDescription() + ")";
            if (str.length() != 0) str.append(", ");
            str.append(e.getKey()).append("=").append(value);
        }
        return getClass().getSimpleName() + "(id=" + idProperty.get(this) + "){" + str + "}";
    }

    public String shortDescription() {
        return "id=" + getId();
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

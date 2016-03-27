package hgyw.com.bookshare.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import hgyw.com.bookshare.entities.reflection.Property;
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
            return (Entity) super.clone();
        }
        catch (CloneNotSupportedException /*| InvocationTargetException*/ e) {
            throw new InternalError("Unreached code", e); // Unreached code //- isAccessible required below
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
        try {
            Map<String, Property> map = ReflectionProperties.getPropertiesMap(this.getClass());
            Property idProperty =  map.remove("id");
            StringBuilder str = new StringBuilder();
            for (Map.Entry<String, Property> e : map.entrySet()) {
                Object value = e.getValue().get(this);
                if (value instanceof Entity) value = "(id=" +  ((Entity) value).getId() +")";
                if (str.length() != 0) str.append(", ");
                str.append(e.getKey()).append("=").append(value);
            }
            return getClass().getSimpleName() + "(id=" + idProperty.get(this) + "){" + str + "}";
        } catch (InvocationTargetException e) {
            return this.getClass().getSimpleName() + "{Error in reflect this object.}";
        }

    }
}

package hgyw.com.bookshare.entities;

import com.annimon.stream.Stream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

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
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            //Unreached code
            throw new InternalError("Unreached code");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Entity)) return false;
        Entity other = (Entity) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {id=" + getId() + "}";
    }
}

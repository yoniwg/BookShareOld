package hgyw.com.bookshare.entities.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by haim7 on 23/03/2016.
 */
public interface Property {
    // throw IllegalArgumentException!
    void set(Object o, Object value) throws InvocationTargetException;
    // throw IllegalArgumentException!
    Object get(Object o) throws InvocationTargetException;
    String getName();
    boolean canWrite();
    Class<?> getPropertyClass();
    Class<?> getReflectedClass();
}

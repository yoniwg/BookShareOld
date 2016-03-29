package hgyw.com.bookshare.entities.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by haim7 on 23/03/2016.
 */
public interface Property {
    // throws IllegalArgumentException, UnsupportedOperationException
    void set(Object o, Object value);
    // throws IllegalArgumentException
    Object get(Object o);
    String getName();
    boolean canWrite();
    Class<?> getPropertyClass();
    Class<?> getReflectedClass();
}

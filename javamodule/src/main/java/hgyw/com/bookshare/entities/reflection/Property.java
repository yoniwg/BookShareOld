package hgyw.com.bookshare.entities.reflection;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by haim7 on 23/03/2016.
 */
public interface Property {
    void set(Object o, Object value) throws InvocationTargetException, IllegalAccessException;
    Object get(Object o) throws InvocationTargetException, IllegalAccessException;
    String getName();
    boolean canWrite();
    Class<?> getPropertyClass();
    Class<?> getReflectedClass();
}

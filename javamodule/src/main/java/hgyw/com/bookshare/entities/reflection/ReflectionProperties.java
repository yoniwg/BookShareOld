package hgyw.com.bookshare.entities.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by haim7 on 23/03/2016.
 */
public class ReflectionProperties extends Reflection {


    public static Collection<Property> getProperties(Class<?> clazz) {
        return getPropertiesMap(clazz).values();
    }

    public static Property getProperty(Class<?> clazz, String propertyName) {
        return getPropertiesMap(clazz).get(propertyName);
    }

    private static Map<String, Property> getPropertiesMap(Class<?> clazz) {
        Map<String, Property> map = new HashMap<>();
        for (Method m : clazz.getMethods())
            if (isGetter(m)) {
                Property p = new ReflectedProperty(propertyNameFromGetter(m), m, getSetterByGetter(m));
                map.put(p.getName(), p);
            }
        return map;
    }

    private static Method getSetterByGetter(Method m) {
        try {
            return m.getDeclaringClass().getMethod("set" + m.getName().substring(3), m.getReturnType());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static <T extends Entity> Property getPropertyOfType(Class<T> clazz, Class<? extends Entity> propertyType) {
        for (Property p : getPropertiesMap(clazz).values())
            if (p.getPropertyClass() == propertyType)
                return p;
        throw new NoSuchElementException("No property of type " + propertyType.getSimpleName() + " reflected in class " + clazz.getSimpleName());
    }

    private static class ReflectedProperty implements Property {
        private final String name;
        private final Method setter;
        private final Method getter;

        public ReflectedProperty(String name, Method getter, Method setter) {
            Objects.requireNonNull(name);
            if (!Modifier.isPublic(getter.getModifiers()) || setter != null && !Modifier.isPublic(setter.getModifiers())) {
                throw new IllegalArgumentException("The getter and setter should be public.");
            }
            this.name = name; this.setter = setter; this.getter = getter;
        }

        @Override
        public void set(Object o, Object value) throws InvocationTargetException {
            if (setter == null) throw new UnsupportedOperationException("The Property is read only.");
            try {
                setter.invoke(o, value);
            } catch (IllegalAccessException e) {
                throw new InternalError(); // unreached code because the method is public
            }
        }

        @Override
        public Object get(Object o) throws InvocationTargetException {
            try {
                return getter.invoke(o);
            } catch (IllegalAccessException e) {
                throw new InternalError(); // unreached code because the method is public
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean canWrite() {
            return setter != null;
        }

        @Override
        public Class<?> getPropertyClass() {
            return getter.getReturnType();
        }

        @Override
        public Class<?> getReflectedClass() {
            return getter.getDeclaringClass();
        }
    }


}

package hgyw.com.bookshare.entities.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    private static class ReflectedProperty implements Property {
        private final String name;
        private final Method setter;
        private final Method getter;

        public ReflectedProperty(String name, Method getter, Method setter) {
            this.name = name; this.setter = setter; this.getter = getter;
        }

        @Override
        public void set(Object o, Object value) throws InvocationTargetException, IllegalAccessException {
            if (setter == null) throw new UnsupportedOperationException("The Property is read only.");
            setter.invoke(o, value);
        }

        @Override
        public Object get(Object o) throws InvocationTargetException, IllegalAccessException {
            return getter.invoke(o);
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

package hgyw.com.bookshare.entities.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import hgyw.com.bookshare.entities.Entity;

/**
 * Created by haim7 on 23/03/2016.
 */
public class PropertiesReflection {

    /**
     * Returns the method name without prefix of "is" for boolean getter, or "get" for boolean and other getters.
     * If the method name is not match the getter og java binding then it will return null.
     */
    private static String removePrefixFromGetter(Method method) {
        String name = method.getName();
        boolean methodReturnsBoolean = method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class;
        if (methodReturnsBoolean && name.startsWith("is") && Character.isUpperCase(name.charAt(2))) {
            return name.substring(2);
        }
        if (name.startsWith("get") && Character.isUpperCase(name.charAt(3))) {
            return name.substring(3);
        }
        return null;
    }

    /**
     * Returns setter that its method name is "set"+setterNameWithoutPrefix, with the propertyType, in class declaringClass.
     * @return the setter or nul if not fount.
     */
    private static Method getSetter(Class<?> declaringClass, String setterNameWithoutPrefix, Class<?> propertyType) {
        try {
            return declaringClass.getMethod("set" + setterNameWithoutPrefix, propertyType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * Creates of all properties of class clazz, where key is the property name.
     * @param clazz
     */
    public static Map<String, Property> getPropertiesMap(Class<?> clazz) {

        Map<String, Property> map = new HashMap<>();
        for (Method m : clazz.getMethods()) {
            String getterNameWithoutPrefix = removePrefixFromGetter(m);
            if (getterNameWithoutPrefix != null && !getterNameWithoutPrefix.equals("Class")) {
                String propertyName = Character.toLowerCase(getterNameWithoutPrefix.charAt(0)) + getterNameWithoutPrefix.substring(1);
                Method setter = getSetter(m.getDeclaringClass(), getterNameWithoutPrefix, m.getReturnType());
                Property p = new ReflectedProperty(propertyName, m, setter);
                map.put(p.getName(), p);
            }
        }
        return map;
    }

    /**
     * Class represents property, that accepts getter and setter of reflection, and property name.
     * The setter can be null and then the property will be read-only.
     */
    private static class ReflectedProperty implements Property {
        private final String name;
        private final Method setter;
        private final Method getter;

        public ReflectedProperty(String name, Method getter, Method setter) {
            if (!Modifier.isPublic(getter.getModifiers()) || setter != null && !Modifier.isPublic(setter.getModifiers())) {
                throw new IllegalArgumentException("The getter and setter should be public.");
            }
            if (getter.getParameterTypes().length != 0) {
                throw new IllegalArgumentException("The getter should not have parameters.");
            }
            if (setter != null) {
                if (setter.getParameterTypes().length != 1 || setter.getParameterTypes()[0] != getter.getReturnType()) {
                    throw new IllegalArgumentException("The setter should have only one parameter with type equals to returned type of getter.");
                }
            }
            this.name = Objects.requireNonNull(name); this.setter = setter; this.getter = getter;
        }

        @Override
        public void set(Object o, Object value) {
            if (setter == null) throw new UnsupportedOperationException("The Property is read only.");
            try {
                setter.invoke(o, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InternalError(e); // unreached code because the method is public
            }
        }

        @Override
        public Object get(Object o) {
            try {
                return getter.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new InternalError(e); // unreached code because the method is public
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

package hgyw.com.bookshare.entities.reflection;

import com.annimon.stream.Stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import hgyw.com.bookshare.entities.Entity;


/**
 * Created by haim7 on 23/03/2016.
 */
public class Reflection {

    private static Map<String, Method> getGetters(Class<?> clazz) {
        Map<String, Method> map = new HashMap<>();
        Stream.of(clazz.getMethods())
                .filter(Reflection::isGetter)
                .forEach(m -> map.put(propertyNameFromGetter(m), m));
        return map;
    }

    static boolean isGetter(Method m) {
        return m.getName().startsWith("get") && (Character.isUpperCase(m.getName().charAt(3))) && !m.getName().equals("getClass");
    }

    static String propertyNameFromGetter(Method m) {
        return Character.toLowerCase(m.getName().charAt(3)) + m.getName().substring(4);
    }

    private static Map<String, Object> applyGetters(Map<String, Method> gettersMap, Object obj) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> valuesMap = new HashMap<>();
        for (Map.Entry<String, Method> entry : gettersMap.entrySet()) {
            valuesMap.put(entry.getKey(), entry.getValue().invoke(obj));
        }
        return valuesMap;
    }

    /**
     * Getters that returns Entity classes will show only the id.
     * @param o The object to reflect
     * @return String describes all values of getters.
     */
    public static String publicGettersToString(Object o) {
        try {
            Map<String, Object> values = Reflection.applyGetters(Reflection.getGetters(o.getClass()), o);
            for (Map.Entry<String, Object> e : values.entrySet()) {
                if (e.getValue() instanceof CharSequence)
                    e.setValue("'" + e.getValue() + "'");
                else if (e.getValue() instanceof Entity)
                    e.setValue("id_" + ((Entity) e.getValue()).getId());
            }
            return o.getClass().getSimpleName() + values;
        } catch (InvocationTargetException | IllegalAccessException e) {
            return "error on reflection"; // Should not be reached
        }
    }
}

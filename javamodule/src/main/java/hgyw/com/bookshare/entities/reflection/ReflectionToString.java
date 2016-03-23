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
public class ReflectionToString extends Reflection {

    /**
     * Returns map of property_name-getter of class clazz. the getter is instance of Method class.
     * @param clazz The class to reflect.
     * @return Map of property_name-getter.
     */
    private static Map<String, Method> getGetters(Class<?> clazz) {
        Map<String, Method> map = new HashMap<>();
        Stream.of(clazz.getMethods())
                .filter(ReflectionToString::isGetter)
                .forEach(m -> map.put(propertyNameFromGetter(m), m));
        return map;
    }

    /**
     * Gets map of property_name-getter and returns map of property-value. the getters are instance
     * of Method class.
     * @param gettersMap The map of getters.
     * @param obj The object on which apply the getters.
     * @return map of property-value.
     * @throws InvocationTargetException In calling the getters
     * @throws IllegalAccessException In calling the getters
     */
    private static Map<String, Object> applyGetters(Map<String, Method> gettersMap, Object obj) throws InvocationTargetException, IllegalAccessException {
        Map<String, Object> valuesMap = new HashMap<>();
        for (Map.Entry<String, Method> entry : gettersMap.entrySet()) {
            valuesMap.put(entry.getKey(), entry.getValue().invoke(obj));
        }
        return valuesMap;
    }

    /**
     * Create string of public getters.
     * - "id" will be shown first.
     * - Getters that returns Entity classes will show only the id.
     * @param o The object to reflect
     * @return String describes all values of getters.
     */
    public static String publicGettersToString(Object o) {
        try {
            Map<String, Object> values = ReflectionToString.applyGetters(ReflectionToString.getGetters(o.getClass()), o);
            for (Map.Entry<String, Object> e : values.entrySet()) {
                if (e.getValue() instanceof CharSequence)
                    e.setValue("'" + e.getValue() + "'");
                else if (e.getValue() instanceof Entity)
                    e.setValue("id_" + ((Entity) e.getValue()).getId());
            }
            Object id = values.remove("id");
            String idString = id == null ? "" : "{" + id + "}";
            return o.getClass().getSimpleName() + idString + values;
        } catch (InvocationTargetException | IllegalAccessException e) {
            return "error on reflection"; // Should not be reached
        }
    }
}

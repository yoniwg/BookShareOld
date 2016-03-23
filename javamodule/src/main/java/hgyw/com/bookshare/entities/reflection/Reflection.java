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
class Reflection {

    static boolean isGetter(Method m) {
        return m.getName().startsWith("get") && (Character.isUpperCase(m.getName().charAt(3))) && !m.getName().equals("getClass");
    }

    static String propertyNameFromGetter(Method m) {
        return Character.toLowerCase(m.getName().charAt(3)) + m.getName().substring(4);
    }

}

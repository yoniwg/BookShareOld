package hgyw.com.bookshare.auxiliaries;

/**
 * Created by haim7 on 23/03/2016.
 */
public final class Auxiliaries {
    
    public static <T extends Comparable<T>> boolean isBetween(T value, T fromValue, T toValue) {
        return (fromValue==null || value.compareTo(fromValue) >= 0)
                && (toValue==null || value.compareTo(toValue) < 0);
    }
    
}

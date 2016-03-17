package hgyw.com.bookshare.entities;

import java.util.NoSuchElementException;

/**
 * Created by Yoni on 3/15/2016.
 */
public enum UserType{

    CUSTOMER(Customer.class),
    SUPPLIER(Supplier.class),
    GUEST(User.class);

    Class<?> clazz;

    UserType(Class<?> clazz){
        this.clazz = clazz;
    }

    public static UserType ofClass(Class<?> clazz){
        for (UserType ut : values()) {
            if (ut.clazz == clazz) return ut;
        }
        throw new NoSuchElementException("No such UserType instance");
    }
}

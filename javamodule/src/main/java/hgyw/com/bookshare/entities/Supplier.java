package hgyw.com.bookshare.entities;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Supplier extends User {
    @Override
    public UserType userType() {
        return UserType.SUPPLIER;
    }
}

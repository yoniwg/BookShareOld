package hgyw.com.bookshare.entities;

/**
 * Created by Yoni on 3/18/2016.
 */
public class Guest extends User {
    @Override
    public UserType userType() {
        return UserType.GUEST;
    }
}

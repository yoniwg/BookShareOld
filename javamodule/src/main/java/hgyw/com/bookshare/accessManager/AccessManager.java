package hgyw.com.bookshare.accessManager;

import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;

/**
 * Created by haim7 on 23/03/2016.
 */
public interface AccessManager {
    void signUp(User user);

    void signIn(Credentials credentials);

    void signOut();

    GeneralAccess getGeneralAccess();

    CustomerAccess getCustomerAccess();

    SupplierAccess getSupplierAccess();

    UserType getCurrentUserType();
}

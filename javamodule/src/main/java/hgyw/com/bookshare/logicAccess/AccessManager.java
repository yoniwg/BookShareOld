package hgyw.com.bookshare.logicAccess;

import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;
import hgyw.com.bookshare.exceptions.WrongLoginException;

/**
 * Created by haim7 on 23/03/2016.
 */
public interface AccessManager {

    /**
     * Returns whether username is taken.
     * @param username The username to check.
     * @return boolean value.
     */
    boolean isUserNameTaken(String username);

    /**
     * Sign up new user.
     * @param user The user to sign up.
     * @throws IllegalArgumentException if user is not instance of Customer or Supplier. or user id is not 0.
     * @throws WrongLoginException
     */
    void signUp(User user) throws WrongLoginException;

    void signIn(Credentials credentials) throws WrongLoginException;

    void signOut();

    GeneralAccess getGeneralAccess();

    CustomerAccess getCustomerAccess();

    SupplierAccess getSupplierAccess();

    UserType getCurrentUserType();
}

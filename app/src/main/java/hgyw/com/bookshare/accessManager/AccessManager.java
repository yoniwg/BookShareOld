package hgyw.com.bookshare.accessManager;

import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Guest;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;

/**
 * Created by Yoni on 3/13/2016.
 */
public enum  AccessManager{

    INSTANCE;

    private GeneralAccess currentAccess;
    private User currentUser;
    private UserType userType;

    AccessManager() {
        currentUser = new Guest();
        switchAccess();
    }


    public void signUp(User user) {
        signIn(user.getCredentials());
    }

    public void signIn(Credentials credentials) {
        switchAccess();
    }

    private void switchAccess() {
        switch (UserType.ofClass(currentUser.getClass())) {
            case GUEST:
                currentAccess = new GeneralAccessImpl(currentUser);
                break;
            case CUSTOMER:
                //currentAccess = new CustomerAccessImpl(currentUser);
                break;
            case SUPPLIER:
                //currentAccess = new SupplierAccessImpl(currentUser);
                break;
        }

    }

    public void signOut() {
        currentUser = null;
        switchAccess();
    }

    public GeneralAccess getGeneralAccess() {
        return currentAccess;
    }

    public CustomerAccess getCustomerAccess() {
        if (currentAccess instanceof CustomerAccess)
            return (CustomerAccess) currentAccess;
        throw new IllegalStateException();
    }

    public SupplierAccess getSupplierAccess() {
        if (currentAccess instanceof SupplierAccess)
            return (SupplierAccess) currentAccess;
        throw new IllegalStateException();
    }

    public UserType getCurrentUserType() {
        return UserType.ofClass(currentUser.getClass());
    }
}

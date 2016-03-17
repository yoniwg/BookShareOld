package hgyw.com.bookshare.accessManager;

import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;

/**
 * Created by Yoni on 3/13/2016.
 */
public enum  AccessManager {

    INSTANCE;

    private GeneralAccess currentAccess;
    private User currentUser;
    private UserType userType;

    AccessManager() {
        currentUser = new User();
        swichAccess();
    }


    public void signUp(User user) {
        signIn(user.getCredentials());
        int i =0;
    }

    public void signIn(Credentials credentials) {
        swichAccess();
    }

    private void swichAccess() {
        switch (UserType.ofClass(currentUser.getClass())) {
            case GUEST:
                currentAccess = new GeneralAccessImpl(currentUser);
                break;
            case CUSTOMER:
                currentAccess = new CustomerAccessImpl(currentUser);
                break;
            case SUPPLIER:
                currentAccess = new SupplierAccessImpl(currentUser);
                break;
        }

    }

    public void signOut() {
        currentUser = null;
        swichAccess();
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

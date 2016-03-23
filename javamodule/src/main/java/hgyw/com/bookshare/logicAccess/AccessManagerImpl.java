package hgyw.com.bookshare.logicAccess;

import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Guest;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;

/**
 * Created by Yoni on 3/13/2016.
 */
public enum AccessManagerImpl implements AccessManager {

    INSTANCE;

    final private ExpandedCrud crud = new ExpandedCrud();
    private GeneralAccess currentAccess;
    private User currentUser;
    private User guest = new Guest();

    AccessManagerImpl() {
        currentUser = guest;
        switchAccess();
    }


    @Override
    public void signUp(User user) {
        if (user.getId() == 0) throw new IllegalArgumentException("New item should have id 0.");
        if (crud.isUsernameTaken(user.getCredentials().getUsername())) {
            throw new RuntimeException("Username and password are taken.");
        }
        crud.createEntity(user);
        signIn(user.getCredentials());
    }

    @Override
    public void signIn(Credentials credentials) {
        if (currentUser != guest) throw new IllegalStateException("There has been a user that is signed in.");
        currentUser = crud.retrieveUserWithCredentials(credentials);
        if (currentUser == null) throw new RuntimeException("Wrong usenname and password"); // TODO specific exception
        switchAccess();
    }

    private void switchAccess() {
        switch (getCurrentUserType()) {
            case GUEST:
                currentAccess = new GeneralAccessImpl(crud, currentUser);
                break;
            case CUSTOMER:
                currentAccess = new CustomerAccessImpl(crud, currentUser);
                break;
            case SUPPLIER:
                //currentAccess = new SupplierAccessImpl(currentUser);
                break;
        }

    }

    @Override
    public void signOut() {
        currentUser = guest;
        switchAccess();
    }

    @Override
    public GeneralAccess getGeneralAccess() {
        return currentAccess;
    }

    @Override
    public CustomerAccess getCustomerAccess() {
        if (currentAccess instanceof CustomerAccess)
            return (CustomerAccess) currentAccess;
        throw new IllegalStateException("Customer user has not registered.");
    }

    @Override
    public SupplierAccess getSupplierAccess() {
        if (currentAccess instanceof SupplierAccess)
            return (SupplierAccess) currentAccess;
        throw new IllegalStateException("Supplier user has not registered.");
    }

    @Override
    public UserType getCurrentUserType() {
        return currentUser.userType();
    }

}

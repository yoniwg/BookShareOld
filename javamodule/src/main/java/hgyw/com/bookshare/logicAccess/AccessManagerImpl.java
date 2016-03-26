package hgyw.com.bookshare.logicAccess;

import hgyw.com.bookshare.crud.CrudFactory;
import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Guest;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;

/**
 * Created by Yoni on 3/13/2016.
 */
enum  AccessManagerImpl implements AccessManager {
    INSTANCE;

    private final User guest = new Guest();
    private final ExpandedCrud crud = (ExpandedCrud) CrudFactory.getInstance();
    private GeneralAccess currentAccess;
    private User currentUser;

    AccessManagerImpl() {
        switchAccess(guest);
    }


    @Override
    public boolean isUserNameTaken(String username) {
        return crud.isUsernameTaken(username);
    }

    @Override
    public void signUp(User user) {
        if (!(user instanceof Customer || user instanceof Supplier)) throw new IllegalArgumentException("The user should be instance of Customer or Supplier.");
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
        User newUser = crud.retrieveUserWithCredentials(credentials);
        if (newUser == null) throw new RuntimeException("Wrong username and password"); // TODO specific exception
        switchAccess(newUser);
    }

    private void switchAccess(User newUser) {
        switch (newUser.userType()) {
            case GUEST:
                currentAccess = new GeneralAccessImpl(crud, newUser);
                break;
            case CUSTOMER:
                currentAccess = new CustomerAccessImpl(crud, (Customer) newUser);
                break;
            case SUPPLIER:
                currentAccess = new SupplierAccessImpl(crud, (Supplier) newUser);
                break;
        }
        currentUser = newUser;
    }

    @Override
    public void signOut() {
        switchAccess(guest);
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

package hgyw.com.bookshare.accessManager;

import com.annimon.stream.Stream;

import hgyw.com.bookshare.crud.CrudFactory;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Guest;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.UserType;

/**
 * Created by Yoni on 3/13/2016.
 */
public enum AccessManagerImpl implements AccessManager {

    INSTANCE;

    private GeneralAccess currentAccess;
    private User currentUser;
    private User guest = new Guest();

    AccessManagerImpl() {
        currentUser = guest;
        switchAccess();
    }


    @Override
    public void signUp(User user) {
        signIn(user.getCredentials());
    }

    @Override
    public void signIn(Credentials credentials) {
        if (currentUser != guest) throw new IllegalStateException("There has been a user that is signed in.");
        currentUser = streamAllUsers()
                .filter(u -> u.getCredentials().equals(credentials))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Wrong usenname and password")); // TODO sacrificial exception
        switchAccess();
    }

    private Stream<User> streamAllUsers() {
        return Stream.concat(
                CrudFactory.getInstance().streamAll(Customer.class),
                CrudFactory.getInstance().streamAll(Supplier.class)
        );
    }

    private void switchAccess() {
        switch (UserType.ofClass(currentUser.getClass())) {
            case GUEST:
                currentAccess = new GeneralAccessImpl(currentUser);
                break;
            case CUSTOMER:
                currentAccess = new CustomerAccessImpl(currentUser);
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
        return UserType.ofClass(currentUser.getClass());
    }

}

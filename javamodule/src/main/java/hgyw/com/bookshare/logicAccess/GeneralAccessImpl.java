package hgyw.com.bookshare.logicAccess;

import java.util.Collection;

import hgyw.com.bookshare.dataAccess.DataAccess;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/18/2016.
 */
class GeneralAccessImpl implements GeneralAccess {

    final protected DataAccess crud;
    final private User currentUser;

    protected void requireItsMeForAccess(User user) {
        if (!user.equals(currentUser)) {
            throw new IllegalArgumentException("The current user has not access to manipulate other users.");
        }
    }

    public GeneralAccessImpl(DataAccess crud, User currentUser) {
        this.crud = crud;
        this.currentUser = currentUser;
    }

    @Override
    public Collection<BookSupplier> findBooks(BookQuery query) {
        return crud.findBooks(query);
    }

    @Override
    public Collection<BookSupplier> findSpecialOffers(int limit) {
        return crud.findSpecialOffers(currentUser, limit);
    }

    @Override
    public Collection<BookReview> getBookReviews(Book book) {
        return crud.findEntityReferTo(BookReview.class, book);
    }

    @Override
    public Collection<BookSupplier> retrieveSuppliers(Book book) {
        return crud.findEntityReferTo(BookSupplier.class, book);
    }

    public <T extends User> T retrieveUserDetails(T currentUser) {
        return crud.retrieveEntity(currentUser);
    }

    public <T extends User> void updateUserDetails(T currentUser, T newDetails) {
        requireItsMeForAccess(newDetails);
        newDetails.setCredentials(crud.retrieveEntity(currentUser).getCredentials()); // Avoid change credentials by this method.
        crud.updateEntity(newDetails);
    }

}

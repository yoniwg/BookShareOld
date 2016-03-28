package hgyw.com.bookshare.logicAccess;

import java.util.Collection;

import hgyw.com.bookshare.dataAccess.DataAccess;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/18/2016.
 */
class GeneralAccessImpl implements GeneralAccess {

    final protected DataAccess dataAccess;
    final private User currentUser;

    protected void requireItsMeForAccess(User user) {
        if (!user.equals(currentUser)) {
            throw new IllegalArgumentException("The current user has not access to manipulate other users.");
        }
    }

    public GeneralAccessImpl(DataAccess dataAccess, User currentUser) {
        this.dataAccess = dataAccess;
        this.currentUser = currentUser;
    }

    @Override
    public Collection<BookSupplier> findBooks(BookQuery query) {
        return dataAccess.findBooks(query);
    }

    @Override
    public Collection<BookSupplier> findSpecialOffers(int limit) {
        return dataAccess.findSpecialOffers(currentUser, limit);
    }

    @Override
    public Collection<BookReview> getBookReviews(Book book) {
        return dataAccess.findEntityReferTo(BookReview.class, book);
    }

    @Override
    public Collection<BookSupplier> retrieveSuppliers(Book book) {
        return dataAccess.findEntityReferTo(BookSupplier.class, book);
    }

    public <T extends User> T retrieveUserDetails(T currentUser) {
        return dataAccess.retrieveEntity(currentUser);
    }

    public <T extends User> void updateUserDetails(T currentUser, T newDetails) {
        requireItsMeForAccess(newDetails);
        newDetails.setCredentials(dataAccess.retrieveEntity(currentUser).getCredentials()); // Avoid change credentials by this method.
        dataAccess.updateEntity(newDetails);
    }

}

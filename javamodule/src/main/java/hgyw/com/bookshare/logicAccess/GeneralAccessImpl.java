package hgyw.com.bookshare.logicAccess;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderedBook;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/18/2016.
 */
class GeneralAccessImpl implements GeneralAccess {

    final protected ExpandedCrud crud;
    final protected User currentUser;

    protected void requireItsMeForAccess(User user) {
        if (!user.equals(getCurrentUser())) {
            throw new IllegalArgumentException("The current user has not access to manipulate other users.");
        }
    }

    public GeneralAccessImpl(ExpandedCrud crud, User currentUser) {
        this.crud = crud;
        this.currentUser = currentUser;
    }

    @Override
    public Collection<BookSupplier> findBooks(BookQuery query) {
        return crud.findBooks(query);
    }

    @Override
    public Collection<BookSupplier> findSpecialOffers() {
        return crud.findSpecialOffers(currentUser);
    }

    @Override
    public Collection<BookReview> getBookReviews(Book book) {
        return crud.findEntityReferTo(BookReview.class, book);
    }

    @Override
    public User getCurrentUser() {
        return (User) currentUser.clone();
    }

    @Override
    public Collection<Supplier> retrieveSuppliers(Book book) {
        return crud.findEntityReferTo(Supplier.class, book);
    }

}
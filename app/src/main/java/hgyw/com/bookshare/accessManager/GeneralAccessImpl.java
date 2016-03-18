package hgyw.com.bookshare.accessManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hgyw.com.bookshare.backend.Backend;
import hgyw.com.bookshare.backend.BackendFactory;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/18/2016.
 */
public class GeneralAccessImpl implements GeneralAccess {

    private Backend backend = BackendFactory.getInstance();

    public GeneralAccessImpl(User currentUser) {
    }

    @Override
    public List<Book> findBooks(BookQuery query) {
        return Arrays.asList((Book[]) backend.getStream(Book.class).filter(query).toArray());
    }

    @Override
    public List<Book> findSpecialOffers(BookQuery query) {
        return null;
    }

    @Override
    public List<BookReview> getBookReviews(Book book) {
        return null;
    }

    @Override
    public User getUser() {
        return null;
    }
}

package hgyw.com.bookshare.logicAccess;

import java.util.Collection;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Supplier;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface GeneralAccess {

    //==information==

    Collection<BookSupplier> findBooks(BookQuery query);
    Collection<BookSupplier> findSpecialOffers(int limit);
    Collection<BookReview> getBookReviews(Book book);
    Collection<Supplier> retrieveSuppliers(Book book);

    Object getCurrentUser();


}

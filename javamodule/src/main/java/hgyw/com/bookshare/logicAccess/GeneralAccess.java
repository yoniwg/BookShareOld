package hgyw.com.bookshare.logicAccess;

import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface GeneralAccess {

    //==information==

    List<BookSupplier> findBooks(BookQuery query);
    List<BookSupplier> findSpecialOffers();
    List<BookReview> getBookReviews(Book book);

    Object getCurrentUser();


}

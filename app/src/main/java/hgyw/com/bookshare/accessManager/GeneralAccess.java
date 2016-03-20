package hgyw.com.bookshare.accessManager;

import android.media.Image;
import android.renderscript.ScriptGroup;

import java.net.URI;
import java.net.URL;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface GeneralAccess {

    //==information==

    List<BookSupplier> findBooks(BookQuery query);
    List<BookSupplier> findSpecialOffers();
    List<BookReview> getBookReviews(Book book);

    User getUser();


}

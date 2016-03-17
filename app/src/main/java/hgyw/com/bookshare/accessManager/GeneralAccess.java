package hgyw.com.bookshare.accessManager;

import android.media.Image;
import android.renderscript.ScriptGroup;

import java.net.URI;
import java.net.URL;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface GeneralAccess {

    //==information==

    List<Book> findBooks(BookQuery query);
    List<Book> findSpecialOffers(BookQuery query);
    List<BookReview> getBookReviews(Book book);
    URL getImageById(long imageId);

    User getUser();


}

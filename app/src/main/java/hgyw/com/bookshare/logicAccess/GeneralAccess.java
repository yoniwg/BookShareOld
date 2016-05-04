package hgyw.com.bookshare.logicAccess;

import java.util.Collection;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface GeneralAccess {

    /**
     * Find books by query.
     * @param query The query
     * @return collections of BookSuppliers math the quary.
     */
    Collection<BookSupplier> findBooks(BookQuery query);

    /**
     * Find special offers for current user.
     * @param limit number of the offers are requested.
     * @return Collection of BookSuppliers.
     */
    Collection<BookSupplier> findSpecialOffers(int limit);

    /**
     *
     * @param book the book.
     * @return collection of BookReview.
     */
    Collection<BookReview> getBookReviews(Book book);

    /**
     * Retrieve BookSuppliers of book.
     * @param book the book
     * @return Collection of BookSuppliers.
     * @throws java.util.NoSuchElementException if the book is not found in database
     */
    Collection<BookSupplier> retrieveSuppliers(Book book);

    /**
     * Retrieve Books of Suppliers.
     * @param supplier the supplier
     * @return Collection of BookSuppliers.
     * @throws java.util.NoSuchElementException if the supplier is not found in database
     */
    Collection<BookSupplier> retrieveBooksOfSuppliers(Supplier supplier);


     <T extends Entity> T retrieve(Class<T> entityClass, long entityId);
}

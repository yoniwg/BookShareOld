package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderStatus;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface SupplierAccess extends GeneralAccess {

    /**
     * retrieve books of current supplier user
     * @return collection of BookSupplier
     */
    Collection<BookSupplier> retrieveMyBooks();

    /**
     * Add book to database.
     * @param book the book. the id will set to generated id.
     * @throws IllegalArgumentException if the book id is not 0.
     */
    void addBook(Book book);

    /**
     * Update book in database.
     * @param book the book.
     * @throws java.util.NoSuchElementException if a book with such id is not found.
     */
    void updateBook(Book book);

    /**
     * Remove the book with id of book.getId() from database.
     * @param book the book by its id to remove.
     * @throws java.util.NoSuchElementException if a book with such id is not found.
     */
    void removeBook(Book book);

    /**
     * retrieve the supplier's details
     * @return the details
     */
    User retrieveSupplierDetails();

    /**
     * replace the current supplier's details with the provided details
     * @param newDetails the new details
     * @throws IllegalArgumentException if the customer id does not match the current user.
     */
    void updateSupplierDetails(Supplier newDetails);

    /**
     * retrieve all orders from current supplier in a provided period
     * @param fromDate begin of period
     * @param toDate end of period
     * @return collection of orders
     */
    Collection<Order> retrieveOrders(Date fromDate, Date toDate);

    /**
     * retrieve all active orders from current supplier (not closed or canceled)
     * @return collection of orders
     */
    Collection<Order> retrieveActiveOrders(Date fromDate, Date toDate);

    /**
     * update the order status
     * @param orderId the id of order
     * @param orderStatus the new status
     * @throws java.util.NoSuchElementException if the order with id orderId is not found.
     * @throws IllegalStateException if current status of order is not match the new (according to implementation).
     */
    void updateOrderStatus(long orderId, OrderStatus orderStatus);

}

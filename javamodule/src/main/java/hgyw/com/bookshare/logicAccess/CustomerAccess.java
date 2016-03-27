package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.Transaction;
import hgyw.com.bookshare.exceptions.NewTransactionException;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface CustomerAccess extends GeneralAccess {

    /**
     * retrieve the customer's details
     * @return the details
     */
    Customer retrieveCustomerDetails();

    /**
     * replace the current customer's details with the provided details
     * @param newDetails - the new details
     */
    void updateCustomerDetails(Customer newDetails);

    /**
     * get all customer's book reviews
     * @return the book reviews
     */
    Collection<BookReview> getCustomerReviews();

    /**
     * find the users which are interested in the provided book
     * @param book the book to be interested in
     * @return collection of interested users
     */
    Collection<Customer> findInterestedInBook(Book book);

    /**
     * retrieve all order in a provided period
     * @param fromDate - begin of period
     * @param toDate - end of period
     * @return collection of orders
     */
    Collection<Order> retrieveOrders(Date fromDate, Date toDate);

    /**
     * retrieve all active orders (not closed or canceled)
     * @return collection of active orders
     */
    Collection<Order> retrieveActiveOrders();

    /**
     * make new transaction and associate collection of orders to it
     * @param transaction - the transaction
     * @param orders - collection of orders
     * @throws NewTransactionException
     */
    void performNewTransaction(Transaction transaction, Collection<Order> orders) throws NewTransactionException;

    /**
     * request to cancel the order
     * @param orderId - the order id to cancel
     */
    void cancelOrder(long orderId);

    /**
     * update the order's rating
     * @param orderId - the order to rate
     * @param orderRating - the order's rating
     */
    void updateOrderRating(long orderId, OrderRating orderRating);

    /**
     * write a review for a book
     * @param bookReview - the book review
     */
    void writeBookReview(BookReview bookReview);

    /**
     * remove a book review
     * @param bookReview - the review with ID to delete
     */
    void removeBookReview(BookReview bookReview);
}

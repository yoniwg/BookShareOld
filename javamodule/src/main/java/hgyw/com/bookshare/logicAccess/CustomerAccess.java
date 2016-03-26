package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    Customer retrieveCustomerDetails();
    void updateCustomerDetails(Customer newDetails);

    Collection<BookReview> getCustomerReviews();
    Collection<Customer> findInterestedInBook(Book book);
    Collection<Order> retrieveOrders(Date fromDate, Date toDate);
    Collection<Order> retrieveOpenOrders();

    void performNewTransaction(Transaction transaction, Collection<Order> orders) throws NewTransactionException;
    void cancelOrder(long orderId);
    void updateOrderRating(long orderId, OrderRating orderRating);

    void writeBookReview(BookReview bookReview);
    void removeBookReview(BookReview bookReview);
}

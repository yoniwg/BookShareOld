package hgyw.com.bookshare.accessManager;

import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.Supplier;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface CustomerAccess extends GeneralAccess {

    Customer retrieveCustomerDetails();
    void updateCustomerDetails(Customer newDetails);

    List<BookReview> getCustomerReviews();
    List<Customer> findInterestedInBook(Book book);
    List<Order> retrieveOrders(Date fromDate, Date toDate);
    List<Order> retrieveOpenOrders();

    void performNewOrder(Order order);
    void cancelOrder(long orderId);
    void updateOrderRating(long orderId, OrderRating orderRating);
    void writeBookReview(BookReview bookReview);
    void removeBookReview(BookReview bookReview);

    List<Supplier> retrieveSuppliers(Book book);
}

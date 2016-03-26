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

    Collection<BookSupplier> retrieveMyBooks();
    void addBook(Book book);
    void updateBook(Book book);
    void removeBook(Book book);

    User retrieveSupplierDetails();
    void updateSupplierDetails(Supplier newDetails);

    Collection<Order> retrieveOrders(Date fromDate, Date toDate);
    Collection<Order> retrieveOpenOrders(Date fromDate, Date toDate);
    void updateOrderStatus(long orderId, OrderStatus orderStatus);

}

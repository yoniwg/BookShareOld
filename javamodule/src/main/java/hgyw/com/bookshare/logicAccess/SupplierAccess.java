package hgyw.com.bookshare.logicAccess;

import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.Supplier;

/**
 * Created by Yoni on 3/13/2016.
 */
public interface SupplierAccess extends GeneralAccess {

    List<Book> retrieveMyBooks();
    void addBook(Book book);
    void updateBook(Book book);
    void removeBook(Book book);

    Supplier retrieveSupplierDetails();
    void updateSupplierDetails(Supplier newDetails);

    List<Order> retrieveOrders(Date fromDate, Date toDate);
    List<Order> retrieveOpenOrders(Date fromDate, Date toDate);
    List<Order> updateOrderStatus(Order order);

}

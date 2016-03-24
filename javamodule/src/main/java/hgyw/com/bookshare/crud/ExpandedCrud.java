package hgyw.com.bookshare.crud;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 24/03/2016.
 */
public interface ExpandedCrud extends Crud {

    User retrieveUserWithCredentials(Credentials credentials);

    boolean isUsernameTaken(String username);

    Collection<Customer> findInterestedInBook(Book book);

    Collection<Order> retrieveOrders(Customer customer, Date fromDate, Date toDate, boolean onlyOpen);

    Collection<BookSupplier> findBooks(BookQuery query);

    Collection<BookSupplier> findSpecialOffers(User currentUser);

}

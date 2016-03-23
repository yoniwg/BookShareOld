package hgyw.com.bookshare.crud;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.entities.Auxiliaries;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 23/03/2016.
 */
public class ExpandedCrud extends ListsCrudImpl {

    public User retrieveUserWithCredentials(Credentials credentials) {
        return streamAllUsers().filter(u -> u.getCredentials().equals(credentials))
                .findFirst()
                .orElse(null);
    }

    public boolean isUsernameTaken(String username) {
        return streamAllUsers()
                .anyMatch(u -> u.getCredentials().getUsername().equals(username));
    }

    private Stream<User> streamAllUsers() {
        return Stream.concat(streamAll(Customer.class), streamAll(Supplier.class));
    }

    public Collection<Customer> findInterestedInBook(Book book) {
        return streamAll(Order.class)
                .filter(o -> Stream.of(o.getOrderedBooks())
                        .anyMatch(ob -> ob.getBookSupplier().getBook().equals(book)))
                .map(Order::getCustomer)
                .collect(Collectors.toList());
    }

    public Collection<Order> retrieveOrders(Customer customer, Date fromDate, Date toDate, boolean onlyOpen) {
        return streamAll(Order.class)
                .filter(o -> o.getCustomer().equals(customer)
                                && Auxiliaries.isBetween(o.getDate(), fromDate, toDate)
                                && (!onlyOpen || o.isOpen())
                ).collect(Collectors.toList());
    }
}

package hgyw.com.bookshare.accessManager;

import com.annimon.stream.Collectors;

import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 20/03/2016.
 */
public class CustomerAccessImpl extends GeneralAccessImpl implements CustomerAccess {

    public CustomerAccessImpl(User currentUser) {
        super(currentUser);
    }

    @Override
    public Customer retrieveCustomerDetails() {
        return getBackend().retrieveEntity(Customer.class, getUser().getId());
    }

    @Override
    public void updateCustomerDetails(Customer newDetails) {
        getBackend().updateEntity(newDetails);
    }

    @Override
    public List<BookReview> getCustomerReviews() {
        return getBackend().getStream(BookReview.class)
                .filter(r -> r.getReviewer().equals(getUser()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findInterestedInBook(Book book) {
        return null;
    }

    @Override
    public List<Order> retrieveOrders(Date fromDate, Date toDate) {
        return getBackend().getStream(Order.class)
                .filter(o -> o.getDate().compareTo(fromDate) >= 0 && o.getDate().compareTo(toDate) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> retrieveOpenOrders() {
        return getBackend().getStream(Order.class)
                .filter(Order::isOpen)
                .collect(Collectors.toList());
    }

    @Override
    public void performNewOrder(Order order) {

    }

    @Override
    public void cancelOrder(Order order) {

    }

    @Override
    public void updateOrderResponse(Order order) {

    }

    @Override
    public void addBookReview(BookReview bookReview) {

    }

    @Override
    public void updateBookReview(BookReview bookReview) {

    }

    @Override
    public void removeBookReview(BookReview bookReview) {

    }

    @Override
    public List<Supplier> retrieveSuppliers(Book book) {
        return getBackend().getStream(BookSupplier.class)
                .filter(bs -> bs.getBook().equals(book))
                .map(BookSupplier::getSupplier)
                .collect(Collectors.toList());
    }

}

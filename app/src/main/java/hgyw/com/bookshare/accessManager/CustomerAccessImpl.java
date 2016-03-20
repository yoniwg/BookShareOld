package hgyw.com.bookshare.accessManager;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
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
        return getCrud().retrieveEntity(Customer.class, currentUser.getId());
    }

    @Override
    public void updateCustomerDetails(Customer newDetails) {
        if (newDetails.getId() != currentUser.getId())
            throw new IllegalArgumentException("The ID is not compatible with the current user");
        getCrud().updateEntity(newDetails);
    }

    @Override
    public List<BookReview> getCustomerReviews() {
        return getCrud().getStream(BookReview.class)
                .filter(r -> r.getReviewer().equals(currentUser))
                .collect(Collectors.toList());
    }

    @Override
    public List<Customer> findInterestedInBook(Book book) {
        return getCrud().getStream(Order.class)
                .filter(o -> Stream.of(o.getBooksList())
                        .anyMatch(ob -> ob.getBook().equals(book)))
                .map(Order::getCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> retrieveOrders(Date fromDate, Date toDate) {
        return getCrud().getStream(Order.class)
                .filter(o -> o.getDate().compareTo(fromDate) >= 0 && o.getDate().compareTo(toDate) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> retrieveOpenOrders() {
        return getCrud().getStream(Order.class)
                .filter(Order::isOpen)
                .collect(Collectors.toList());
    }

    @Override
    public void performNewOrder(Order order) {
        // TODO: That's it? Any validation?
        getCrud().createEntity(order);
    }

    @Override
    public void cancelOrder(long orderId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateOrderRating(long orderId, OrderRating orderRating) {
        Order order = getCrud().retrieveEntity(Order.class, orderId);
        order.setOrderRating(orderRating);
        getCrud().updateEntity(order);
    }

    @Override
    public void writeBookReview(BookReview bookReview) {
        getCrud().updateEntity(bookReview);
    }

    @Override
    public void removeBookReview(BookReview bookReview) {
        getCrud().deleteEntity(bookReview);
    }

    @Override
    public List<Supplier> retrieveSuppliers(Book book) {
        return getCrud().getStream(BookSupplier.class)
                .filter(bs -> bs.getBook().equals(book))
                .map(BookSupplier::getSupplier)
                .collect(Collectors.toList());
    }

}

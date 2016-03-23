package hgyw.com.bookshare.logicAccess;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 20/03/2016.
 */
public class CustomerAccessImpl extends GeneralAccessImpl implements CustomerAccess {

    public CustomerAccessImpl(ExpandedCrud crud, User currentUser) {
        super(crud, currentUser);
    }

    @Override
    public Customer retrieveCustomerDetails() {
        return crud.retrieveEntity(Customer.class, currentUser.getId());
    }

    @Override
    public void updateCustomerDetails(Customer newDetails) {
        if (newDetails.getId() != currentUser.getId())
            throw new IllegalArgumentException("The ID is not compatible with the current user");
        crud.updateEntity(newDetails);
    }

    @Override
    public Collection<BookReview> getCustomerReviews() {
        return crud.findEntityReferTo(BookReview.class, currentUser);
    }

    @Override
    public Collection<Customer> findInterestedInBook(Book book) {
        return crud.findInterestedInBook(book);
    }

    @Override
    public Collection<Order> retrieveOrders(Date fromDate, Date toDate) {
        return crud.retrieveOrders((Customer)currentUser, fromDate, toDate, false);
    }

    @Override
    public Collection<Order> retrieveOpenOrders() {
        return crud.retrieveOrders((Customer)currentUser, null, null, true);
    }

    @Override
    public void performNewOrder(Order order) {
        // TODO: That's it? Any validation?
        crud.createEntity(order);
    }

    @Override
    public void cancelOrder(long orderId) {
        throw new UnsupportedOperationException(); // TODO ?
    }

    @Override
    public void updateOrderRating(long orderId, OrderRating orderRating) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        order.setOrderRating(orderRating);
        crud.updateEntity(order);
    }

    @Override
    public void writeBookReview(BookReview bookReview) {
        crud.updateEntity(bookReview);
    }

    @Override
    public void removeBookReview(BookReview bookReview) {
        crud.deleteEntity(bookReview);
    }

    @Override
    public Collection<Supplier> retrieveSuppliers(Book book) {
        return crud.findEntityReferTo(Supplier.class, book);
    }

}

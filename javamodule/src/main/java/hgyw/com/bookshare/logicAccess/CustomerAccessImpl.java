package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;

import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 20/03/2016.
 */
class CustomerAccessImpl extends GeneralAccessImpl implements CustomerAccess {

    public CustomerAccessImpl(ExpandedCrud crud, User currentUser) {
        super(crud, currentUser);
    }

    @Override
    public Customer retrieveCustomerDetails() {
        return crud.retrieveEntity(Customer.class, currentUser.getId());
    }

    @Override
    public void updateCustomerDetails(Customer newDetails) {
        requireItsMe(newDetails);
        newDetails.setCredentials(retrieveCustomerDetails().getCredentials()); // Avoid change credentials by this method.
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
        requireItsMe(order.getCustomer());
        // TODO: That's it? Any validation?
        crud.createEntity(order);
    }

    @Override
    public void cancelOrder(long orderId) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        requireItsMe(order.getCustomer());
        throw new UnsupportedOperationException(); // TODO ?
    }

    @Override
    public void updateOrderRating(long orderId, OrderRating orderRating) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        requireItsMe(order.getCustomer());
        order.setOrderRating(orderRating);
        crud.updateEntity(order);
    }

    @Override
    public void writeBookReview(BookReview bookReview) {
        requireItsMe(bookReview.getCustomer());
        if (bookReview.getId() == 0) crud.createEntity(bookReview);
        else crud.updateEntity(bookReview);
    }

    @Override
    public void removeBookReview(BookReview bookReview) {
        requireItsMe(crud.retrieveEntity(bookReview).getCustomer());
        crud.deleteEntity(bookReview);
    }


}

package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;

import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.OrderStatus;
import hgyw.com.bookshare.entities.Transaction;
import hgyw.com.bookshare.exceptions.NewTransactionException;

/**
 * Created by haim7 on 20/03/2016.
 */
class CustomerAccessImpl extends GeneralAccessImpl implements CustomerAccess {

    final private Customer currentUser;

    public CustomerAccessImpl(ExpandedCrud crud, Customer currentUser) {
        super(crud, currentUser);
        this.currentUser = currentUser;
    }

    @Override
    public Customer retrieveCustomerDetails() {
        return crud.retrieveEntity(Customer.class, currentUser.getId());
    }

    @Override
    public void updateCustomerDetails(Customer newDetails) {
        requireItsMeForAccess(newDetails);
        newDetails.setCredentials(retrieveCustomerDetails().getCredentials()); // Avoid change credentials by this method.
        crud.updateEntity(newDetails);
    }

    @Override
    public Collection<BookReview> getCustomerReviews() {
        return crud.findEntityReferTo(BookReview.class, currentUser);
    }

    @Override
    public Collection<Customer> findInterestedInBook(Book book) {
        return crud.findInterestedInBook(book, currentUser);
    }

    @Override
    public Collection<Order> retrieveOrders(Date fromDate, Date toDate) {
        return crud.retrieveOrders(currentUser, null, fromDate, toDate, false);
    }

    @Override
    public Collection<Order> retrieveOpenOrders() {
        return crud.retrieveOrders(currentUser, null, null, null, true);
    }

    @Override
    public void performNewTransaction(Transaction transaction, Collection<Order> orders) throws NewTransactionException {
        requireItsMeForAccess(transaction.getCustomer());
        validateOrdersDetails(orders);
        for (Order o : orders) {
            o.setId(0);
            o.setOrderStatus(OrderStatus.NEW);
            o.setTransaction(transaction);
            crud.createEntity(o);
        }
        transaction.setDate(new Date());
        crud.createEntity(transaction);

    }

    private void validateOrdersDetails(Collection<Order> orders) throws NewTransactionException {
        for (Order o : orders) {
            BookSupplier realBookSupplier = crud.retrieveEntity(o.getBookSupplier());
            if (!o.getUnitPrice().equals(realBookSupplier.getPrice())) {
                throw new NewTransactionException(NewTransactionException.Issue.PRICE_NOT_MATCH, o);
            }
            if (realBookSupplier.getAmountAvailable() <= 0) {
                throw new NewTransactionException(NewTransactionException.Issue.NOT_AVAILABLE, o);
            }
        }
    }

    @Override
    public void cancelOrder(long orderId) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        requireItsMeForAccess(order.getTransaction().getCustomer());
        throw new UnsupportedOperationException(); // TODO ?
    }

    @Override
    public void updateOrderRating(long orderId, OrderRating orderRating) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        requireItsMeForAccess(order.getTransaction().getCustomer());
        order.setOrderRating(orderRating);
        crud.updateEntity(order);
    }

    @Override
    public void writeBookReview(BookReview bookReview) {
        requireItsMeForAccess(bookReview.getCustomer());
        if (bookReview.getId() == 0) crud.createEntity(bookReview);
        else crud.updateEntity(bookReview);
    }

    @Override
    public void removeBookReview(BookReview bookReview) {
        requireItsMeForAccess(crud.retrieveEntity(bookReview).getCustomer());
        crud.deleteEntity(bookReview);
    }


}

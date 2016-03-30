package hgyw.com.bookshare.logicAccess;

import com.annimon.stream.Stream;

import java.util.Collection;
import java.util.Date;
import java.util.Stack;

import hgyw.com.bookshare.dataAccess.DataAccess;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.OrderStatus;
import hgyw.com.bookshare.entities.Transaction;
import hgyw.com.bookshare.exceptions.OrdersTransactionException;

/**
 * Created by haim7 on 20/03/2016.
 */
class CustomerAccessImpl extends GeneralAccessImpl implements CustomerAccess {

    final private Customer currentUser;

    public CustomerAccessImpl(DataAccess crud, Customer currentUser) {
        super(crud, currentUser);
        this.currentUser = currentUser;
    }

    @Override
    public Customer retrieveCustomerDetails() {
        return retrieveUserDetails(currentUser);
    }

    @Override
    public void updateCustomerDetails(Customer newDetails) {
        updateUserDetails(currentUser, newDetails);
    }

    @Override
    public Collection<BookReview> getCustomerReviews() {
        return dataAccess.findEntityReferTo(BookReview.class, currentUser);
    }

    @Override
    public Collection<Customer> findInterestedInBook(Book book) {
        return dataAccess.findInterestedInBook(book, currentUser);
    }

    @Override
    public Collection<Order> retrieveOrders(Date fromDate, Date toDate) {
        return dataAccess.retrieveOrders(currentUser, null, fromDate, toDate, false);
    }

    @Override
    public Collection<Order> retrieveActiveOrders() {
        return dataAccess.retrieveOrders(currentUser, null, null, null, true);
    }

    @Override
    public void performNewTransaction(Transaction transaction, Collection<Order> orders) throws OrdersTransactionException {
        Stream.of(orders).forEach(Order::computePriceByBookSupplier);
        // validations and check that transaction can be done
        validateOrdersTransaction(orders);
        // create transaction
        transaction.setId(0);
        transaction.setDate(new Date());
        transaction.setCustomer(retrieveCustomerDetails());
        dataAccess.createEntity(transaction);
        // create orders
        for (Order o : orders) {
            o.setId(0);
            o.setOrderStatus(OrderStatus.NEW);
            o.setTransaction(transaction);
            dataAccess.createEntity(o);
            // decrease amount available
            BookSupplier bookSupplier = dataAccess.retrieveEntity(o.getBookSupplier());
            bookSupplier.setAmountAvailable(bookSupplier.getAmountAvailable());
            dataAccess.updateEntity(bookSupplier);
        }
    }

    private void validateOrdersTransaction(Collection<Order> orders) throws OrdersTransactionException {
        for (Order o : orders) {
            // Validates according to bookSupplier in the database, the Order::getBookSupplier() is
            //   only reference, and can be non updated.
            BookSupplier realBookSupplier = dataAccess.retrieveEntity(o.getBookSupplier());
            if (!o.getUnitPrice().equals(realBookSupplier.getPrice())) {
                throw new OrdersTransactionException(OrdersTransactionException.Issue.PRICE_NOT_MATCH, o);
            }
            if (realBookSupplier.getAmountAvailable() <= 0) {
                throw new OrdersTransactionException(OrdersTransactionException.Issue.NOT_AVAILABLE, o);
            }
        }
    }

    @Override
    public void cancelOrder(long orderId) {
        Order order = dataAccess.retrieveEntity(Order.class, orderId);
        requireItsMeForAccess(order.getTransaction().getCustomer());
        if (!(order.getOrderStatus() == OrderStatus.NEW
                || order.getOrderStatus() == OrderStatus.WAITING_FOR_PAYING)){
            throw new IllegalStateException("tc cancel the status must be " + OrderStatus.NEW + " or " + OrderStatus.WAITING_FOR_PAYING + ".");
        }
        order.setOrderStatus(OrderStatus.WAITING_FOR_CANCEL);
        dataAccess.updateEntity(order);
    }

    @Override
    public void updateOrderRating(long orderId, OrderRating orderRating) {
        Order order = dataAccess.retrieveEntity(Order.class, orderId);
        requireItsMeForAccess(order.getTransaction().getCustomer());
        order.setOrderRating(orderRating);
        dataAccess.updateEntity(order);
    }

    @Override
    public void addBookReview(BookReview bookReview) {
        bookReview.setId(0);
        bookReview.setCustomer(currentUser);
        if (dataAccess.findEntityReferTo(BookReview.class, currentUser, bookReview.getBook()).size() > 0) {
            throw new IllegalStateException("The user already has review on this book!");
        }
        dataAccess.createEntity(bookReview);
    }

    @Override
    public void updateBookReview(BookReview bookReview) {
        requireItsMeForAccess(dataAccess.retrieveEntity(bookReview).getCustomer());
        dataAccess.updateEntity(bookReview);
    }

    @Override
    public void removeBookReview(BookReview bookReview) {
        requireItsMeForAccess(dataAccess.retrieveEntity(bookReview).getCustomer());
        dataAccess.deleteEntity(bookReview);
    }


}

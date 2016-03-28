package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;

import hgyw.com.bookshare.dataAccess.DataAccess;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderStatus;
import hgyw.com.bookshare.entities.Supplier;

/**
 * Created by haim7 on 26/03/2016.
 */
public class SupplierAccessImpl extends GeneralAccessImpl implements SupplierAccess {

    private final Supplier currentUser;

    public SupplierAccessImpl(DataAccess crud, Supplier currentUser) {
        super(crud, currentUser);
        this.currentUser = currentUser;
    }

    @Override
    public Collection<BookSupplier> retrieveMyBooks() {
        return crud.findEntityReferTo(BookSupplier.class, currentUser);
    }

    @Override
    public void addBook(Book book) {
        crud.createEntity(book);
    }

    @Override
    public void updateBook(Book book) {
        crud.updateEntity(book);
    }

    @Override
    public void removeBook(Book book) {
        crud.deleteEntity(book);
    }

    @Override
    public Supplier retrieveSupplierDetails() {
        return retrieveUserDetails(currentUser);
    }

    @Override
    public void updateSupplierDetails(Supplier newDetails) {
        updateUserDetails(currentUser, newDetails);
    }

    @Override
    public Collection<Order> retrieveOrders(Date fromDate, Date toDate) {
        return crud.retrieveOrders(null, currentUser, fromDate, toDate, false);
    }

    @Override
    public Collection<Order> retrieveActiveOrders(Date fromDate, Date toDate) {
        return crud.retrieveOrders(null, currentUser, fromDate, toDate, true);
    }

    @Override
    public void updateOrderStatus(long orderId, OrderStatus orderStatus) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        OrderStatus currentOrderStatus = order.getOrderStatus();
        requireItsMeForAccess(order.getBookSupplier().getSupplier());
        // the WAITING_FOR_CANCEL can set only by customer request.
        if (orderStatus == OrderStatus.WAITING_FOR_CANCEL) {
            throw new IllegalStateException("Supplier cannot set order state to waiting-for-cancel.");
        }
        // don't allow cance without customer request before.
        if (orderStatus == OrderStatus.CANCELED && currentOrderStatus != OrderStatus.WAITING_FOR_CANCEL) {
            throw new IllegalStateException("You cannot cancel non-waiting-for-cancel order");
        }
        order.setOrderStatus(orderStatus);
        crud.updateEntity(order);
    }
}

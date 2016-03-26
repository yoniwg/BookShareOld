package hgyw.com.bookshare.logicAccess;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import hgyw.com.bookshare.crud.ExpandedCrud;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderStatus;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 26/03/2016.
 */
public class SupplierAccessImpl extends GeneralAccessImpl implements SupplierAccess {

    public SupplierAccessImpl(ExpandedCrud crud, Supplier currentUser) {
        super(crud, currentUser);
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
        return (Supplier) currentUser;
    }

    @Override
    public void updateSupplierDetails(Supplier newDetails) {
        requireItsMeForAccess(newDetails);
        newDetails.setCredentials(retrieveSupplierDetails().getCredentials()); // Avoid change credentials by this method.
        crud.updateEntity(newDetails);
    }

    @Override
    public Collection<Order> retrieveOrders(Date fromDate, Date toDate) {
        return crud.retrieveOrders(null, (Supplier) currentUser, fromDate, toDate, false);
    }

    @Override
    public Collection<Order> retrieveOpenOrders(Date fromDate, Date toDate) {
        return crud.retrieveOrders(null, (Supplier) currentUser, fromDate, toDate, true);
    }

    @Override
    public void updateOrderStatus(long orderId, OrderStatus orderStatus) {
        Order order = crud.retrieveEntity(Order.class, orderId);
        requireItsMeForAccess(order.getSupplier());
        order.setOrderStatus(orderStatus);
        crud.updateEntity(order);
    }
}

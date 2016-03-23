package hgyw.com.bookshare.entities;

import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Order extends Entity {

    private Customer customer;
    private Supplier supplier;
    private List<OrderedBook> booksList = new ArrayList<>();
    OrderRating orderRating;
    private Date date = new Date();
    private boolean open;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    BigDecimal calcTotalPrice() {
        return Stream.of(booksList).map(OrderedBook::calcTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Entity clone() {
        Order newObject = (Order) super.clone();
        newObject.booksList = new ArrayList(this.booksList);
        return newObject;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public List<OrderedBook> getBooksList() {
        return booksList;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public OrderRating getOrderRating() {
        return orderRating;
    }

    public void setOrderRating(OrderRating orderRating) {
        this.orderRating = orderRating;
    }
}

package hgyw.com.bookshare.entities;

import java.math.BigDecimal;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Order extends Entity {
    private BookSupplier bookSupplier;
    private int amount = 1;
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private OrderRating orderRating = new OrderRating();
    private OrderStatus orderStatus = OrderStatus.NEW;
    private Transaction transaction;

    
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void computePriceByBookSupplier() {
        unitPrice = bookSupplier.getPrice();
    }

    public BigDecimal calcTotalPrice() {
        return getUnitPrice().multiply(BigDecimal.valueOf(getAmount()));
    }

    public BookSupplier getBookSupplier() {
        return bookSupplier;
    }

    public void setBookSupplier(BookSupplier bookSupplier) {
        this.bookSupplier = bookSupplier;
    }

    public OrderRating getOrderRating() {
        return orderRating;
    }

    public void setOrderRating(OrderRating orderRating) {
        this.orderRating = orderRating;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

}

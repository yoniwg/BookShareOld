package hgyw.com.bookshare.entities;

import java.math.BigDecimal;

/**
 * Created by Yoni on 3/15/2016.
 */
public class OrderedBook extends Entity{
    private BookSupplier bookSupplier;
    private int amount = 1;
    private BigDecimal unitPrice = BigDecimal.ZERO;

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
}

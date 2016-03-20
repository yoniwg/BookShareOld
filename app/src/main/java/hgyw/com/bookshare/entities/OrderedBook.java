package hgyw.com.bookshare.entities;

import java.math.BigDecimal;

/**
 * Created by Yoni on 3/15/2016.
 */
public class OrderedBook extends Entity{
    private Book book;
    private int amount;
    private BigDecimal unitPrice;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

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

    public BigDecimal calcTotalPrice() {
        return getUnitPrice().multiply(BigDecimal.valueOf(getAmount()));
    }
}

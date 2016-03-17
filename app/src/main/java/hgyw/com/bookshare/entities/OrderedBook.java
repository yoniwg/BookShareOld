package hgyw.com.bookshare.entities;

import java.math.BigDecimal;

/**
 * Created by Yoni on 3/15/2016.
 */
public class OrderedBook {
    private Book book;
    private int amount;
    private BigDecimal price;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

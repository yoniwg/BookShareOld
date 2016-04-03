package hgyw.com.bookshare.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Yoni on 3/15/2016.
 */
public class BookSupplier extends Entity {

    private Supplier supplier;
    private Book book;
    private BigDecimal price = BigDecimal.ZERO;
    private int amountAvailable = 100;


    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(3, RoundingMode.HALF_UP);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}

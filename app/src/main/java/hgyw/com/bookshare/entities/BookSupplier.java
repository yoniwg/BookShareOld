package hgyw.com.bookshare.entities;

import java.math.BigDecimal;

/**
 * Created by Yoni on 3/15/2016.
 */
public class BookSupplier extends Entity {
    private Supplier supplier;
    private Book book;
    private BigDecimal price;
    private int amount;
}

package hgyw.com.bookshare.entities;

import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Order extends Entity {

    Customer customer;
    Supplier supplier;
    List<OrderedBook> booksList = new ArrayList<>();

    BigDecimal calcTotalPrice() {
        return Stream.of(booksList).map(OrderedBook::calcTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

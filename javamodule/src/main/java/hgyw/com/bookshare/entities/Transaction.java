package hgyw.com.bookshare.entities;

import java.util.Date;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Transaction extends Entity {

    private Customer customer;

    private Date date = new Date();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

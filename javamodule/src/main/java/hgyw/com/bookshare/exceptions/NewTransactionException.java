package hgyw.com.bookshare.exceptions;

import hgyw.com.bookshare.entities.Order;

/**
 * Created by Yoni on 3/27/2016.
 */
public class NewTransactionException extends Exception {

    private final Issue issue;
    private final Order order;

    public enum Issue {
        PRICE_NOT_MATCH, NOT_AVAILABLE
    }


    public NewTransactionException(Issue issue, Order order) {
        super(issue.toString());
        this.issue = issue;
        this.order = order;
    }

    public Issue getIssue() {
        return issue;
    }

    public Order getOrder() {
        return order;
    }
}

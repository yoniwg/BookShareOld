package hgyw.com.bookshare.entities;

/**
 * Created by haim7 on 26/03/2016.
 */
public class OrderStatus {

    private boolean sent;

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "sent=" + sent +
                '}';
    }
}

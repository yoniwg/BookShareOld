package hgyw.com.bookshare.entities;

/**
 * Created by Yoni on 3/20/2016.
 */
public class OrderRating {

    public enum Rating {EMPTY ,POOR, BAD, MEDIUM, GOOD, EXCELLENT}

    Rating itemAsDescribed = Rating.EMPTY;
    Rating shippingTime = Rating.EMPTY;
    Rating communication = Rating.EMPTY;

    public Rating getItemAsDescribed() {
        return itemAsDescribed;
    }

    public void setItemAsDescribed(Rating itemAsDescribed) {
        this.itemAsDescribed = itemAsDescribed;
    }

    public Rating getShippingTime() {
        return shippingTime;
    }

    public void setShippingTime(Rating shippingTime) {
        this.shippingTime = shippingTime;
    }

    public Rating getCommunication() {
        return communication;
    }

    public void setCommunication(Rating communication) {
        this.communication = communication;
    }
}

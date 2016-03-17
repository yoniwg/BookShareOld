package hgyw.com.bookshare.entities;

/**
 * Created by Yoni on 3/15/2016.
 */
public abstract class Entity {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

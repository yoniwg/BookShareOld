package hgyw.com.bookshare.entities;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Book extends Entity {
    private String title;
    private String bookAbstract;
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBookAbstract() {
        return bookAbstract;
    }

    public void setBookAbstract(String bookAbstract) {
        this.bookAbstract = bookAbstract;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

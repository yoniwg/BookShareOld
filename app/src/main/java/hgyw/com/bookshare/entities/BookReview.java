package hgyw.com.bookshare.entities;

/**
 * Created by Yoni on 3/15/2016.
 */
public class BookReview extends Entity{
    private User reviewer;
    private Book book;
    private int rating;
    private String title;
    private String description;
}

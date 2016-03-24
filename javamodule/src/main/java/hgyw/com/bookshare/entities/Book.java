package hgyw.com.bookshare.entities;

/**
 * Created by Yoni on 3/15/2016.
 */
public class Book extends Entity {

    public enum Genre {ACTION, ROMANCE, SCIENCE, SCIENCE_FICTION,
        DRAMA, SATIRE, CHILDREN, COMICS, BIOGRAPHIES, FANTASY, HEALTH}


    private String title;
    private String bookAbstract;
    private String author;
    private Genre genre;
    private ImageEntity image;

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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
    }

}

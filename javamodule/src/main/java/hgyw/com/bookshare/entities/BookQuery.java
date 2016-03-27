package hgyw.com.bookshare.entities;

import com.annimon.stream.function.Predicate;

import java.math.BigDecimal;

import hgyw.com.bookshare.Auxiliaries.Auxiliaries;

/**
 * Created by Yoni on 3/15/2016.
 */
public class BookQuery implements Predicate<BookSupplier>{


    private String titleQuery = "";

    private String authorQuery = "";

    private Book.Genre genreQuery = null;

    private BigDecimal beginPrice = null;
    private BigDecimal endPrice = null;

    public String getTitleQuery() {
        return titleQuery;
    }

    public void setTitleQuery(String titleQuery) {
        this.titleQuery = titleQuery;
    }

    public String getAuthorQuery() {
        return authorQuery;
    }

    public void setAuthorQuery(String authorQuery) {
        this.authorQuery = authorQuery;
    }

    public Book.Genre getGenreQuery() {
        return genreQuery;
    }

    public void setGenreQuery(Book.Genre genreQuery) {
        this.genreQuery = genreQuery;
    }

    public BigDecimal getBeginPrice() {
        return beginPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setPriceBounds(BigDecimal begin, BigDecimal end){
        beginPrice = begin;
        endPrice = end;
    }


    @Override
    public boolean test(BookSupplier bookSupplier) {
        Book book = bookSupplier.getBook();
        BigDecimal price = bookSupplier.getPrice();
        return book.getTitle().toLowerCase().contains(getTitleQuery().toLowerCase())
                && book.getAuthor().toLowerCase().contains(getAuthorQuery().toLowerCase())
                && (genreQuery == null || book.getGenre() == genreQuery)
                && Auxiliaries.isBetween(price, beginPrice, endPrice);
    }
}

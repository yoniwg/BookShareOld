package hgyw.com.bookshare.entities;

import com.annimon.stream.function.Predicate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Yoni on 3/15/2016.
 */
public class BookQuery implements Predicate<Entity>{

    private String beginTitle;
    private String endTitle;

    private String beginAuthor;
    private String endAuthor;

    private BigDecimal beginPrice;
    private BigDecimal endPrice;

    public void setTitleBounds(String begin, String end){
        resetAll();
        beginTitle = begin;
        endTitle = end;
    }

    public void setAuthorBounds(String begin, String end){
        resetAll();
        beginAuthor = begin;
        endAuthor = end;
    }

    public void setTitleBounds(BigDecimal begin, BigDecimal end){
        resetAll();
        beginPrice = begin;
        endPrice = end;
    }

    private void resetAll() {
        beginTitle = null;
        endTitle = null;
        beginAuthor = null;
        endAuthor = null;
        beginPrice = null;
        endPrice = null;
    }

    public String getBeginTitle() {
        return beginTitle;
    }

    public String getEndTitle() {
        return endTitle;
    }

    public String getBeginAuthor() {
        return beginAuthor;
    }

    public String getEndAuthor() {
        return endAuthor;
    }

    public BigDecimal getBeginPrice() {
        return beginPrice;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    @Override
    public boolean test(Entity entity) {
        if ( ! (entity instanceof Book)){
            throw new IllegalArgumentException("BookQuery is a predicate for Book only");
        }
        Book book = (Book) entity;
        if (beginTitle != null && endTitle != null){
            return book.getTitle().compareTo(beginTitle) > 0
                    && book.getTitle().compareTo(endTitle) <0;
        }
        if (beginAuthor != null && endAuthor != null){
            return book.getAuthor().compareTo(beginAuthor) > 0
                    && book.getAuthor().compareTo(endAuthor) <0;
        }
        // TODO: add price query

        throw new IllegalArgumentException("No one of the queries are full");
    }
}

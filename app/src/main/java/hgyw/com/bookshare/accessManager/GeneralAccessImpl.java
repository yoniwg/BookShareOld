package hgyw.com.bookshare.accessManager;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.backend.Backend;
import hgyw.com.bookshare.backend.BackendFactory;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderedBook;
import hgyw.com.bookshare.entities.User;

/**
 * Created by Yoni on 3/18/2016.
 */
public class GeneralAccessImpl implements GeneralAccess {

    private Backend backend = BackendFactory.getInstance();

    User currentUser;
    public GeneralAccessImpl(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public List<BookSupplier> findBooks(BookQuery query) {
        return backend.getStream(BookSupplier.class).filter(query).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findSpecialOffers() {
        final int TOP_NUMBER = 3;
        Stream<BookSupplier> listOfBooks = getDistinctBooksOfUser();
        List<String> top3Authors = getTopInstances(listOfBooks.map(bs -> bs.getBook().getAuthor()), TOP_NUMBER);
        List<Book.Genre> top3Genre = getTopInstances(listOfBooks.map(bs -> bs.getBook().getGenre()), TOP_NUMBER);
        //find books from top TOP_NUMBER authors and genres
        Stream<BookSupplier> specialOffers = Stream.of(new ArrayList());
        for (int i = 0; i < TOP_NUMBER; i++){
            BookQuery authorBQ = new BookQuery();
            authorBQ.setAuthorQuery(top3Authors.get(i));
            specialOffers = Stream.concat(specialOffers, Stream.of(findBooks(authorBQ)));
            BookQuery genreBQ = new BookQuery();
            genreBQ.setGenreQuery(top3Genre.get(i));
            specialOffers = Stream.concat(specialOffers, Stream.of(findBooks(genreBQ)));
        }
        return specialOffers.distinct().collect(Collectors.toList());
    }

    private Stream<BookSupplier> getDistinctBooksOfUser() {
        Stream<List<OrderedBook>> listsOfOrderedBooks = backend.getStream(Order.class).filter(o -> o.getCustomer() == currentUser).map(Order::getBooksList);
        Stream<Book> StreamOfBooks = Stream.of(new ArrayList<Book>());
        for (List<OrderedBook> lob: listsOfOrderedBooks.collect(Collectors.toList())){
            StreamOfBooks = Stream.concat(StreamOfBooks, Stream.of(lob).map(OrderedBook::getBook));
        }
        List<Book> listOfBooks = StreamOfBooks.distinct().collect(Collectors.toList());
        return backend.getStream(BookSupplier.class).filter(bs -> listOfBooks.contains(bs.getBook()));
    }

    private <T> List<T> getTopInstances(Stream<T> stream, int amount){
        Map<T, Integer> map = new HashMap();
        for (T t: stream.collect(Collectors.toList())) {
            Integer oldValue = map.get(t);
            map.put(t, (oldValue == null) ? 1 : oldValue + 1);
        }
        return Stream.of(map)
                .sortBy(e -> e.getValue())
                .map(e -> e.getKey()).collect(Collectors.toList())
                .subList(0, amount);
    }

    @Override
    public List<BookReview> getBookReviews(Book book) {
        return null;
    }

    @Override
    public User getUser() {
        return null;
    }
}

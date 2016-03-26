package hgyw.com.bookshare.crud;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.Auxiliaries.Auxiliaries;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderedBook;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

/**
 * Created by haim7 on 23/03/2016.
 */
class ExpandedCrudImpl extends ListsCrudImpl implements ExpandedCrud {

    @Override
    public User retrieveUserWithCredentials(Credentials credentials) {
        return streamAllUsers().filter(u -> u.getCredentials().equals(credentials))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return streamAllUsers()
                .anyMatch(u -> u.getCredentials().getUsername().equals(username));
    }

    private Stream<User> streamAllUsers() {
        return Stream.concat(streamAll(Customer.class), streamAll(Supplier.class));
    }

    @Override
    public Collection<Customer> findInterestedInBook(Book book, User userAsked) {
        return streamAll(Order.class)
                .filter(o -> Stream.of(o.getOrderedBooks())
                        .anyMatch(ob -> ob.getBookSupplier().getBook().equals(book)))
                .map(Order::getCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Order> retrieveOrders(Customer customer, Supplier supplier, Date fromDate, Date toDate, boolean onlyOpen) {
        return streamAll(Order.class)
                .filter(o -> (customer==null || o.getCustomer().equals(customer))
                                && (supplier==null || o.getSupplier().equals(supplier))
                                && Auxiliaries.isBetween(o.getDate(), fromDate, toDate)
                                && (!onlyOpen || o.isOpen())
                ).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findBooks(BookQuery query) {
        return streamAll(BookSupplier.class).filter(query).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findSpecialOffers(User user) {
        final int TOP_NUMBER = 3;
        Stream<BookSupplier> listOfBooks = getDistinctBooksOfUser(user);
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

    private Stream<BookSupplier> getDistinctBooksOfUser(User currentUser) {
        Stream<List<OrderedBook>> listsOfOrderedBooks = streamAll(Order.class).filter(o -> o.getCustomer() == currentUser).map(Order::getOrderedBooks);
        Stream<Book> StreamOfBooks = Stream.of(new ArrayList<Book>());
        for (List<OrderedBook> lob: listsOfOrderedBooks.collect(Collectors.toList())){
            StreamOfBooks = Stream.concat(StreamOfBooks, Stream.of(lob).map(OrderedBook::getBookSupplier).map(BookSupplier::getBook));
        }
        List<Book> listOfBooks = StreamOfBooks.distinct().collect(Collectors.toList());
        return streamAll(BookSupplier.class).filter(bs -> listOfBooks.contains(bs.getBook()));
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


}

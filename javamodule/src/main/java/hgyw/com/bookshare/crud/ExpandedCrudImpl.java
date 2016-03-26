package hgyw.com.bookshare.crud;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

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
import hgyw.com.bookshare.entities.OrderStatus;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.Transaction;
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
                .filter(o -> o.getBookSupplier().getBook().equals(book))
                .map(Order::getTransaction)
                .map(Transaction::getCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Order> retrieveOrders(Customer customer, Supplier supplier, Date fromDate, Date toDate, boolean onlyOpen) {
        return streamAll(Order.class)
                .filter(o -> (customer == null || o.getTransaction().getCustomer().equals(customer))
                        && (supplier == null || o.getBookSupplier().equals(supplier))
                        && Auxiliaries.isBetween(o.getTransaction().getDate(), fromDate, toDate)
                        && (!onlyOpen || o.getOrderStatus() != OrderStatus.CLOSED)
                ).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findBooks(BookQuery query) {
        return streamAll(BookSupplier.class).filter(query).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findSpecialOffers(User user, int limit) {
        Stream<BookSupplier> listOfBooks = getDistinctBooksOfUser(user);
        List<String> topAuthors = getTopInstances(listOfBooks.map(bs -> bs.getBook().getAuthor()), limit);
        List<Book.Genre> topGenre = getTopInstances(listOfBooks.map(bs -> bs.getBook().getGenre()), limit);
        //find books from top authors and genres
        return streamAll(BookSupplier.class)
                //sort by author or genre - give high priority to author & genre fitness
                .sortBy(bs ->
                        - (topAuthors.contains(bs.getBook().getAuthor()) ? 1 : 0)
                        - (topGenre.contains(bs.getBook().getGenre()) ? 1 : 0))
                .limit(limit)
                .distinct()
                .collect(Collectors.toList());
    }

    private Stream<BookSupplier> getDistinctBooksOfUser(User currentUser) {
        return streamAll(Order.class)
                .filter(o -> o.getTransaction().getCustomer().equals(currentUser))
                .map(Order::getBookSupplier)
                .distinct();
    }

    private <T> List<T> getTopInstances(Stream<T> stream, int amount){
        Map<T, Integer> map = new HashMap<>();
        for (T t: stream.collect(Collectors.toList())) {
            Integer oldValue = map.get(t);
            map.put(t, (oldValue == null) ? 1 : oldValue + 1);
        }
        return Stream.of(map)
                .sortBy(Map.Entry::getValue)
                .map(Map.Entry::getKey).limit(amount).collect(Collectors.toList());
    }


}

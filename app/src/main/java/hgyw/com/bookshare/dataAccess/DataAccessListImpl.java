package hgyw.com.bookshare.dataAccess;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hgyw.com.bookshare.auxiliaries.Auxiliaries;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.IdReference;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.Transaction;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.reflection.EntityReflection;

/**
 * Created by haim7 on 23/03/2016.
 */
class DataAccessListImpl extends ListsCrudImpl implements DataAccess {

    @Override
    public Optional<User> retrieveUserWithCredentials(Credentials credentials) {
        return streamAllUsers().filter(u -> u.getCredentials().equals(credentials))
                .findFirst();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return streamAllUsers()
                .anyMatch(u -> u.getCredentials().getUsername().equals(username));
    }

    private Stream<User> streamAllUsers() {
        return Stream.concat(streamAllNonDeleted(Customer.class), streamAllNonDeleted(Supplier.class));
    }

    @Override
    public Collection<Customer> findInterestedInBook(Book book, User userAsked) {
        return streamAllNonDeleted(Order.class)
                .filter(o -> retrieve(BookSupplier.class, o.getBookSupplierId()).getBookId() == book.getId())
                .map(o -> retrieve(Transaction.class, o.getTransactionId()))
                .map(t -> retrieve(Customer.class, t.getCustomerId()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Order> retrieveOrders(Customer customer, Supplier supplier, Date fromDate, Date toDate, boolean onlyOpen) {
        return streamAllNonDeleted(Order.class)
                .filter(o -> (customer == null || retrieve(Transaction.class, o.getTransactionId()).getCustomerId() == customer.getId())
                                && (supplier == null || o.getBookSupplierId() == supplier.getId())
                                && Auxiliaries.isBetween(retrieve(Transaction.class, o.getTransactionId()).getDate(), fromDate, toDate)
                                && (!onlyOpen || o.getOrderStatus().isActive())
                ).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findBooks(BookQuery query) {
        return streamAllNonDeleted(BookSupplier.class).filter(bs -> performFilterQuery(bs, query)).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findSpecialOffers(User user, int limit) {
        List<String> topAuthors = getTopInstances(getDistinctBooksOfUser(user).map(bs -> retrieve(Book.class, bs.getBookId()).getAuthor()), limit);
        List<Book.Genre> topGenre = getTopInstances(getDistinctBooksOfUser(user).map(bs -> retrieve(Book.class, bs.getBookId()).getGenre()), limit);
        //find books from top authors and genres
        // give high priority to author & genre fitness
        Function<Book, Integer> rateValueOfBook = book -> -(
                (topAuthors.contains(book.getAuthor()) ? 1 : 0)
                        + (topGenre.contains(book.getGenre()) ? 1 : 0)
        );
        return streamAllNonDeleted(BookSupplier.class)
                .sortBy(bs -> rateValueOfBook.apply(retrieve(Book.class, bs.getBookId())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Stream<BookSupplier> getDistinctBooksOfUser(User currentUser) {
        return streamAllNonDeleted(Order.class)
                .filter(o -> retrieve(Transaction.class, o.getTransactionId()).getCustomerId() == currentUser.getId())
                .map(o -> retrieve(BookSupplier.class, o.getBookSupplierId()))
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


    @Override
    public <T extends Entity> Collection<T> findEntityReferTo(Class<T> referringClass, IdReference ... referredItems) {
        // TODO CHECKING!
        Predicate<T> predicate = EntityReflection.predicateEntityReferTo(referringClass, referredItems);
        return streamAllNonDeleted(referringClass).filter(predicate).collect(Collectors.toList());
    }

    public <T extends Entity> Stream<T> streamAllNonDeleted(Class<? extends T> entityType) {
        return super.streamAll(entityType).filter(e -> !e.isDeleted());
    }

    private boolean performFilterQuery(BookSupplier bookSupplier, BookQuery bookQuery) {
        Book book = retrieve(Book.class, bookSupplier.getBookId());
        BigDecimal price = bookSupplier.getPrice();
        return book.getTitle().toLowerCase().contains(bookQuery.getTitleQuery().toLowerCase())
                && book.getAuthor().toLowerCase().contains(bookQuery.getAuthorQuery().toLowerCase())
                && (bookQuery.getGenreQuery() == null || book.getGenre() == bookQuery.getGenreQuery())
                && Auxiliaries.isBetween(price, bookQuery.getBeginPrice(), bookQuery.getEndPrice());
    }
}

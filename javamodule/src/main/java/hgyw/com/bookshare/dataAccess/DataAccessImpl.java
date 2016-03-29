package hgyw.com.bookshare.dataAccess;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import hgyw.com.bookshare.Auxiliaries.Auxiliaries;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.Transaction;
import hgyw.com.bookshare.entities.User;
import hgyw.com.bookshare.entities.reflection.Property;
import hgyw.com.bookshare.entities.reflection.PropertiesReflection;

/**
 * Created by haim7 on 23/03/2016.
 */
class DataAccessImpl extends ListsCrudImpl implements DataAccess {

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
                .filter(o -> o.getBookSupplier().getBook().equals(book))
                .map(Order::getTransaction)
                .map(Transaction::getCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Order> retrieveOrders(Customer customer, Supplier supplier, Date fromDate, Date toDate, boolean onlyOpen) {
        return streamAllNonDeleted(Order.class)
                .filter(o -> (customer == null || o.getTransaction().getCustomer().equals(customer))
                                && (supplier == null || o.getBookSupplier().equals(supplier))
                                && Auxiliaries.isBetween(o.getTransaction().getDate(), fromDate, toDate)
                                && (!onlyOpen || o.getOrderStatus().isActive())
                ).collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findBooks(BookQuery query) {
        return streamAllNonDeleted(BookSupplier.class)
                .filter(bs -> performFilterQuery(bs, query))
                //.sortBy(bs -> performSortQuery(bs, query))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookSupplier> findSpecialOffers(User user, int limit) {
        List<String> topAuthors = getTopInstances(getDistinctBooksOfUser(user).map(bs -> bs.getBook().getAuthor()), limit);
        List<Book.Genre> topGenre = getTopInstances(getDistinctBooksOfUser(user).map(bs -> bs.getBook().getGenre()), limit);
        //find books from top authors and genres
        // give high priority to author & genre fitness
        Function<BookSupplier, Integer> rateValueFoo = bs -> -(
                        (topAuthors.contains(bs.getBook().getAuthor()) ? 1 : 0)
                        + (topGenre.contains(bs.getBook().getGenre()) ? 1 : 0)
        );
        return streamAllNonDeleted(BookSupplier.class)
                .sortBy(rateValueFoo)
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Stream<BookSupplier> getDistinctBooksOfUser(User currentUser) {
        return streamAllNonDeleted(Order.class)
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


    @Override
    public <T extends Entity> Collection<T> findEntityReferTo(Class<? extends T> referringClass, Entity ... referredItems) {
        for (Entity e : referredItems) {
            retrieveEntity(e); // Throw exceptions if not found
        }
        return streamAllNonDeleted(referringClass)
                .filter(e -> {
                    for (Method method : e.getClass().getMethods()) {
                        if (method.getName().startsWith("get")) {
                            for (Entity refItem : referredItems) {
                                if (method.getReturnType() == refItem.getClass()) {
                                    try {
                                        if (!method.invoke(e).equals(refItem)) return false;
                                    } catch (IllegalAccessException | InvocationTargetException e1) {
                                        // do nothing
                                    }
                                }
                            }
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

    }

    public <T extends Entity> Stream<T> streamAllNonDeleted(Class<? extends T> entityType) {
        return super.streamAll(entityType).filter(e -> !e.isDeleted());
    }


    private boolean performFilterQuery(BookSupplier bookSupplier, BookQuery bookQuery) {
        Book book = bookSupplier.getBook();
        BigDecimal price = bookSupplier.getPrice();
        return book.getTitle().toLowerCase().contains(bookQuery.getTitleQuery().toLowerCase())
                && book.getAuthor().toLowerCase().contains(bookQuery.getAuthorQuery().toLowerCase())
                && (bookQuery.getGenreQuery() == null || book.getGenre() == bookQuery.getGenreQuery())
                && Auxiliaries.isBetween(price, bookQuery.getBeginPrice(), bookQuery.getEndPrice());
    }

}

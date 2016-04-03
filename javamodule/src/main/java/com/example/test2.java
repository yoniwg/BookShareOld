package com.example;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import hgyw.com.bookshare.dataAccess.DataAccess;
import hgyw.com.bookshare.dataAccess.DataAccessFactory;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookQuery;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.Rating;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.Transaction;
import hgyw.com.bookshare.exceptions.OrdersTransactionException;
import hgyw.com.bookshare.exceptions.WrongLoginException;
import hgyw.com.bookshare.logicAccess.AccessManager;
import hgyw.com.bookshare.logicAccess.AccessManagerFactory;
import hgyw.com.bookshare.logicAccess.CustomerAccess;
import hgyw.com.bookshare.logicAccess.SupplierAccess;

public class test2 {

    public static void main(String[] args) {

      //  DataAccess da = DataAccessFactory.getInstance();

        AccessManager accessManager = AccessManagerFactory.getInstance();
        SupplierAccess sa = null;
        CustomerAccess ca = null;
        Book book = new Book();
        BookSupplier bookSupplier = new BookSupplier();
        BookQuery bookQuery;
        Customer customer = new Customer();

        Credentials firstSupplierCredentials = Credentials.create("firstSupplier", "sda34^fdgfd%");
        Credentials secondSupplierCredentials = Credentials.create("secondSupplier", "sda34^fdgfd%");

        /////////////////////////////
        // new supplier
        Supplier supplier = new Supplier();
        supplier.setId(0);
        supplier.setCredentials(firstSupplierCredentials);
        supplier.setFirstName("");
        supplier.setLastName("Feldhaime");
        supplier.setAddress("Israel");
        supplier.setEmail("admin@feldhaim.co.il");
        supplier.setPhoneNumber("03-4004004");
        try {
            accessManager.signUp(supplier);
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
        sa = accessManager.getSupplierAccess();

        book.setId(0);
        book.setTitle("The Fellowship of the Ring");
        book.setAuthor("J. R. R. Tolkien");
        sa.addBook(book);
        bookSupplier.setId(0);
        bookSupplier.setBook(book);
        bookSupplier.setPrice(new BigDecimal("49.99"));
        sa.addBookSupplier(bookSupplier);

        book.setId(0);
        book.setTitle("The Two Towers");
        book.setAuthor("J. R. R. Tolkien");
        sa.addBook(book);
        bookSupplier.setId(0);
        bookSupplier.setBook(book);
        bookSupplier.setPrice(new BigDecimal("89.99"));
        sa.addBookSupplier(bookSupplier);

        book.setId(0);
        book.setTitle("The Return of the King");
        book.setAuthor("J. R. R. Tolkien");
        sa.addBook(book);
        bookSupplier.setId(0);
        bookSupplier.setBook(book);
        bookSupplier.setPrice(new BigDecimal("79.99"));
        sa.addBookSupplier(bookSupplier);
        try {
            System.out.println("$$$Negative test: Trying to add again:");
            sa.addBookSupplier(bookSupplier);
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        //////////////////////////////
        accessManager.signOut();
        //////////////////////////////

        // new supplier

        supplier.setId(0);
        supplier.setCredentials(secondSupplierCredentials);
        supplier.setFirstName("");
        supplier.setLastName("Yefe-Nof");
        supplier.setAddress("Jerusalem, Israel");
        supplier.setEmail("admin@yefenof.co.il");
        supplier.setPhoneNumber("03-5005005");
        try {
            accessManager.signUp(supplier);
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
        sa = accessManager.getSupplierAccess();

        for (int i = 1; i <= 7; i++) {
            book.setId(0);
            book.setTitle("Harry potter " + i);
            book.setAuthor("J.K.Rowling");
            sa.addBook(book);
            bookSupplier.setId(0);
            bookSupplier.setBook(book);
            bookSupplier.setPrice(BigDecimal.valueOf(100 + 10 * i));
            sa.addBookSupplier(bookSupplier);
        }

        // add bookSupplier for another book
        bookQuery = new BookQuery();
        bookQuery.setTitleQuery("The Two Towers");
        book = sa.findBooks(bookQuery).iterator().next().getBook(); // get first book from books match the quarry
        bookSupplier.setId(0);
        bookSupplier.setBook(book);
        bookSupplier.setPrice(BigDecimal.valueOf(77.88));
        sa.addBookSupplier(bookSupplier);

        // retrieve all current harry potter books adn change the details of books
        bookQuery = new BookQuery();
        bookQuery.setTitleQuery("HARRY POTTER");
        for (BookSupplier bs : sa.findBooks(bookQuery)) {
            Book b = bs.getBook();
            b.setAuthor("Joanne Jo Rowling");
            sa.updateBook(b);
        }

        // remove bookSupplier
        bookQuery = new BookQuery();
        bookQuery.setTitleQuery("HARRY POTTER 7");
        bookSupplier = sa.findBooks(bookQuery).iterator().next();
        sa.removeBookSupplier(bookSupplier);
        try {
            System.out.println("$$$Negative test: Try to remove again:");
            sa.removeBookSupplier(bookSupplier);
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        //////////////////////////////
        accessManager.signOut();
        //////////////////////////////
        // resign as first supplier
        /////////////////////////////

        try {
            accessManager.signIn(firstSupplierCredentials);
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
        sa = accessManager.getSupplierAccess();
        supplier = sa.retrieveSupplierDetails();

        // retrieve books by query
        bookQuery = new BookQuery();
        bookQuery.setTitleQuery("HARRY POTTER");
        bookQuery.setPriceBounds(BigDecimal.valueOf(135), BigDecimal.valueOf(165));
        // add bookSupplier for these book and current supplier
        List<Book> booksFound = Stream.of(sa.findBooks(bookQuery)).map(BookSupplier::getBook).distinct().collect(Collectors.toList());
        System.out.println(" *** books found: "); booksFound.forEach(System.out::println);
        for (Book b : booksFound) {
            bookSupplier.setId(0);
            bookSupplier.setBook(b);
            bookSupplier.setSupplier(supplier);
            bookSupplier.setPrice(BigDecimal.valueOf(66.99));
            sa.addBookSupplier(bookSupplier);
        }

        // update supplier details
        supplier = sa.retrieveSupplierDetails();
        supplier.setPhoneNumber("03-3333333");
        sa.updateSupplierDetails(supplier);

        // retrive supplier books
        System.out.println(" *** Books of supplier: " + supplier);
        sa.retrieveMyBooks().forEach(System.out::println);

        bookSupplier = sa.retrieveMyBooks().iterator().next();
        bookSupplier.setPrice(BigDecimal.valueOf(1000));
        sa.updateBookSupplier(bookSupplier);

        //////////////////////////////
        accessManager.signOut();
        //////////////////////////////

        // new customer
        customer.setId(0);
        customer.setCredentials(Credentials.create("haim1", "12345"));
        customer.setEmail("haim763@gmail.com");
        customer.setFirstName("Haim");
        customer.setLastName("Greenstein");
        try {
            accessManager.signUp(customer);
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
        ca = accessManager.getCustomerAccess();

        // order query results
        bookQuery = new BookQuery();
        bookQuery.setAuthorQuery("Rowling");
        bookQuery.setPriceBounds(BigDecimal.valueOf(105), BigDecimal.valueOf(145));
        Collection<BookSupplier> booksForOrder = ca.findBooks(bookQuery);
        Collection<Order> orders = Stream.of(booksForOrder).map(bs -> {
            Order order = new Order();
            order.setBookSupplier(bs);
            order.computePriceByBookSupplier();
            return order;
        }).collect(Collectors.toList());
        Transaction transaction = new Transaction();
        transaction.setCreditCard("231972947817861868");
        try {
            ca.performNewTransaction(transaction, orders);
        } catch (OrdersTransactionException e) {
            e.printStackTrace();
        }

        //
        System.out.println(" *** findSpecialOffers:");
        ca.findSpecialOffers(100).forEach(System.out::println);

        // cancel order
        orders = ca.retrieveActiveOrders();
        ca.cancelOrder(orders.iterator().next().getId());
        try {
            System.out.println("$$$Negative test: ");
            ca.cancelOrder(orders.iterator().next().getId());
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        // order rating
        OrderRating orderRating = new OrderRating();
        orderRating.setCommunication(Rating.GOOD);
        orderRating.setItemAsDescribed(Rating.BAD);
        ca.updateOrderRating(orders.stream().skip(1).findFirst().get().getId(), orderRating);

        // book review
        BookReview bookReview = new BookReview();
        bookReview.setBook(book);
        bookReview.setTitle("Bad Book");
        bookReview.setDescription("The book is very very boring, and is not interesting at all.");
        ca.addBookReview(bookReview);
        bookReview.setRating(Rating.POOR);
        ca.updateBookReview(bookReview);
        try {
            System.out.println(" $$$ Negative test: ");
            ca.addBookReview(bookReview);
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        try {
            System.out.println(" $$$ Negative test: ");
            ca.removeBookReview(new BookReview());
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }

        System.out.println(" ** suppliers of book " + book.shortDescription() + ": ");
        ca.retrieveSuppliers(book).forEach(System.out::println);

        //////////////////////////////
        accessManager.signOut();
        //////////////////////////////

        // new customer
        customer.setId(0);
        customer.setCredentials(Credentials.create("yoni1", "54321"));
        customer.setEmail("yoni@gmail.com");
        customer.setFirstName("Yoni");
        customer.setLastName("Wiesberg");
        System.out.println(" ** username haim1: " + accessManager.isUserNameTaken("haim1"));
        System.out.println(" ** username yoni1: " + accessManager.isUserNameTaken("yoni1"));
        try {
            accessManager.signUp(customer);
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////////////
        printWholeDatabase(DataAccessFactory.getInstance());
    }


    private static void printWholeDatabase(DataAccess dataAccess) {
        final List<Class<? extends Entity>> classes = Arrays.asList(
                Customer.class, Supplier.class, Book.class,
                BookSupplier.class, Order.class, Transaction.class, BookReview.class

        );

        System.out.println("\n **************** Database Summury ***************");
        for (Class<? extends Entity> c : classes) System.out.println(
                " **** " + c.getSimpleName() + " List" + " **** " + "\n * "
                        + dataAccess.streamAll(c).map(Object::toString).collect(Collectors.joining("\n * "))
        );
    }

}

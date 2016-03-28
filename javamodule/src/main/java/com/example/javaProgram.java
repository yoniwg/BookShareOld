package com.example;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import hgyw.com.bookshare.exceptions.OrdersTransactionException;
import hgyw.com.bookshare.exceptions.WrongLoginException;
import hgyw.com.bookshare.logicAccess.*;
import hgyw.com.bookshare.dataAccess.*;
import hgyw.com.bookshare.entities.*;

public class javaProgram {

    private static DataAccess dataAccess;

    public static void main(String[] args) {

        long startTimeCount = System.currentTimeMillis();

        dataAccess = DataAccessFactory.getInstance();

        System.out.println("initializing TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        {
            System.out.println("*** Reflection methods example ***");
            Class<?> c = Book.class;
            System.out.println("M: "+ Stream.of(c.getMethods()).map(Member::getName).collect(Collectors.toList()));
            System.out.println("DM: " + Stream.of(c.getDeclaredMethods()).map(Member::getName).collect(Collectors.toList()));
            System.out.println("F: " + Stream.of(c.getFields()).map(Member::getName).collect(Collectors.toList()));
            System.out.println("DF: " + Stream.of(c.getDeclaredFields()).map(Member::getName).collect(Collectors.toList()));
            System.out.println("*****************************************************\n");
        }

        System.out.println("reflaction example TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        System.out.println("addData TOTAL-TIME = " + (- startTimeCount + (startTimeCount = System.currentTimeMillis())) );

        {
            System.out.println("\n *** Colning test ***");
            Order first, cloned;

            first = dataAccess.retrieveEntity(Order.class, 1);
            cloned = (Order) first.clone();
            System.out.println("First: " + first);
            System.out.println("Cloned: " + cloned);
            System.out.println("References equation of Order.getCustomer(): " + (first.getTransaction().getCustomer() == cloned.getTransaction().getCustomer()));
            System.out.println("But the items in orderedBooks list has the same references...");

            System.out.println("*****************************************************\n");
        }

        System.out.println("cloning TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        System.out.println("print TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        try {
            accessChecking();
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
    }

    private static void accessChecking() throws WrongLoginException {
        System.out.println("\n ******************** Access Checking ******************** ");

        AccessManager manager = AccessManagerFactory.getInstance();
        GeneralAccess access = manager.getGeneralAccess();
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("Book Reviews: " + access.getBookReviews(getRandomItem(Book.class)));

        Book book = getRandomItem(Book.class);//dataAccess.retrieveEntity(Book.class, 2);
        System.out.println("This is a book: " + book);

        try {
            System.out.println("$$$ findSpecialOffers: " + access.findSpecialOffers(5));
        } catch (Exception ex) { ex.printStackTrace(); }

        CustomerAccess cAccess;

        // Signing:

        System.out.println("\ntrying to access as customer.");
        try { cAccess = manager.getCustomerAccess();}
        catch (Exception e) { System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage()); }
        System.out.println("trying end.");
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("\ntrying to access with wrong username and password.");
        try { manager.signIn(Credentials.create("haim", "1234")); }
        catch (Exception e) { System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage()); }
        System.out.println("trying end.");
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("registering...");
        manager.signIn(Credentials.create("root", "1234"));
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("\ntrying to register again.");
        try { manager.signIn(Credentials.create("yaakov", "34")); }
        catch (Exception e) { System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage()); }
        System.out.println("trying end.");
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("\ntrying to signout.");
        try { manager.signOut(); }
        catch (Exception e) { System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage()); }
        System.out.println("trying end.");
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("\nregistering...");
        manager.signIn(Credentials.create("root", "1234"));
        System.out.println("current user type: " + manager.getCurrentUserType());

        // Customer access:

        System.out.println("\n*** Customer access check: ***");
        cAccess = manager.getCustomerAccess();

        Customer customer = cAccess.retrieveCustomerDetails();
        System.out.println("current Customer Details: " + customer);
        System.out.println("Customer Details Changing...");
        customer.setFirstName("new");
        customer.setLastName("name");
        customer.setCredentials(Credentials.create("new", "credentials"));
        cAccess.updateCustomerDetails(customer);
        System.out.println("current Customer Details: " + customer);

        System.out.println("Customer Reviews: " + cAccess.getCustomerReviews());
        System.out.println("Interested In Book: " + cAccess.findInterestedInBook(getRandomItem(Book.class)));

        System.out.println("** Book Review functions **");
        System.out.println("These are current book reviews: " + cAccess.getBookReviews(book));
        BookReview newReview = new BookReview();
        newReview.setCustomer(cAccess.retrieveCustomerDetails());
        newReview.setBook(book);
        newReview.setTitle("My first review");
        newReview.setDescription("Bla bla bla, bla bla, bla bla bla - bla bla - bla bla.");
        System.out.println("This review we are trying to add: " + newReview);
        cAccess.writeBookReview(newReview);
        System.out.println("These are current book reviews: " + cAccess.getBookReviews(book));
        System.out.println("Now we remove the review:");
        cAccess.removeBookReview(newReview);
        System.out.println("These are current book reviews: " + cAccess.getBookReviews(book));

        try {
            System.out.println("$$$ findSpecialOffers: " + cAccess.findSpecialOffers(5));
            System.out.println("$$$ findInterestedInBook: " + cAccess.findInterestedInBook(book));
        } catch (Exception ex) { ex.printStackTrace(); }

        System.out.println("\n** Now we have to check only order functions! **");

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        List<Order> orderList = new ArrayList<>();
        for (int i = 0; i < 10; i++ ) {
            Order order = new Order();
            order.setBookSupplier(getRandomItem(BookSupplier.class));
            order.computePriceByBookSupplier();
            orderList.add(order);
        }
        try {
            cAccess.performNewTransaction(transaction, orderList);
        } catch (OrdersTransactionException e) {
            e.printStackTrace();
        }

        System.out.println("Retrieve Orders: " + cAccess.retrieveOrders(null, null));
        Date now = new Date(), yesterday = new Date(now.getTime() - (long)(24*60*6*1000 * Math.random()));
        System.out.println("Retrieve Orders today: " + cAccess.retrieveOrders(yesterday, now));
        System.out.println("Retrieve Orders this moment: " + cAccess.retrieveOrders(now, now));
        System.out.println("Retrieve Orders open: " + cAccess.retrieveActiveOrders());


    }

    static <T extends Entity> T getRandomItem(Class<T> clazz) {
        long index = (long) (dataAccess.streamAll(clazz).count() * Math.random());
        return dataAccess.streamAll(clazz).skip(index).findFirst().orElseThrow(() -> new NoSuchElementException("TESTING MESSAGE: the stream of entity is empty"));
        //return dataAccess.retrieveEntity(clazz, 1);
    }

}

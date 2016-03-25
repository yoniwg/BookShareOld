package com.example;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import hgyw.com.bookshare.entities.reflection.Property;
import hgyw.com.bookshare.entities.reflection.ReflectionProperties;
import hgyw.com.bookshare.logicAccess.AccessManager;
import hgyw.com.bookshare.logicAccess.AccessManagerFactory;
import hgyw.com.bookshare.logicAccess.CustomerAccess;
import hgyw.com.bookshare.logicAccess.GeneralAccess;
import hgyw.com.bookshare.crud.Crud;
import hgyw.com.bookshare.crud.CrudFactory;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.Order;

public class javaProgram {

    private static Crud crud;

    public static void main(String[] args) {
        long startTimeCount = System.currentTimeMillis();

        crud = CrudFactory.getInstance();

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

            first = crud.retrieveEntity(Order.class, 1);
            cloned = (Order) first.clone();
            System.out.println("First: " + first);
            System.out.println("Cloned: " + cloned);
            System.out.println("References equation of Order.getCustomer(): " + (first.getCustomer() == cloned.getCustomer()));
            System.out.println("But the items in orderedBooks list has the same references...");

            System.out.println("*****************************************************\n");
        }

        System.out.println("cloning TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        System.out.println("print TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        accessChecking();
    }

    private static void accessChecking() {
        System.out.println("\n ******************** Access Checking ******************** ");

        AccessManager manager = AccessManagerFactory.getInstance();
        GeneralAccess access = manager.getGeneralAccess();
        System.out.println("current user type: " + manager.getCurrentUserType());

        System.out.println("Book Reviews: " + access.getBookReviews(getRandomItem(Book.class)));

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

        Customer customerDetails = cAccess.retrieveCustomerDetails();
        System.out.println("current Customer Details: " + customerDetails);
        System.out.println("Customer Details Changing...");
        customerDetails.setFirstName("new");
        customerDetails.setLastName("name");
        customerDetails.setCredentials(Credentials.create("new", "credentials"));
        cAccess.updateCustomerDetails(customerDetails);
        System.out.println("current Customer Details: " + customerDetails);

        System.out.println("Customer Reviews: " + cAccess.getCustomerReviews());
        System.out.println("Interested In Book: " + cAccess.findInterestedInBook(getRandomItem(Book.class)));

        System.out.println("** Book Review functions **");
        Book book = getRandomItem(Book.class);
        System.out.println("This is a book: " + book);
        System.out.println("These are current book reviews: " + cAccess.getBookReviews(book));
        BookReview newReview = new BookReview();
        newReview.setCustomer((Customer) cAccess.getCurrentUser());
        newReview.setBook(book);
        newReview.setTitle("My first review");
        newReview.setDescription("Bla bla bla, bla bla, bla bla bla - bla bla - bla bla.");
        System.out.println("This review we are trying to add: " + newReview);
        cAccess.writeBookReview(newReview);
        System.out.println("These are current book reviews: " + cAccess.getBookReviews(book));
        System.out.println("Now we remove the review:");
        cAccess.removeBookReview(newReview);
        System.out.println("These are current book reviews: " + cAccess.getBookReviews(book));

        System.out.println("\n** Now we have to check only order functions! **");

        System.out.println("Retrieve Orders: " + cAccess.retrieveOrders(null, null));
        Date now = new Date(), yesterday = new Date(now.getTime() - 24*60*60);
        System.out.println("Retrieve Orders today: " + cAccess.retrieveOrders(yesterday, now));
        System.out.println("Retrieve Orders this moment: " + cAccess.retrieveOrders(now, now));
        System.out.println("Retrieve Orders open: " + cAccess.retrieveOpenOrders());


    }

    static <T extends Entity> T getRandomItem(Class<T> clazz) {
        //long index = (long) (crud.streamAll(clazz).count() * Math.random());
        //return crud.streamAll(clazz).skip(index).findFirst().orElseThrow(() -> new NoSuchElementException("TESTING MESSAGE: the stream of entity is empty"));
        return crud.retrieveEntity(clazz, 1);
    }

}

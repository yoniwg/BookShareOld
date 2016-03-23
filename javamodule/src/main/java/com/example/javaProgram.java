package com.example;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.NoSuchElementException;

import hgyw.com.bookshare.logicAccess.AccessManager;
import hgyw.com.bookshare.logicAccess.AccessManagerImpl;
import hgyw.com.bookshare.logicAccess.CustomerAccess;
import hgyw.com.bookshare.logicAccess.GeneralAccess;
import hgyw.com.bookshare.crud.Crud;
import hgyw.com.bookshare.crud.CrudFactory;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.BookReview;
import hgyw.com.bookshare.entities.BookSupplier;
import hgyw.com.bookshare.entities.Credentials;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Entity;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;
import hgyw.com.bookshare.entities.OrderedBook;
import hgyw.com.bookshare.entities.Supplier;
import hgyw.com.bookshare.entities.User;

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

        addData();

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

        printWholeDatabase();

        System.out.println("print TOTAL-TIME = " + (-startTimeCount + (startTimeCount = System.currentTimeMillis())));

        accessChecking();
    }

    private static void accessChecking() {
        System.out.println("\n ******************** Access Checking ******************** ");

        AccessManager manager = AccessManagerImpl.INSTANCE;
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

        System.out.println("\n*** Customer access checing: ***");
        cAccess = manager.getCustomerAccess();
        System.out.println("User Reviews: " + cAccess.getCustomerReviews());
    }

    private static void printWholeDatabase() {
        final List<Class<? extends Entity>> classes = Arrays.asList(
                Customer.class, Supplier.class, Order.class, Book.class,
                BookSupplier.class, BookReview.class
        );

        System.out.println("\n **************** Database Summury ***************");
        for (Class<? extends Entity> c : classes) System.out.println(
                " **** " + c.getSimpleName() + " List" + " **** " + "\n * "
                        + crud.streamAll(c).map(Object::toString).collect(Collectors.joining("\n * "))
        );
    }

    static void addData() {

        // ****************************************************** //

        User user;
        user = new Customer();

        user.setId(0);
        user.setCredentials(Credentials.create("root", "1234"));
        user.setFirstName("Haim");
        user.setLastName("Greenstein");
        user.setAddress("Petach Tikve");
        user.setBirthday(java.sql.Date.valueOf("1992-3-23"));
        user.setEmail("haim123@gmail.com");
        user.setPhoneNumber("050-1234567");
        crud.createEntity(user);

        user.setId(0);
        user.setFirstName("Yoni");
        user.setLastName("Wiseberg");
        user.setAddress("Bney Brak");
        user.setBirthday(java.sql.Date.valueOf("1991-6-24"));
        user.setEmail("yw32@gmail.com");
        user.setPhoneNumber("052-1234567");
        crud.createEntity(user);

        user.setId(0);
        user.setFirstName("Israel");
        user.setLastName("Israeli");
        user.setAddress("Tel Aviv");
        user.setBirthday(java.sql.Date.valueOf("1976-12-3"));
        user.setEmail("dsfsf@walla.co.il");
        user.setPhoneNumber("03-6438326");
        crud.createEntity(user);

        user = new Supplier();

        user.setId(0);
        user.setFirstName("");
        user.setLastName("Feldhaime");
        user.setAddress("Israel");
        user.setEmail("admin@feldhaim.co.il");
        user.setPhoneNumber("03-4004004");
        crud.createEntity(user);

        user.setId(0);
        user.setFirstName("");
        user.setLastName("Yefe-Nof");
        user.setAddress("Jerusalem, Israel");
        user.setEmail("admin@yefenof.co.il");
        user.setPhoneNumber("03-5005005");
        crud.createEntity(user);

        // ****************************************************** //

        Book b = new Book();

        b.setId(0);
        b.setTitle("The Fellowship of the Ring");
        b.setAuthor("J. R. R. Tolkien");
        crud.createEntity(b);

        b.setId(0);
        b.setTitle("The Two Towers");
        b.setAuthor("J. R. R. Tolkien");
        crud.createEntity(b);

        b.setId(0);
        b.setTitle("The Return of the King");
        b.setAuthor("J. R. R. Tolkien");
        crud.createEntity(b);

        for (int i = 1; i <= 7; i++) {
            b.setId(0);
            b.setTitle("Hary potter " + i);
            b.setAuthor("J.K.Rolling");
            crud.createEntity(b);
        }

        // ****************************************************** //

        BookSupplier bs = new BookSupplier();
        BookReview br = new BookReview();

        long suppliersCount = crud.streamAll(Supplier.class).count();
        long booksCount = crud.streamAll(Book.class).count();
        for (int i = 1; i <= booksCount; i++) {
            // add BookSupplier's randomly
            final double probabilityThatSupplierHaveBook = 2.0 / suppliersCount;
            for (int j = 1; j <= suppliersCount; j++)
                if (Math.random() < probabilityThatSupplierHaveBook) {
                    bs.setId(0);
                    bs.setSupplier(crud.retrieveEntity(Supplier.class, j));
                    bs.setBook(crud.retrieveEntity(Book.class, i));
                    bs.setPrice(BigDecimal.valueOf(30 + Math.random() * 50).setScale(3, BigDecimal.ROUND_HALF_UP));
                    crud.createEntity(bs);
                }
            // add Review to each book
            for (int j =0; j < 3; j++) {
                br.setId(0);
                br.setBook(crud.retrieveEntity(Book.class, i));
                br.setTitle(generateRandomString(10));
                br.setDescription(generateRandomString(5));
                br.setRating((int) (Math.random() * 5));
                br.setUser(getRandomItem(Customer.class));
                crud.createEntity(br);
            }

        }

        // ****************************************************** //

        Order order = new Order();
        OrderedBook ob;

        order.setId(0);
        order.setDate(new GregorianCalendar(2016, Calendar.JUNE, 12).getTime());
        order.setOrderRating(new OrderRating());
        order.setCustomer(getRandomItem(Customer.class));
        ob = new OrderedBook();
        ob.setBookSupplier(getRandomItem(BookSupplier.class));
        order.getOrderedBooks().add(ob);
        ob = new OrderedBook();
        ob.setBookSupplier(getRandomItem(BookSupplier.class));
        order.getOrderedBooks().add(ob);
        for (OrderedBook orderedBook : order.getOrderedBooks()) orderedBook.computePriceByBookSupplier();
        crud.createEntity(order);

        // ****************************************************** //
        
    }


    /**
     * Retrieve random entity item
     * This should be an expensive method!
     */
    static <T extends Entity> T getRandomItem(Class<T> clazz) {
        long index = (long) (crud.streamAll(clazz).count() * Math.random());
        return crud.streamAll(clazz).skip(index).findFirst().orElseThrow(() -> new NoSuchElementException("TESTING MESSEGE: the stream of entity is empty"));
    }

    static String generateRandomString(int size) {
        StringBuilder str = new StringBuilder(size);
        boolean newWord = true;
        for (int i = 0; i < size; i++) {
            if (!newWord &&  Math.random() < 0.25) {
                str.append(' ');
                newWord = true;
            }
            else {
                char c = (char) ((int)(Math.random()*26) + (newWord ? 'A' : 'a'));
                str.append(c);
                newWord = false;
            }
        }
        return str.toString().trim();
    }
}

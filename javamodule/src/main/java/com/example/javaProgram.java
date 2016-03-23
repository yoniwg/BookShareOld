package com.example;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import hgyw.com.bookshare.accessManager.AccessManager;
import hgyw.com.bookshare.accessManager.GeneralAccess;
import hgyw.com.bookshare.backend.Crud;
import hgyw.com.bookshare.backend.CrudFactory;
import hgyw.com.bookshare.entities.Book;
import hgyw.com.bookshare.entities.Customer;
import hgyw.com.bookshare.entities.Order;
import hgyw.com.bookshare.entities.OrderRating;

public class javaProgram {

    public static void main(String[] args) {
        
        {
            System.out.println("*** Reflection method and fields retrieve methods ***");
            Class<?> c = Book.class;
            System.out.println("M: "+ Stream.of(c.getMethods()).map(m -> m.getName()).collect(Collectors.toList()));
            System.out.println("DM: " + Stream.of(c.getDeclaredMethods()).map(m -> m.getName()).collect(Collectors.toList()));
            System.out.println("F: " + Stream.of(c.getFields()).map(m -> m.getName()).collect(Collectors.toList()));
            System.out.println("DF: " + Stream.of(c.getDeclaredFields()).map(m -> m.getName()).collect(Collectors.toList()));
            System.out.println("*****************************************************\n");
        }
        
        AccessManager manager = AccessManager.INSTANCE;
        GeneralAccess access = manager.getGeneralAccess();
        Crud crud = CrudFactory.getInstance();

        ///////////////////////
        // Add Data
        ///////////////////////

        addData(crud);

        System.out.println("First book: " + crud.retrieveEntity(Customer.class, 1));
        System.out.println("Cloned book: " + crud.retrieveEntity(Customer.class, 2));

        //System.out.println("Cloned book: " + crud.retrieveEntity(Customer.class, 3));
        System.out.println(
                access.findSpecialOffers()
        );


        System.out.println("Hi!");
    }


    static void addData(Crud crud) {

        Customer customer = new Customer();
        customer.setFirstName("Haim");
        customer.setLastName("Greenstein");
        customer.setAddress("Petach Tikve");
        customer.setBirthday(java.sql.Date.valueOf("1992-3-23"));
        customer.setEmail("haim123@gmail.com");
        customer.setPhoneNumber("050-1234567");
        crud.createEntity(customer);

        customer.setId(0);
        customer.setFirstName("Yoni");
        customer.setLastName("Wiseberg");
        customer.setAddress("Bney Brak");
        customer.setBirthday(java.sql.Date.valueOf("1991-6-24"));
        customer.setEmail("yw32@gmail.com");
        customer.setPhoneNumber("052-1234567");
        crud.createEntity(customer);



        Order order = new Order();
        order.setId(3243242);
        order.setDate(new GregorianCalendar(2016, Calendar.JUNE, 12).getTime());
        order.setOrderRating(new OrderRating());

    }

}

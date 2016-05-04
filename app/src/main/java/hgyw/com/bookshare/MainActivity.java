package hgyw.com.bookshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import hgyw.com.bookshare.entities.Book;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Book book = new Book();

    }


}

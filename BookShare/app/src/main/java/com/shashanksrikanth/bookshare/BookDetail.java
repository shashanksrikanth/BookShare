package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class BookDetail extends AppCompatActivity {
    // This activity shows the details about books

    TextView bookDetailTitle;
    TextView bookDetailAuthor;
    TextView bookDetailDescription;
    TextView bookDetailPublisher;
    TextView bookDetailAverageRating;
    ImageView bookDetailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Declare instance variables
        bookDetailTitle = findViewById(R.id.bookDetailTitle);
        bookDetailAuthor = findViewById(R.id.bookDetailAuthor);
        bookDetailDescription = findViewById(R.id.bookDetailDescription);
        bookDetailPublisher = findViewById(R.id.bookDetailPublisher);
        bookDetailAverageRating = findViewById(R.id.bookDetailAverageRating);
        bookDetailImage = findViewById(R.id.bookDetailImage);

        // Unpack intent
        Intent intent = getIntent();
        BookItem book = (BookItem) intent.getSerializableExtra("BookItem");

        // Set texts
        if(!book.bookTitle.equals("NULL")) bookDetailTitle.setText(book.bookTitle);
        if(!book.bookAuthor.equals("NULL")) bookDetailAuthor.setText(book.bookAuthor);
        if(!book.bookDescription.equals("NULL")) bookDetailDescription.setText(book.bookDescription);
        if(!book.bookPublisher.equals("NULL")) bookDetailPublisher.setText(book.bookPublisher);
        if(book.bookAverageRating!=-1) bookDetailAverageRating.setText(Integer.toString(book.bookAverageRating));
        if(!book.bookImageLink.equals("NULL"))
            Picasso.get().load(book.bookImageLink).error(R.drawable.missing_book).placeholder(R.drawable.placeholder).into(bookDetailImage);
    }
}
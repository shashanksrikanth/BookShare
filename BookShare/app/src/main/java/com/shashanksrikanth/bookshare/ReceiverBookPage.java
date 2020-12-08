package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReceiverBookPage extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    // This class is for seeing the books that are up for donation in a list, and mass selecting them for requesting donations

    TextView listName;
    TextView listDescription;

    ArrayList<BookItem> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    BookItemAdapter adapter;

    FirebaseFirestore databaseReference;

    String listDatabaseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_book_page);

        // Initialize text views
        listName = findViewById(R.id.receiverBookPageListName);
        listDescription = findViewById(R.id.receiverBookPageListDescription);

        // Unpack intent and set the list name and description
        Intent intent = getIntent();
        ListItem item = (ListItem) intent.getSerializableExtra("ListItem");
        if(item!=null) {
            listName.setText(item.listName);
            listDescription.setText(item.listDescription);
            listDatabaseID = item.listDatabaseID;
        }

        // Get database reference
        databaseReference = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.receiverBookPageRecyclerView);
        adapter = new BookItemAdapter(bookList, null, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Populate bookList with the books from the database
        DocumentReference documentReference = databaseReference.collection("bookLists").document(listDatabaseID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> isbnList = documentSnapshot.toObject(ListItem.class).isbnList;
                for(String isbn : isbnList) {
                    ReceiverBookDownloader downloader = new ReceiverBookDownloader(isbn, ReceiverBookPage.this);
                    new Thread(downloader).start();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // Opens up the details about the book
        int position = recyclerView.getChildLayoutPosition(v);
        BookItem book = bookList.get(position);
        Intent intent = new Intent(this, BookDetail.class);
        intent.putExtra("BookItem", book);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    public void addBookToList(final BookItem item) {
        // Add a book to the list- isbn and book is sent from BookDownloader
        bookList.add(item);
        adapter.notifyDataSetChanged();
    }
}
package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiverBookPage extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    // This class is for seeing the books that are up for donation in a list, and mass selecting them for requesting donations

    TextView listName;
    TextView listDescription;

    ArrayList<BookItem> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    BookItemAdapter adapter;

    FirebaseFirestore databaseReference;
    String listDatabaseID;
    HashMap<String, String> selectedBooks = new HashMap<>();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu layout
        getMenuInflater().inflate(R.menu.receiver_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // What to do based on which button is selected in the menu
        switch (item.getItemId()) {
            case R.id.emailDonor:
                if(selectedBooks.keySet().size()!=0) sendEmail();
                else {
                    AlertDialog.Builder noticeBuilder = new AlertDialog.Builder(this);
                    noticeBuilder.setTitle("No Books Selected");
                    noticeBuilder.setMessage("One or more books must be selected to send an email.");
                    AlertDialog noticeDialog = noticeBuilder.create();
                    noticeDialog.show();
                }
                return true;
            case R.id.receiverBookDescription:
                AlertDialog.Builder definitionBuilder = new AlertDialog.Builder(this);
                definitionBuilder.setIcon(R.drawable.baseline_help_black_48);
                definitionBuilder.setTitle("How to request a donation?");
                definitionBuilder.setMessage("Long click on the books you want to request for donation. Then, press the " +
                        "email icon in the top, and send an email to the donor.");
                AlertDialog dialog = definitionBuilder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        // Item is selected for requesting donation
        int position = recyclerView.getChildLayoutPosition(v);
        BookItem book = bookList.get(position);
        if(!book.bookIsSelected) {
            v.setBackgroundResource(R.color.colorSelected);
            selectedBooks.put(book.bookISBN, book.bookTitle);
            book.changeStatusSelected(true);
        }
        else {
            v.setBackgroundResource(R.color.colorDarkCream);
            selectedBooks.remove(book.bookISBN);
            book.changeStatusSelected(false);
        }
        return true;
    }

    public void addBookToList(final BookItem item) {
        // Add a book to the list- isbn and book is sent from BookDownloader
        bookList.add(item);
        adapter.notifyDataSetChanged();
    }

    public void sendEmail() {
        // Helper function that sends an email
        final String[] uid = {""};
        databaseReference.collection("bookLists").document(listDatabaseID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    ListItem item = task.getResult().toObject(ListItem.class);
                    uid[0] = item.uid;
                }
                final String[] emailAddresses = {""};
                databaseReference.collection("users").document(uid[0]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    AppUser user = task.getResult().toObject(AppUser.class);
                                    emailAddresses[0] = user.email;
                                }
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String emailSubject = "Donation request!";
                                String emailText = "Hello there! \n" + "This is a donation request for the following books: \n";
                                int itemCount = 1;
                                for(String key : selectedBooks.keySet()) {
                                    emailText += itemCount + ". " + selectedBooks.get(key) + '\n';
                                    itemCount++;
                                }
                                emailText += "Please email back to set up donation schematics. \n";
                                emailText += "Have a great day!";
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.putExtra(Intent.EXTRA_EMAIL, emailAddresses);
                                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                                intent.putExtra(Intent.EXTRA_TEXT, emailText);
                                intent.setType("message/rfc822");
                                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                            }
                });
            }
        });
    }
}
package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class DonorBookPage extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    // The activity that shows the books the donor has up for donation- specific for each list of donations

    TextView listName;
    TextView listDescription;

    ArrayList<BookItem> bookList = new ArrayList<>();
    RecyclerView recyclerView;
    BookItemAdapter adapter;

    HashMap<String, BookItem> storedBooks = new HashMap<>();

    FirebaseFirestore databaseReference;

    String listDatabaseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_book_page);

        // Declare text views
        listName = findViewById(R.id.donorBookPageListName);
        listDescription = findViewById(R.id.donorBookPageListDescription);

        // Unpack intent and set the list name and description
        Intent intent = getIntent();
        ListItem item = (ListItem) intent.getSerializableExtra("ListItem");
        if(item!=null) {
            listName.setText(item.listName);
            listDescription.setText(item.listDescription);
            listDatabaseID = item.listDatabaseID;
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.donorBookPageRecyclerView);
        adapter = new BookItemAdapter(bookList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get database reference
        databaseReference = FirebaseFirestore.getInstance();

        // Populate bookList with the books from the database
        DocumentReference documentReference = databaseReference.collection("bookLists").document(listDatabaseID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> isbnList = documentSnapshot.toObject(ListItem.class).isbnList;
                for(String isbn : isbnList) {
                    BookDownloader downloader = new BookDownloader(isbn, DonorBookPage.this);
                    new Thread(downloader).start();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu layout
        getMenuInflater().inflate(R.menu.donor_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // What to do based on which button is selected in the menu
        switch(item.getItemId()) {
            case R.id.addDonorBook:
                AlertDialog.Builder addBuilder = new AlertDialog.Builder(this);
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setGravity(Gravity.CENTER_HORIZONTAL);
                addBuilder.setView(editText);
                addBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String isbn = editText.getText().toString().trim();
                        DocumentReference documentReference = databaseReference.collection("bookLists").document(listDatabaseID);
                        documentReference.update("isbnList", FieldValue.arrayUnion(isbn));
                        updateBookList(isbn, true);
                    }
                });
                addBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                addBuilder.setIcon(R.drawable.baseline_add_circle_black_48);
                addBuilder.setTitle("Enter ISBN");
                AlertDialog addDialog = addBuilder.create();
                addDialog.show();
                return true;
            case R.id.donorBookDescription:
                AlertDialog.Builder definitionBuilder = new AlertDialog.Builder(this);
                definitionBuilder.setIcon(R.drawable.baseline_help_black_48);
                definitionBuilder.setTitle("How to add a book?");
                definitionBuilder.setMessage("Click the Add button, then enter the ISBN. The book information will be retrieved using " +
                        "the Google Books API. If the API is unable to find the book, you will be notified. Your books will appear in " +
                        "this screen");
                AlertDialog dialog = definitionBuilder.create();
                dialog.show();
                return true;
        }
        return true;
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
        // Delete book from the list when long clicked, delete IsbnItem if necessary
        final int position = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookItem book = bookList.get(position);
                String isbn = book.bookISBN;
                storedBooks.remove(isbn);
                DocumentReference documentReference = databaseReference.collection("bookLists").document(listDatabaseID);
                documentReference.update("isbnList", FieldValue.arrayRemove(isbn));
                bookList.remove(position);
                adapter.notifyDataSetChanged();
                final DocumentReference documentReferenceIsbn =  databaseReference.collection("isbnLists").document(isbn);
                documentReferenceIsbn.update("listIds", FieldValue.arrayRemove(listDatabaseID));
                documentReferenceIsbn.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            IsbnItem isbnItem = task.getResult().toObject(IsbnItem.class);
                            if(isbnItem.listIds.size() == 0) documentReferenceIsbn.delete();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public void addBookToList(final BookItem item) {
        // Add a book to the list- isbn and book is sent from BookDownloader
        bookList.add(item);
        adapter.notifyDataSetChanged();
        storedBooks.put(item.bookISBN, item);
        DocumentReference documentReference = databaseReference.collection("isbnLists").document("allUniqueGenres");
        documentReference.update("uniqueGenres", FieldValue.arrayUnion(item.bookGenre));
        final DocumentReference documentReferenceIsbn = databaseReference.collection("isbnLists").document(item.bookISBN);
        documentReferenceIsbn.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        documentReferenceIsbn.update("listIds", FieldValue.arrayUnion(listDatabaseID));
                    }
                    else {
                        ArrayList<String> listIds = new ArrayList<>();
                        listIds.add(listDatabaseID);
                        IsbnItem isbnItem = new IsbnItem(item.bookISBN, item.bookTitle, item.bookAuthor, item.bookGenre, listIds);
                        databaseReference.collection("isbnLists").document(item.bookISBN).set(isbnItem);
                    }
                }
            }
        });
    }

    public void updateBookList(String isbn, boolean isbnValid) {
        // Update book list every time there is a change
        if(!isbnValid) return;
        bookList.clear();
        DocumentReference documentReference = databaseReference.collection("bookLists").document(listDatabaseID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> isbnList = documentSnapshot.toObject(ListItem.class).isbnList;
                for(String isbn : isbnList) {
                    if(storedBooks.containsKey(isbn)) {
                        bookList.add(storedBooks.get(isbn));
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        BookDownloader downloader = new BookDownloader(isbn, DonorBookPage.this);
                        new Thread(downloader).start();
                    }

                }
            }
        });
    }

    public void noBookError(String isbn) {
        // Pops up a toast if there is no book with the user-given ISBN; called from BookDownloader
        Toast.makeText(this, "Book with " + isbn + " not found.", Toast.LENGTH_LONG).show();
        DocumentReference documentReference = databaseReference.collection("bookLists").document(listDatabaseID);
        documentReference.update("isbnList", FieldValue.arrayRemove(isbn));
        updateBookList(isbn, false);
    }
}
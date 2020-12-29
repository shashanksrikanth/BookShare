package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

public class ReceiverHomePage extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    // Activity that is the home page for receivers

    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    String[] drawerItems;
    private static final String TAG = "ReceiverHomePage";

    FirebaseFirestore databaseReference;

    ArrayList<ListItem> listItems = new ArrayList<>();
    ListItemAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_home_page);

        // Set up drawer menu
        drawerItems = new String[] {"Home Page", "Donor Page"};
        drawerLayout = findViewById(R.id.drawerLayoutReceiverHomePage);
        drawerList = findViewById(R.id.leftDrawerReceiverHomePage);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, drawerItems));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // Get database reference
        databaseReference = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.receiverHomeRecyclerView);
        adapter = new ListItemAdapter(listItems, null, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateDonorList();
    }

    private void selectItem(int position) {
        // Dictates what to do when a drawer item is selected
        if(drawerItems[position].equals("Home Page")) {
            Intent intent = new Intent(this, UserHomePage.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, DonorHomePage.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // Needed for drawer menu
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        // Needed for drawer menu
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu layout
        getMenuInflater().inflate(R.menu.receiver_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Dictates what to do based on which menu item is clicked on
        if(drawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: drawerToggle " + item);
            return true;
        }
        switch(item.getItemId()) {
            case R.id.filterGenre:
                final ArrayList<String>[] allGenres = new ArrayList[]{new ArrayList<>()};
                databaseReference.collection("isbnLists").document("allUniqueGenres").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        allGenres[0] = (ArrayList<String>) snapshot.get("uniqueGenres");
                        String[] allGenresArray = allGenres[0].toArray(new String[0]);
                        AlertDialog.Builder genreBuilder = new AlertDialog.Builder(ReceiverHomePage.this);
                        genreBuilder.setTitle("Select Genre");
                        genreBuilder.setIcon(R.drawable.baseline_filter_list_black_48);
                        genreBuilder.setItems(allGenresArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getListsByGenre(allGenres[0].get(which));
                            }
                        });
                        genreBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        });
                        AlertDialog genreDialog = genreBuilder.create();
                        genreDialog.show();
                    }
                });
            case R.id.filterBookTitle:
                AlertDialog.Builder bookBuilder = new AlertDialog.Builder(this);
                bookBuilder.setTitle("Enter a book name or part of a book name");
                bookBuilder.setIcon(R.drawable.baseline_filter_list_black_48);
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setGravity(Gravity.CENTER_HORIZONTAL);
                bookBuilder.setView(editText);
                bookBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bookQuery = editText.getText().toString().trim().toLowerCase();
                        getListsByBook(bookQuery);
                    }
                });
                bookBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                AlertDialog bookDialog = bookBuilder.create();
                bookDialog.show();
            case R.id.clearFilters:
                updateDonorList();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ReceiverBookPage.class);
        int index = recyclerView.getChildLayoutPosition(v);
        ListItem item = listItems.get(index);
        intent.putExtra("ListItem", item);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        // Show info about the list in a dialog
        int position = recyclerView.getChildLayoutPosition(v);
        ListItem item = listItems.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String dialogTitle = "List name: " + item.listName;
        String dialogMessage = "Number of items in the list: " + item.isbnList.size();
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private void getListsByGenre(final String genre) {
        // Helper function that shows the lists by genre
        listItems.clear();
        adapter.notifyDataSetChanged();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final String userID = currentUser.getUid();
        databaseReference.collection("bookLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot snapshot : task.getResult()) {
                        final ListItem item = snapshot.toObject(ListItem.class);
                        if(!item.uid.equals(userID)) {
                            ArrayList<String> isbnList = item.isbnList;
                            for(String isbn : isbnList) {
                                databaseReference.collection("isbnLists").document(isbn).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot isbnSnapshot = task.getResult();
                                        if(isbnSnapshot.get("bookGenre").equals(genre)) {
                                            if(!listItems.contains(item)) {
                                                listItems.add(item);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    private void getListsByBook(final String bookName) {
        // Helper function that shows the lists with a specific book
        listItems.clear();
        adapter.notifyDataSetChanged();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final String userID = currentUser.getUid();
        databaseReference.collection("bookLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot snapshot : task.getResult()) {
                        final ListItem item = snapshot.toObject(ListItem.class);
                        if(!item.uid.equals(userID)) {
                            ArrayList<String> isbnList = item.isbnList;
                            for(String isbn : isbnList) {
                                databaseReference.collection("isbnLists").document(isbn).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot isbnSnapshot = task.getResult();
                                        if(isbnSnapshot.get("bookTitle").toString().toLowerCase().equals(bookName) || isbnSnapshot.get("bookTitle").toString().toLowerCase().contains(bookName)) {
                                            if(!listItems.contains(item)) {
                                                listItems.add(item);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateDonorList() {
        // Helper function that gets the lists from the database and updates the arraylist
        listItems.clear();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        final String userID = currentUser.getUid();
        databaseReference.collection("bookLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(DocumentSnapshot snapshot : task.getResult()) {
                        ListItem item = snapshot.toObject(ListItem.class);
                        if(!item.uid.equals(userID)) {
                            item.setListDatabaseID(snapshot.getId());
                            listItems.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }
}
package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

        // Populate list array to show lists for donations, excluding the lists that belong to the current user
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Dictates what to do based on which menu item is clicked on
        if(drawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: drawerToggle " + item);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
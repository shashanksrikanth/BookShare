package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class DonorHomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    FirebaseFirestore databaseReference;
    String[] drawerItems;
    ArrayList<DonorListItem> donorList;
    private static final String TAG = "DonorHomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home_page);

        // Set up drawer menu
        drawerItems = new String[] {"Home Page", "Receiver Page"};
        drawerLayout = findViewById(R.id.drawerLayoutDonorHomePage);
        drawerList = findViewById(R.id.leftDrawerDonorHomePage);
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
    }

    private void selectItem(int position) {
        // Dictates what to do when a drawer item is selected
        if(drawerItems[position].equals("Home Page")) {
            Intent intent = new Intent(this, UserHomePage.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, ReceiverHomePage.class);
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
        getMenuInflater().inflate(R.menu.donor_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // What to do when a menu item is clicked
        if(drawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: drawerToggle " + item);
            return true;
        }
        switch(item.getItemId()) {
            case R.id.addDonorList:
                LayoutInflater inflater = LayoutInflater.from(this);
                @SuppressLint("InflateParams")
                final View dialogView = inflater.inflate(R.layout.add_donor_list_dialog, null);
                AlertDialog.Builder addBuilder = new AlertDialog.Builder(this);
                addBuilder.setTitle("Add List");
                addBuilder.setIcon(R.drawable.baseline_add_circle_black_48);
                addBuilder.setView(dialogView);
                addBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText listName = dialogView.findViewById(R.id.listName);
                        EditText listDescription = dialogView.findViewById(R.id.listDescription);
                        String name = listName.getText().toString().trim();
                        String description = listDescription.getText().toString();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        ArrayList<String> isbn = new ArrayList<>();
                        DonorListItem item = new DonorListItem(name, description, uid, isbn);
                        databaseReference.collection("donorBookLists").add(item);
                        updateDonorList();
                    }
                });
                addBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
                AlertDialog addDialog = addBuilder.create();
                addDialog.show();
                return true;
            case R.id.donorListDefinition:
                AlertDialog.Builder definitionBuilder = new AlertDialog.Builder(this);
                definitionBuilder.setIcon(R.drawable.baseline_help_black_48);
                definitionBuilder.setTitle("What is a donation list?");
                definitionBuilder.setMessage("A donation list is a list of donations you can make available to a recipient. It can either be " +
                        "specific (i.e., fiction, for kids), or it can be a general list");
                AlertDialog dialog = definitionBuilder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDonorList() {
        // Helper function that gets the lists from the database and updates the arraylist
    }
}
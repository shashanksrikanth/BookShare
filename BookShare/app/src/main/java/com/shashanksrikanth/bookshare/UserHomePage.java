package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHomePage extends AppCompatActivity {

    TextView userNameLabel;
    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    String[] drawerItems;
    String userID;
    boolean goBackToPreviousActivity;
    FirebaseFirestore databaseReference;
    private static final String TAG = "UserHomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        // Declare text view
        userNameLabel = findViewById(R.id.userNameLabel);

        // Unpack intent
        Intent intent = getIntent();
        goBackToPreviousActivity = intent.getBooleanExtra("goBackToMain", false);

        // Get user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null) userID = currentUser.getUid();

        // Get database reference
        databaseReference = FirebaseFirestore.getInstance();

        // Set up drawer menu
        drawerItems = new String[] {"Donor Page", "Receiver Page"};
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.left_drawer);
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

        // Set up welcome message
        DocumentReference documentReference = databaseReference.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AppUser user = documentSnapshot.toObject(AppUser.class);
                String message = user.firstName + " " + user.lastName + "!";
                userNameLabel.setText(message);
            }
        });

    }

    public void onBackPressed() {
        if(goBackToPreviousActivity) super.onBackPressed();
        else return;
    }

    private void selectItem(int position) {
        // Dictates what to do when a drawer item is selected
        if(drawerItems[position].equals("Donor Page")) {
            Intent intent = new Intent(this, DonorHomePage.class);
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
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: drawerToggle " + item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DonorHomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    String[] drawerItems;
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
        // Needed for drawer meny
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
                Toast.makeText(this, "You have chosen to add a list", Toast.LENGTH_LONG).show();
                return true;
            case R.id.donorListDefinition:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.baseline_help_black_48);
                builder.setTitle("What is a donation list?");
                builder.setMessage("A donation list is a list of donations you can make available to a recipient. It can either be " +
                        "specific (i.e., fiction, for kids), or it can be a general list");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
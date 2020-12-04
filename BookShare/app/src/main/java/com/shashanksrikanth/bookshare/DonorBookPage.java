package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DonorBookPage extends AppCompatActivity {
    // The activity that shows the books the donor has up for donation- specific for each list of donations

    TextView listName;
    TextView listDescription;

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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu layout
        getMenuInflater().inflate(R.menu.donor_book_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addDonorBook:
                Toast.makeText(this, "Clicked the add button", Toast.LENGTH_SHORT).show();
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
}
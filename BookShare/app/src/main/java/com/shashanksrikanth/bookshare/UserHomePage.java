package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserHomePage extends AppCompatActivity {

    TextView userNameLabel;
    String userID;
    boolean goBackToPreviousActivity;

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

        // Get user's name from database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getName(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getName(DataSnapshot snapshot) {
        for(DataSnapshot snap : snapshot.getChildren()) {
            String firstName = snap.child(userID).getValue(AppUser.class).firstName;
            String lastName = snap.child(userID).getValue(AppUser.class).lastName;
            String welcomeMessage = firstName + " " + lastName + "!";
            userNameLabel.setText(welcomeMessage);
        }
    }

    public void onBackPressed() {
        if(goBackToPreviousActivity) super.onBackPressed();
        else return;
    }
}
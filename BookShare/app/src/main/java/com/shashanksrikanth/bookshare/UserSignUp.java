package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {

    boolean goBackToMainActivity;
    EditText userFirstName;
    EditText userLastName;
    TextView errorMessage;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        // Unpack intent
        Intent intent = getIntent();
        goBackToMainActivity = intent.getBooleanExtra("goBackToMain", false);

        // Declare edit texts and text views
        userFirstName = findViewById(R.id.userFirstName);
        userLastName = findViewById(R.id.userLastName);
        errorMessage = findViewById(R.id.errorMessageSignUp);

        // Declare Firebase DB reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        if(goBackToMainActivity) super.onBackPressed();
        else return;
    }

    public void onClickRegister(View v) {
        boolean isValid = validateForm();
        if(isValid) {
            String firstName = userFirstName.getText().toString();
            String lastName = userLastName.getText().toString();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser!=null) {
                AppUser user = new AppUser(firstName, lastName, currentUser.getEmail());
                databaseReference.child("users").child(currentUser.getUid()).setValue(user);
                Intent intent = new Intent(this, UserHomePage.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean validateForm() {
        // Helper function that checks if users entered the information
        boolean isValid = true;
        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        if(TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            isValid = false;
            errorMessage.setText(getString(R.string.missingInformation));
        }
        else errorMessage.setText("");
        return isValid;
    }
}
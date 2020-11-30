package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {

    String userEmail;
    String userID;
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
        userEmail = intent.getStringExtra("userEmail");
        userID = intent.getStringExtra("userEmail");
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
            String modifiedUserEmail = userEmail.replace('.', '%');
            AppUser user = new AppUser(firstName, lastName, modifiedUserEmail);
            databaseReference.child("users").child(userID).setValue(user);
            Intent intent = new Intent(this, UserHomePage.class);
            startActivity(intent);
            finish();
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
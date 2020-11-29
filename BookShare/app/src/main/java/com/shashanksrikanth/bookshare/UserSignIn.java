package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserSignIn extends AppCompatActivity {

    boolean signIn;
    EditText usernameSignIn;
    EditText passwordSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);

        Intent intent = getIntent();
        signIn = intent.getBooleanExtra("signIn", true);

        usernameSignIn = findViewById(R.id.usernameValue);
        passwordSignIn = findViewById(R.id.passwordValue);
    }

    public void clickGo(View v) {
        // On-click function for what happens when the user clicks the Go button
        String username = usernameSignIn.getText().toString();
        String password = passwordSignIn.getText().toString();
        if(signIn) {
            Toast.makeText(this, username + " " + password + " sign in", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, username + " " + password + " sign up", Toast.LENGTH_LONG).show();
        }
    }

    public void hideKeyboard(View v) {
        // On-click function that hides the keyboard when the screen is touched
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
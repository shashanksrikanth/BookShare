package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserSignUp extends AppCompatActivity {

    String email;
    boolean goBackToMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        Intent intent = getIntent();
        email = intent.getStringExtra("userEmail");
        goBackToMainActivity = intent.getBooleanExtra("goBackToMain", false);
        TextView textview = findViewById(R.id.textView);
        textview.setText(email);
    }

    @Override
    public void onBackPressed() {
        if(goBackToMainActivity) super.onBackPressed();
        else return;
    }
}
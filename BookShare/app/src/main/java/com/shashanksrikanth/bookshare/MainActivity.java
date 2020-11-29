package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSignIn(View v) {
        // On-click function if the sign in button is clicked
        Intent intent = new Intent(this, UserSignIn.class);
        intent.putExtra("signIn", true);
        startActivity(intent);
    }

    public void onClickSignUp(View v) {
        // On-click function if the sign in button is clicked
        Intent intent = new Intent(this, UserSignIn.class);
        intent.putExtra("signIn", false);
        startActivity(intent);
    }
}
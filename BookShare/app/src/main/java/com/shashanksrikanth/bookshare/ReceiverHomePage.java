package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class ReceiverHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_home_page);

        Toast.makeText(this, "You are in ReceiverHomePage", Toast.LENGTH_LONG).show();
    }
}
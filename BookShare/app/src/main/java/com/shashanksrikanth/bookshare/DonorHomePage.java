package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class DonorHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home_page);

        Toast.makeText(this, "You are in DonorHomePage", Toast.LENGTH_LONG).show();
    }
}
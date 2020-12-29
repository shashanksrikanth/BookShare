package com.shashanksrikanth.bookshare;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // First activity that users see after the splash screen- the login page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No network connection!");
            builder.setMessage("This app requires connectivity to the internet, either via WiFi or data. Please do so before using this app.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finishAffinity();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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

    private boolean checkNetworkConnection() {
        // Check if the device is connected to the internet
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
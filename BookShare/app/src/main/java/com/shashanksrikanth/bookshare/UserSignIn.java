package com.shashanksrikanth.bookshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserSignIn extends AppCompatActivity {

    boolean signIn;
    EditText usernameSignIn;
    EditText passwordSignIn;
    TextView errorMessage;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);

        // Get the boolean as to whether the user has signed in or signed up
        Intent intent = getIntent();
        signIn = intent.getBooleanExtra("signIn", true);

        // Initialize text and edit views
        usernameSignIn = findViewById(R.id.usernameValue);
        passwordSignIn = findViewById(R.id.passwordValue);
        errorMessage = findViewById(R.id.errorMessage);

        // Initialize Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void clickGo(View v) {
        // On-click function for what happens when the user clicks the Go button
        boolean isValid = validateForm();
        if(isValid) {
            String username = usernameSignIn.getText().toString();
            String password = passwordSignIn.getText().toString();
            if(signIn) {
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(UserSignIn.this, UserHomePage.class);
                            assert user != null;
                            intent.putExtra("goBackToPreviousActivity", false);
                            startActivity(intent);
                            UserSignIn.this.finish();
                        }
                        else Toast.makeText(UserSignIn.this, "Sign-in failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(UserSignIn.this, UserSignUp.class);
                            assert user != null;
                            intent.putExtra("goBackToMain", false);
                            startActivity(intent);
                            UserSignIn.this.finish();
                        }
                        else Toast.makeText(UserSignIn.this, "Sign-up failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
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

    private boolean validateForm() {
        // Helper function that checks if the user has entered the information
        boolean isValid = true;
        String username = usernameSignIn.getText().toString();
        String password = passwordSignIn.getText().toString();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            isValid = false;
            errorMessage.setText(getString(R.string.missingCredentials));
        }
        else errorMessage.setText("");
        return isValid;
    }
}
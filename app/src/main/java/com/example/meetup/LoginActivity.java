package com.example.meetup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText edit_gmail_login,edit_Password_login;
    Button login_button;
    TextView signUp_text;
    ProgressBar progressBar;

    // Check if user is signed in (non-null) and update UI accordingly.

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }
    private FirebaseAuth auth;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edit_Password_login = findViewById(R.id.edit_Password_login);
        edit_gmail_login = findViewById(R.id.edit_gmail_login);
        login_button = findViewById(R.id.login_button);
        signUp_text=findViewById(R.id.signUp_text);
        progressBar=findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        signUp_text.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        login_button.setOnClickListener(v -> {
            String gmail = edit_gmail_login.getText().toString();
            String Password = edit_Password_login.getText().toString();

            if (TextUtils.isEmpty(gmail) || TextUtils.isEmpty(Password)) {
                Toast.makeText(LoginActivity.this, "fill properly", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (Password.length() < 4) {
                Toast.makeText(LoginActivity.this, "Password must be greater then 4 characters", Toast.LENGTH_SHORT).show();
                return;
            }else {
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(gmail, Password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                progressBar.setVisibility(View.GONE);
                                finish();
                            } else {
                                handleSignInError(task.getException());
                            }
                        });
            }
        });
}
     //If the user is put the wrong password or email or if the internet of the device is not connected.

    private void handleSignInError(Exception exception) {
        if (exception instanceof FirebaseAuthInvalidCredentialsException){
            Toast.makeText(this, "No user founded with this email", Toast.LENGTH_SHORT).show();
        }else if (exception instanceof FirebaseAuthInvalidUserException){
            Toast.makeText(this, "Invalid password. Please try again.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.meetup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


@SuppressLint("CustomSplashScreen")
public class RegisterActivity extends AppCompatActivity {

    TextInputEditText edit_gmail, edit_Password,name_cloud;
    Button register_btn;
    ProgressBar progressBar2;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        edit_gmail = findViewById(R.id.edit_gmail);
        edit_Password = findViewById(R.id.edit_Password);
        register_btn = findViewById(R.id.register_btn);
        name_cloud = findViewById(R.id.name_cloud);
        progressBar2 = findViewById(R.id.progressBar2);

        firebaseDatabase=FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dbRef=firebaseDatabase.getReference().child("Users");

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = edit_gmail.getText().toString();
                String password = edit_Password.getText().toString();
                String name = name_cloud.getText().toString();

                AlertDialog dialog = showProgressDialog();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "fill properly", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                } else {
                    RegisterUser(email, password);
                    dialog.show();
                }
            }
    private void RegisterUser(String email,String password) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                AlertDialog dialog = showProgressDialog();
                if (task.isSuccessful()) {

                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);

                    finish();
                }
                 else {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Failed to Register", Toast.LENGTH_SHORT).show();

                }
            }

        });

            }
        });



    }
    private AlertDialog showProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}
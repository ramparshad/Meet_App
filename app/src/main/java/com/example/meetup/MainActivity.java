package com.example.meetup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageButton logout_btn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        logout_btn = findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this, R.style.dialog);
            dialog.setContentView(R.layout.dialog_layout);
            Button yes_btn, no_btn;
            yes_btn = dialog.findViewById(R.id.yes_btn);
            no_btn = dialog.findViewById(R.id.no_btn);


            yes_btn.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
            no_btn.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        });

        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void joinMeeting(View view) {
        EditText editText =findViewById(R.id.edit_text);

        if(editText == null){
            editText.setError("Please Enter meeting Id");
        }
        else {
        String meetingId = editText.getText().toString();
        if(meetingId.length() > 5){
            AlertDialog dialog = showProgressDialog();
            dialog.show();
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(meetingId).build();

            JitsiMeetActivity.launch(this, options);

            dialog.dismiss();
        }
        else {
            editText.setError("Meeting id is too short");
        }
        }
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
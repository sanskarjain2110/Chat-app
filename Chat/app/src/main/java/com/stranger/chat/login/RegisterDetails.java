package com.stranger.chat.login;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.stranger.chat.R;

public class RegisterDetails extends AppCompatActivity {
    EditText nameField;
    Button registerButton;
    ImageView profilePic;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        profilePic = findViewById(R.id.addProfilePic);
        nameField = findViewById(R.id.nameField);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> {
            if (user.isEmailVerified()) {
                Toast.makeText(getApplicationContext(), "email", LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), RegisterDetails.class));
//                finish();
            } else {
                Toast.makeText(getApplicationContext(), "verify your email", LENGTH_SHORT).show();
            }
        });

    }
}
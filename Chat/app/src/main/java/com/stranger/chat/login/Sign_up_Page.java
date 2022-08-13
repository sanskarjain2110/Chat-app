package com.stranger.chat.login;

import static android.text.TextUtils.isEmpty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.chat.R;

public class Sign_up_Page extends AppCompatActivity {
    EditText emailField, passwordField, confirmPasswordField;
    Button sign_upButton;
    TextView sign_in;
    FirebaseAuth mAuth;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);

        sign_upButton = findViewById(R.id.sign_upButton);
        sign_in = findViewById(R.id.sign_in);

        mAuth = FirebaseAuth.getInstance();

        builder = new AlertDialog.Builder(this);


        sign_in.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });

        sign_upButton.setOnClickListener(view -> {

            String email = emailField.getText().toString().toLowerCase().replaceAll("\\s", "");

            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField.getText().toString();

            if (isEmpty(email) || isEmpty(password) || isEmpty(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Please fill fields", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 8) {
                Toast.makeText(getApplicationContext(), "Sort password", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Password and Confirm password is not same", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(getApplicationContext(), "Request registered", Toast.LENGTH_SHORT).show();
                            verificationLink(user);
                            dialogBox(email, user);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && !user.isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), RegisterDetails.class));
            finish();
        }
    }

    private void verificationLink(@NonNull FirebaseUser user) {
        user.sendEmailVerification().addOnSuccessListener(unused ->
                Toast.makeText(getApplicationContext(), "A verification email is sent please verify yourself first", Toast.LENGTH_LONG).show()
        );
    }

    private void dialogBox(String email, FirebaseUser user) {
        builder.setTitle("Please verify your email " + email)
                .setCancelable(false)
                .setPositiveButton("Resend email", (dialogInterface, i) -> {
                    verificationLink(user);
                    dialogBox(email, user);
                })
                .setNegativeButton("Edit email", (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }
}
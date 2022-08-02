package com.stranger.chat.login;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stranger.chat.MainActivity;
import com.stranger.chat.R;

public class Sign_up_Page extends AppCompatActivity {
    EditText emailField, passwordField, confirmPasswordField;
    Button sign_upButton;
    TextView sign_in;
    FirebaseAuth mAuth;

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

        sign_in.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });

        sign_upButton.setOnClickListener(view -> {
            String password = null, confirmPassword = null;
            String email = emailField.getText().toString();
            password = passwordField.getText().toString();
            confirmPassword = confirmPasswordField.getText().toString();

            if (isEmpty(email) || isEmpty(password) || isEmpty(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Please fill fields", LENGTH_SHORT).show();
            } else if (password.length() < 8) {
                Toast.makeText(getApplicationContext(), "Sort password", LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Password and Confirm password is not same", LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(getApplicationContext(), "Request registered", LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.", LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
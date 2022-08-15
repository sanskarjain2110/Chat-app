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

public class Log_in_page extends AppCompatActivity {
    EditText emailField, passwordField;
    TextView forgotPassword, signUp;
    Button login;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);

        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.sin_up);
        login = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(view -> {
            String email = emailField.getText().toString().toLowerCase().replaceAll("\\s", "");
            String password = passwordField.getText().toString();

            if (isEmpty(email) || isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "please fill entry", LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.", LENGTH_SHORT).show();
                    }
                });
            }
        });

        signUp.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Sign_up_Page.class));
            finish();
        });

        forgotPassword.setOnClickListener(view ->
                Toast.makeText(getApplicationContext(), "not implemented", LENGTH_SHORT).show()
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            if (currentUser.isEmailVerified()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
//            }
        }
    }
}
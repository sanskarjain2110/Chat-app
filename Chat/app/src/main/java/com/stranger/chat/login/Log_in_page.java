package com.stranger.chat.login;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.chat.MainActivity;
import com.stranger.chat.R;

public class Log_in_page extends AppCompatActivity {
    EditText emailField, passwordField;
    TextView forgotPassword, signUp;
    Button login;

    FirebaseAuth mAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        database = FirebaseFirestore.getInstance();

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
                        //fatching data from FirebaseFirestore to local database
                        // local database  --> then change activity
                        database.collection("users").document(mAuth.getCurrentUser().getUid())
                                .addSnapshotListener(this, (snapshot, error) -> {
                                    SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("host_username", snapshot.getString("username"));
                                    editor.putString("host_phoneNumber", snapshot.getString("phoneNumber"));
                                    editor.apply();

                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                });
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

        forgotPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ForgotPassword_page.class)));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}
package com.stranger.chat.login;

import static android.text.TextUtils.isEmpty;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stranger.chat.R;

public class Log_in_page extends AppCompatActivity {
    EditText phoneNumberField, passwordField;
    TextView forgotPassword, signUp;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        phoneNumberField = findViewById(R.id.phoneNumberField);
        passwordField = findViewById(R.id.passwordField);

        forgotPassword = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.sin_up);

        login = findViewById(R.id.loginButton);
        login.setOnClickListener(view -> {
            String phoneNumber = phoneNumberField.getText().toString();
            String password = passwordField.getText().toString();

            boolean signal = false;

            int username;
            try {
                username = Integer.parseInt(phoneNumber);
                signal = true;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                                "Phone number field is empty or contain character",
                                Toast.LENGTH_SHORT)
                        .show();
                signal = false;
            }

            if (isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Password field is empty",
                        Toast.LENGTH_SHORT).show();
                signal = false;
            } else {
                signal = true;
            }

            if (signal) {
                try {
                } catch (Exception e) {
                }
            }


        });

    }
}
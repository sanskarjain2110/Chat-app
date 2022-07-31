package com.stranger.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class ResisterPage extends AppCompatActivity {
    TextInputLayout nameField, phoneField, passwordField;
    Button resister;
    String name, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resister_page);

        resister = findViewById(R.id.registerButton);
        nameField = findViewById(R.id.nameField);
        phoneField = findViewById(R.id.phoneNumberField);
        passwordField = findViewById(R.id.passwordField);

        resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = Objects.requireNonNull(nameField.getEditText()).toString();
                phone = Objects.requireNonNull(phoneField.getEditText()).toString();
                password = Objects.requireNonNull(passwordField.getEditText()).toString();
            }
        });


    }
}
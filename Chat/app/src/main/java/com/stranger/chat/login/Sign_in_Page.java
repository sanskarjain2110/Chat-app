package com.stranger.chat.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.stranger.chat.R;

public class Sign_in_Page extends AppCompatActivity {
    TextInputLayout nameField, phoneField, passwordField;
    Button resister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        nameField = findViewById(R.id.nameField);
        phoneField = findViewById(R.id.phoneNumberField);
        passwordField = findViewById(R.id.passwordField);

        resister = findViewById(R.id.registerButton);
        resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, phoneNumber, password = null;
                try {
                    name = nameField.getEditText().toString();
                    phoneNumber = phoneField.getEditText().toString();
                    password = passwordField.getEditText().toString();

                    if (password.length()<6){
                        Toast.makeText(getApplicationContext(), "to short password", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "please fill above field carefully", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }
}
package com.stranger.chat.login_modules;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stranger.chat.MainActivity;
import com.stranger.chat.R;

import java.util.concurrent.TimeUnit;

public class Log_in_page extends AppCompatActivity {

    EditText phoneNumberField, otpField;
    Button loginButton, getOtpButton;

    FirebaseAuth mAuth;
    FirebaseFirestore database;

    String phoneNumber, otp;
    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        phoneNumberField = findViewById(R.id.phoneNumberField);
        otpField = findViewById(R.id.otpField);

        loginButton = findViewById(R.id.loginButton);
        getOtpButton = findViewById(R.id.getOtpButton);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // to get otp
        getOtpButton.setOnClickListener(view -> {
            phoneNumber = "+91" + phoneNumberField.getText().toString().replaceAll("\\s", "");

            if (isEmpty(phoneNumber)) {
                Toast.makeText(getApplicationContext(), "Fill the Phone Number Field first", LENGTH_SHORT).show();
            } else if (phoneNumber.length() != 13) {
                Toast.makeText(getApplicationContext(), "Enter valid number", LENGTH_SHORT).show();
            } else {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), LENGTH_LONG).show();

                                Log.e(TAG, "onVerificationCompleted:" + e.getMessage());
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                super.onCodeSent(verificationId, token);

                                mVerificationId = verificationId;
                            }
                        }).build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        loginButton.setOnClickListener(view -> {
            otp = otpField.getText().toString();

            if (isEmpty(phoneNumber) || isEmpty(otp)) {
                Toast.makeText(getApplicationContext(), "Please fill fields", LENGTH_SHORT).show();
            } else {
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, otp));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            String currentUser = mAuth.getCurrentUser().getUid();
            database.collection("users").document(currentUser).addSnapshotListener(this, (snapshot, error) -> {
                if (error != null) {
                    return;
                } else if (snapshot != null && snapshot.exists()) {
                    // if exist then add then move to MainActivity class
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    // if not exist user will redirect to profile update page class
                    startActivity(new Intent(getApplicationContext(), Add_Profile_Detail.class));
                }
                finish();
            });
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                        String currentUser = mAuth.getCurrentUser().getUid();

                        //fatching data from FirebaseFirestore to local database
                        // local database  --> then change activity
                        database.collection("users").document(currentUser)
                                .addSnapshotListener(this, (snapshot, error) -> {

                                    // local database objects
                                    SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();

                                    // if exist then add that data to local
                                    if (snapshot != null && snapshot.exists()) {
                                        editor.putString("host_username", snapshot.getString("username"));
                                        editor.putString("host_phoneNumber", snapshot.getString("phoneNumber"));
                                        editor.apply();

                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        editor.putString("host_username", phoneNumber);
                                        editor.putString("host_phoneNumber", phoneNumber);
                                        editor.apply();

                                        // user will redirect to profile update page
                                        startActivity(new Intent(getApplicationContext(), Add_Profile_Detail.class));
                                    }
                                    finish();
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "otp verified failed", LENGTH_SHORT).show();
                    }
                });
    }
}
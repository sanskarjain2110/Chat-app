package com.stranger.chat.login;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.stranger.chat.MainActivity;
import com.stranger.chat.R;
import com.stranger.chat.data.Sign_upData;

import java.util.concurrent.TimeUnit;

//logout code
//FirebaseAuth.getInstance().signOut();
public class Sign_up_Page extends AppCompatActivity {
    ImageView profilePic;

    EditText nameField, emailField, passwordField, confirmPasswordField, phoneNumberField, otpField;

    Button sign_upButton, getOtpButton;

    TextView sign_in;

    Sign_upData data = new Sign_upData();

    String name, email, password, confirmPassword, phoneNumber, otp;

    FirebaseAuth mAuth;

    String mVerificationId;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        profilePic = findViewById(R.id.addProfilePic);

        nameField = findViewById(R.id.nameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        phoneNumberField = findViewById(R.id.phoneNumberField);
        otpField = findViewById(R.id.otpField);

        getOtpButton = findViewById(R.id.getOtpButton);
        sign_upButton = findViewById(R.id.sign_upButton);
        sign_in = findViewById(R.id.sign_in);

        mAuth = FirebaseAuth.getInstance();

        builder = new AlertDialog.Builder(this);

        profilePic.setOnClickListener(view -> {
        });

        getOtpButton.setOnClickListener(view -> {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                    .setPhoneNumber("+91" + phoneNumberField.getText().toString().replaceAll("\\s", ""))
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                            signInWithPhoneAuthCredential(credential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                            super.onCodeSent(verificationId, token);

                            mVerificationId = verificationId;
                        }
                    }).build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        });

        sign_upButton.setOnClickListener(view -> {
            name = nameField.getText().toString();
            email = emailField.getText().toString().toLowerCase().replaceAll("\\s", "");
            password = passwordField.getText().toString();
            confirmPassword = confirmPasswordField.getText().toString();
            phoneNumber = "+91" + phoneNumberField.getText().toString().replaceAll("\\s", "");
            otp = otpField.getText().toString();

            if (isEmpty(name) || isEmpty(email) || isEmpty(password) || isEmpty(confirmPassword) || isEmpty(phoneNumber) || isEmpty(otp)) {
                Toast.makeText(getApplicationContext(), "Please fill fields", LENGTH_SHORT).show();
            } else if (password.length() < 8) {
                Toast.makeText(getApplicationContext(), "Sort password", LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(getApplicationContext(), "Password and Confirm password is not same", LENGTH_SHORT).show();
            } else {
                data.setUsername(name);
                data.setEmail(email);
                data.setPhoneNumber(phoneNumber);

                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(mVerificationId, otp));
            }
        });

        sign_in.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Log_in_page.class));
            finish();
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "otp verified", LENGTH_SHORT).show();

                        signInWithEmailAndPassword();
                    } else {
                        Toast.makeText(getApplicationContext(), "otp verified failed", LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithEmailAndPassword() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.linkWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Request registered", LENGTH_SHORT).show();
                            verificationLink(user);

                            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(data);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void verificationLink(@NonNull FirebaseUser user) {
        user.sendEmailVerification().addOnSuccessListener(unused ->
                Toast.makeText(getApplicationContext(), "A verification email is sent please verify yourself first", LENGTH_SHORT).show()
        );
    }
}
package com.stranger.chat.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.login_modules.LogInPage;

// to be implement
// linked divices
public class Settings extends AppCompatActivity {
    String currentUserId = FirebaseDatabaseConnection.currentUser.getUid();

    Toolbar topAppBar;
    LinearLayout userCardView;
    ImageView profilePic;
    TextView username, phoneNumber;
    Button account, linkedDevices, apperareance, chats, stories, notification, privacy,
            dataAndStorage, help, inviteFriend, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        topAppBar = findViewById(R.id.topAppBar);
        userCardView = findViewById(R.id.userCard);
        profilePic = findViewById(R.id.profilePic);
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);

        account = findViewById(R.id.account);
        linkedDevices = findViewById(R.id.linkedDevices);

        apperareance = findViewById(R.id.apperareance);
        chats = findViewById(R.id.chats);
        stories = findViewById(R.id.stories);
        notification = findViewById(R.id.notification);
        privacy = findViewById(R.id.privacy);
        dataAndStorage = findViewById(R.id.dataAndStorage);
        help = findViewById(R.id.help);
        inviteFriend = findViewById(R.id.inviteFriend);
        logout = findViewById(R.id.logout);

        topAppBar.setNavigationOnClickListener(v -> finish());

        userCardView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Profile.class)));

        account.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Account.class)));

        linkedDevices.setOnClickListener(v -> Toast.makeText(getApplicationContext(), R.string.not_implemented, Toast.LENGTH_SHORT).show());

        apperareance.setOnClickListener(v -> new Intent(getApplicationContext(), Apperareance.class));

        chats.setOnClickListener(v -> {
        });

        stories.setOnClickListener(v -> {
        });

        notification.setOnClickListener(v -> {
        });

        privacy.setOnClickListener(v -> {
        });

        dataAndStorage.setOnClickListener(v -> {
        });

        help.setOnClickListener(v -> {
        });

        inviteFriend.setOnClickListener(v -> {

        });

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(getApplicationContext(), LogInPage.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabaseConnection.userDocument(currentUserId).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                phoneNumber.setText((String) value.get("phoneNumber"));
                username.setText((String) value.get("username"));
                if (value.get("profilePic") != null) {
                    Picasso.get().load((String) value.get("profilePic")).into(profilePic);
                }
            }
        });
    }
}
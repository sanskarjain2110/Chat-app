package com.stranger.chat.chat_modules;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;

public class ConversationSetting extends AppCompatActivity {
    AppBarLayout appBar;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView profileImageView;
    MaterialToolbar toolBar;

    TextView username, phoneNumber;

    LinearLayout videoCall, audioCall, notification, search, socialMedia;

    Button disappearingMessage, chatColorAndWallpaper, soundAndNotification, contactDetails, viewSafetyNumber, seeAll, block;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_setting);

        Bundle bundle = getIntent().getBundleExtra("data");

        appBar = findViewById(R.id.appBar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        profileImageView = findViewById(R.id.profileImageView);
        toolBar = findViewById(R.id.toolBar);

        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);

        toolBar.setNavigationOnClickListener(v -> finish());

        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                // Toolbar is fully collapsed
                collapsingToolbar.setTitleEnabled(true);
                collapsingToolbar.setTitle(bundle.getString("reciverName"));
            } else {
                // Toolbar is partially or fully expanded
                collapsingToolbar.setTitleEnabled(false);
            }
        });

        Picasso.get().load(bundle.getString("reciverProfilePic")).into(profileImageView);
        username.setText(bundle.getString("reciverName"));
        phoneNumber.setText(bundle.getString("reciverNumber"));

    }
}

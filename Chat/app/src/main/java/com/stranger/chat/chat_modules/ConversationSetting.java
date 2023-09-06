package com.stranger.chat.chat_modules;

import static com.stranger.chat.fuctionality.Keys.BUNDLE_DATA;
import static com.stranger.chat.fuctionality.Keys.firebase.PHONE_NUMBER;
import static com.stranger.chat.fuctionality.Keys.firebase.PROFILE_PIC_URL;
import static com.stranger.chat.fuctionality.Keys.firebase.USERNAME;

import android.os.Bundle;
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
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_setting);

        Bundle bundle = getIntent().getBundleExtra(BUNDLE_DATA);

        appBar = findViewById(R.id.appBar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        profileImageView = findViewById(R.id.profileImageView);
        toolBar = findViewById(R.id.toolBar);

        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);

        toolBar.setNavigationOnClickListener(v -> finish());

        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) { // Toolbar is fully collapsed
                collapsingToolbar.setTitleEnabled(true);
                collapsingToolbar.setTitle(bundle.getString(USERNAME));
            } else { // Toolbar is partially or fully expanded
                collapsingToolbar.setTitleEnabled(false);
            }
        });

        Picasso.get().load(bundle.getString(PROFILE_PIC_URL)).placeholder(R.drawable.account_circle_24px).into(profileImageView);
        username.setText(bundle.getString(USERNAME));
        phoneNumber.setText(bundle.getString(PHONE_NUMBER));

    }
}

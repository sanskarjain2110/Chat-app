package com.stranger.chat.chat_modules;

import static com.stranger.chat.fuctionality.Keys.BUNDLE_DATA;
import static com.stranger.chat.fuctionality.Keys.firebase.MESSAGE_ID;
import static com.stranger.chat.fuctionality.Keys.firebase.PHONE_NUMBER;
import static com.stranger.chat.fuctionality.Keys.firebase.PROFILE_PIC_URL;
import static com.stranger.chat.fuctionality.Keys.firebase.USERNAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;

public class ConversationSetting extends AppCompatActivity {
    AppBarLayout appBar;
    CollapsingToolbarLayout collapsingToolbar;
    ImageView profileImageView;
    TextView username, phoneNumber;
    MaterialToolbar toolBar;

    FloatingActionButton videoCall, audioCall, notification, search, socialMedia;

    LinearLayout sharedMedia, media;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_setting);

        Bundle bundle = getIntent().getBundleExtra(BUNDLE_DATA);

        appBar = findViewById(R.id.appBar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        profileImageView = findViewById(R.id.profileImageView);
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);
        toolBar = findViewById(R.id.toolBar);

        sharedMedia = findViewById(R.id.sharedMedia);
        media = findViewById(R.id.media);

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

        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("message/" + bundle.getString(MESSAGE_ID));
        listRef.listAll().addOnSuccessListener(listResult -> {
            if (listResult.getItems().size() == 0) {
                sharedMedia.setVisibility(View.GONE);
                return;
            }
            for (StorageReference item : listResult.getItems()) {
                // All the items under listRef.
                View view = LayoutInflater.from(this).inflate(R.layout.sample_image, null);
                media.addView(view);
                ImageView img = view.findViewById(R.id.image);
                item.getDownloadUrl().addOnCompleteListener(task -> Picasso.get().load(task.getResult()).placeholder(R.drawable.image_48px).into(img));
            }
        }).addOnFailureListener(e -> {
            sharedMedia.setVisibility(View.GONE);
            Toast.makeText(this, "Somthing went wrong in media section", Toast.LENGTH_SHORT).show();
        });
    }
}

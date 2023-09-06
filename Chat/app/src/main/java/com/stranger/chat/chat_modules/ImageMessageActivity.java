package com.stranger.chat.chat_modules;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stranger.chat.R;
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection;
import com.stranger.chat.fuctionality.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImageMessageActivity extends AppCompatActivity {

    Bundle bundle;

    Toolbar toolbar;
    ImageView image;
    EditText message;
    Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_message);

        bundle = getIntent().getBundleExtra("data");
        String imageUri = bundle.getString("image");
        String messageId = bundle.getString("messageId");

        toolbar = findViewById(R.id.topAppBar);
        image = findViewById(R.id.image);
        message = findViewById(R.id.getMessage);
        send = findViewById(R.id.send);

        toolbar.setNavigationOnClickListener(v -> finish());

        image.setImageURI(Uri.parse(imageUri));

        send.setOnClickListener(view -> {
            finish();
            String pictureName = UUID.randomUUID().toString() + ".jpg";
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("message").child(messageId).child(pictureName);

            storageReference.putFile(Uri.parse(imageUri)).addOnFailureListener(e -> {
                Toast.makeText(ImageMessageActivity.this, "network problem", Toast.LENGTH_SHORT).show();

            }).addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String dilogId = FirebaseDatabaseConnection.randomId(),
                        text = message.getText().toString().trim(),
                        time = (String) new Text(this).timeStamp();

                Map<String, Object> pushMessage = new HashMap<>();
                pushMessage.put("sender", FirebaseDatabaseConnection.currentUser.getUid());
                pushMessage.put("timeStamp", time);
                if (text.length() != 0) pushMessage.put("message", text);
                pushMessage.put("uri", uri);
                pushMessage.put("dilogId", dilogId);
                pushMessage.put("type", "image");

                FirebaseDatabaseConnection.messageDataCollection(messageId).document(dilogId).set(pushMessage).addOnSuccessListener(v -> {
                    Map<String, Object> lastPush = new HashMap<>();
                    lastPush.put("lastSeen", time);
                    FirebaseDatabaseConnection.messageDocument(messageId).update(lastPush);

                }).addOnFailureListener(v -> Toast.makeText(this, "check internet connection", Toast.LENGTH_SHORT).show());
            }));
        });
    }
}
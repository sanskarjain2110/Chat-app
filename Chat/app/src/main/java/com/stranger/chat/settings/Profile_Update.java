package com.stranger.chat.settings;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.stranger.chat.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile_Update extends AppCompatActivity {

    ImageView profilePicField;
    EditText nameField, phoneNumberField;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;

    Uri profilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        profilePicField = findViewById(R.id.changeProfilePic);
        nameField = findViewById(R.id.nameField);
        phoneNumberField = findViewById(R.id.phoneNumberField);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        // back button
        findViewById(R.id.backButton).setOnClickListener(view -> finish());

        // done button
        findViewById(R.id.doneButton).setOnClickListener(view -> finish());

        // fill the details
        FirebaseFirestore.getInstance().collection("users").document(currentUserId).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            if (value != null) {
                nameField.setText((String) value.get("username"));
                phoneNumberField.setText((String) value.get("phoneNumber"));
                if (value.get("profilePic") != null) {
                    Picasso.get().load((String) value.get("profilePic")).into(profilePicField);
                }
            }
        });

        // to uplode profile pic
        profilePicField.setOnClickListener(view -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 101);
        });
    }

    // use for get result from intent here it use for images
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null) {
            String destinationUriString = UUID.randomUUID().toString() + ".jpg";
            UCrop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), destinationUriString)))
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(20000, 2000)
                    .start(this);
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            profilePicUri = UCrop.getOutput(data);

            // set image to widiget
            profilePicField.setImageURI(profilePicUri);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploding image...");
            progressDialog.setProgress(0);
            progressDialog.show();

            //uplode profile pic in firebaseStorage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users").child(currentUserId).child("profilePic.jpg");
            storageRef.putFile(profilePicUri).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }).addOnProgressListener(snapshot -> {
                int progressPercent = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage(progressPercent + "% file uploded");
            }).addOnSuccessListener(taskSnapshot -> {
                progressDialog.setMessage("Verifying......");
                // on successfull upload it will download CloudPicUri
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("profilePic", uri.toString() + " ");
                    FirebaseFirestore.getInstance().collection("users").document(currentUserId).update(details);
                    progressDialog.dismiss();
                }).addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "Image not uploded", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
            });
        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            final Throwable cropError = UCrop.getError(data);
            Log.e(TAG, "onActivityResult: ", cropError);
        }
    }
}
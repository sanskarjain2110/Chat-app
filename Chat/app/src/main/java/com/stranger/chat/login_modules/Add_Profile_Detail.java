package com.stranger.chat.login_modules;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
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
import com.stranger.chat.MainActivity;
import com.stranger.chat.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Add_Profile_Detail extends AppCompatActivity {

    ImageView profilePic;
    EditText nameField;
    Button updateButton;

    Uri profilePicUri;

    // Use for buinding sign in data
    Map<String, String> details = new HashMap<>();

    SharedPreferences sharedPref;

    FirebaseAuth mAuth;
    FirebaseUser user;

    String name, phoneNumber, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_details);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();

        sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
        phoneNumber = sharedPref.getString("host_phoneNumber", userId);

        profilePic = findViewById(R.id.addProfilePic);
        nameField = findViewById(R.id.nameField);

        updateButton = findViewById(R.id.updateButton);

        // setting name field as mobile number
        nameField.setText(phoneNumber);

        // to open gallery and select image
        profilePic.setOnClickListener(view -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 101);
        });

        updateButton.setOnClickListener(view -> {
            name = nameField.getText().toString().trim();
            if (isEmpty(name)) {
                Toast.makeText(getApplicationContext(), "Please fill fields", LENGTH_SHORT).show();
            } else {
                details.put("username", name.trim());
                details.put("phoneNumber", phoneNumber);
                details.put("userId", userId);

                //login data updated
                FirebaseFirestore.getInstance().collection("users").document(userId).set(details);

                //saving data to local database
                SharedPreferences sharedPref = getSharedPreferences("localData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("host_username", details.get("username"));
                editor.apply();

                // after updating the profile user will redirect to main activity
                // we are calling main page befor Add_Profile_Detail class because we have to open it in background
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
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
            profilePic.setImageURI(profilePicUri);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploding image........");
            progressDialog.setProgress(0);
            progressDialog.show();

            //uplode profile pic in firebaseStorage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("users").child(userId).child("profilePic.jpg");
            storageRef.putFile(profilePicUri)
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    })
                    .addOnProgressListener(snapshot -> {
                        int progressPercent = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressDialog.setMessage(progressPercent + "% file uploded");
                    })
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.setMessage("Verifying......");
                        // on successfull upload it will download CloudPicUri
                        storageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    details.put("profilePic", uri.toString() + " ");
                                    progressDialog.dismiss();
                                })
                                .addOnFailureListener(exception -> {
                                    Toast.makeText(getApplicationContext(), "Image not uploded", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                });
                    });
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e(TAG, "onActivityResult: ", cropError);
        }
    }
}
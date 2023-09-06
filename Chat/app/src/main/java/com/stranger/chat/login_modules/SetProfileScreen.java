package com.stranger.chat.login_modules;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.currentUser;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.userDocument;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.stranger.chat.R;
import com.stranger.chat.fuctionality.Routes;
import com.stranger.chat.fuctionality.Text;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetProfileScreen extends AppCompatActivity {
    TextView tvTitle;
    RelativeLayout pic;

    ImageView profilePic;
    Button btAddPic;
    EditText tfFirstName, tfSecondName;
    Button btNext;

    Uri profilePicUri;
    // Use for buinding sign in data
    Map<String, String> details = new HashMap<>();
    SharedPreferences sharedPref;

    String name, phonenumber, userId;

    Intent selectImageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile_screen);

        phonenumber = currentUser.getPhoneNumber();
        name = phonenumber;

        userId = currentUser.getUid();
        if (isEmpty(userId)) new Routes(this, this).loginModuleRoutes();

        selectImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        tvTitle = findViewById(R.id.tvTitle);
        profilePic = findViewById(R.id.profilePic);
        pic = findViewById(R.id.pic);
        btAddPic = findViewById(R.id.btAddPic);
        tfFirstName = findViewById(R.id.tfFirstName);
        tfSecondName = findViewById(R.id.tfLastName);
        btNext = findViewById(R.id.btNext);

        // Clickable Span
        new Text(this).setClickableSpan(tvTitle, "Profiles are visible to people you message, contacts, and groups. ", "Learn more", null, false);

        // to open gallery and select image
        pic.setOnClickListener(view -> startActivityForResult(selectImageIntent, 101));
        btAddPic.setOnClickListener(view -> startActivityForResult(selectImageIntent, 101));

        if (!isEmpty(name)) tfFirstName.setText(name);

        // save details in firebase
        btNext.setOnClickListener(view -> {
            String firstName = tfFirstName.getText().toString().trim(), secondName = tfSecondName.getText().toString().trim();

            if (isEmpty(firstName)) {
                Toast.makeText(getApplicationContext(), "please fill required field", LENGTH_SHORT).show();
                return;
            } else name = String.format("%s %s", firstName, secondName);

            if (!isEmpty(name)) details.put("username", name.trim());

            if (!isEmpty(phonenumber)) details.put("phonenumber", phonenumber);
            details.put("userId", userId);

            userDocument(userId).set(details); // user data upload on firebase

            //saving data to local database
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("host_username", details.get("username"));
            editor.apply();

            // after updating the profile user will redirect to main activity
            // we are calling main page befor Add_Profile_Detail class because we have to open it in background
            new Routes(this, this).loginModuleRoutes();
        });
    }

    // use for get result from intent here it use for images
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;

        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            String destinationUriString = UUID.randomUUID().toString() + ".jpg";
            UCrop.of(data.getData(), Uri.fromFile(new File(getCacheDir(), destinationUriString))).withAspectRatio(1, 1).withMaxResultSize(20000, 2000).start(this);
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
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
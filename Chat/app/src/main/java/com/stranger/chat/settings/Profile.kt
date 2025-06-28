package com.stranger.chat.settings

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.stranger.chat.fuctionality.FirebaseDatabaseConnection
import com.stranger.chat.ui.theme.ChatTheme
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ProfileUI(
    closeActivity: () -> Unit = {},
    userImage: @Composable (modifier: Modifier) -> Unit = { modifier ->
        Icon(Icons.Rounded.AccountCircle, "Account", modifier)
    },
    onUserImageChange: () -> Unit = {},
    username: String = "",
    onUsernameChange: (String) -> Unit = {},
    onSaveChange: () -> Unit = {}
) {
    ChatTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(title = { Text("Profile") }, navigationIcon = {
                IconButton(onClick = closeActivity) { Icon(Icons.Rounded.ArrowBackIos, "back") }
            })
        }, floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onSaveChange) {
                Text(text = "Save changes")
            }

        }) { paddingValue ->
            Column(
                Modifier
                    .padding(paddingValue)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                userImage(
                    Modifier
                        .size(256.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onUserImageChange),
                )

                TextField(value = username,
                    onValueChange = onUsernameChange,
                    modifier = Modifier.fillMaxWidth(0.8F),
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Rounded.Person, "Username") },
                    trailingIcon = {
                        if (username != "") IconButton(onClick = { onUsernameChange("") },
                            content = { Icon(Icons.Rounded.Close, "close") })
                    })
            }
        }
    }
}

class Profile : AppCompatActivity() {
    private var currentUserId = FirebaseDatabaseConnection.currentUser.uid
    private var query: DocumentReference? = FirebaseDatabaseConnection.userDocument(currentUserId)

    // data
    private var username: String = ""
    private var profilePicUri: Any? = null

    var image: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            result?.let {
                val destinationUri = Uri.fromFile(File(cacheDir, "${UUID.randomUUID()}.jpg"))
                UCrop.of(it, destinationUri)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(2000, 2000)  // safe resolution
                    .start(this)
            } ?: run {
                Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileUI(closeActivity = { finish() },
                userImage = { modifier ->
                    if (profilePicUri == null) Icon(
                        Icons.Rounded.AccountCircle, "Account", modifier
                    )
                    else {
                        // transition need to add
                        AsyncImage(profilePicUri, "profile", modifier)
                    }
                },
                onUserImageChange = { image.launch("image/*") },
                username = username,
                onUsernameChange = { username = it },
                onSaveChange = {
                    finish()
//        saveButton.setOnClickListener(View.OnClickListener { v: View? ->
//            val data: MutableMap<String, Any> = HashMap()
//            data["username"] = nameField.getText().toString()
//            query!!.update(data).addOnSuccessListener { unused: Void? -> finish() }
//        })
                })
        }

    }

    override fun onStart() {
        super.onStart()
        query!!.addSnapshotListener { value: DocumentSnapshot?, error: FirebaseFirestoreException? ->
            when {
                error != null -> Log.e("error", "error message:${error.message} \nerror: $error")

                value != null -> {
                    username = value["username"] as String
                    profilePicUri = value["profilePic"]
                }

                else -> Log.d("result", "error message: no data found!!!!")
            }
        }
    }

    // use for get result from intent here it use for images
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        when {
            intentData != null &&
                    resultCode == RESULT_OK && requestCode == 101 -> {
                val destinationUriString = UUID.randomUUID().toString() + ".jpg"
                UCrop.of(intentData.data!!, Uri.fromFile(File(cacheDir, destinationUriString)))
                    .withAspectRatio(1f, 1f).withMaxResultSize(20000, 2000).start(this)
            }

            intentData != null &&
                    resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP -> {
                profilePicUri = UCrop.getOutput(intentData)

                // set image to widiget
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploding image...")
                progressDialog.progress = 0
                progressDialog.show()

                //uplode profile pic in firebaseStorage
                val storageRef =
                    FirebaseStorage.getInstance().reference.child("users").child(currentUserId)
                        .child("profilePic.jpg")
                storageRef.putFile(profilePicUri!! as Uri).addOnFailureListener { e: Exception? ->
                    Toast.makeText(applicationContext, "Network Error", Toast.LENGTH_LONG).show()
                    progressDialog.dismiss()
                }.addOnProgressListener { snapshot: UploadTask.TaskSnapshot ->
                    val progressPercent =
                        (100 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
                    progressDialog.setMessage("$progressPercent% file uploded")
                }.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    progressDialog.setMessage("Verifying......")
                    // on successfull upload it will download CloudPicUri
                    storageRef.downloadUrl.addOnSuccessListener { uri: Uri ->
                        val details: MutableMap<String, Any> = HashMap()
                        details["profilePic"] = "$uri "
                        FirebaseFirestore.getInstance().collection("users").document(currentUserId)
                            .update(details)
                        progressDialog.dismiss()
                    }.addOnFailureListener { exception: Exception? ->
                        Toast.makeText(applicationContext, "Image not uploded", Toast.LENGTH_LONG)
                            .show()
                        progressDialog.dismiss()
                    }
                }
            }

            resultCode == UCrop.RESULT_ERROR -> {
                val cropError = UCrop.getError(intentData!!)
                Log.e(ContentValues.TAG, "onActivityResult: ", cropError)
            }
        }
    }
}
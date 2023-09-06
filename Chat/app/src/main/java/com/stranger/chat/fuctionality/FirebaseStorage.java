package com.stranger.chat.fuctionality;

import com.google.firebase.storage.StorageReference;

public class FirebaseStorage {

    public static StorageReference profilePicStorageReferance(String userId) {
        return com.google.firebase.storage.FirebaseStorage.getInstance().getReference().child("users").child(userId).child("profilePic.jpg");
    }
}

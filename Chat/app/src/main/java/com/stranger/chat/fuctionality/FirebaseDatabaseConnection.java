package com.stranger.chat.fuctionality;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabaseConnection {
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = mAuth.getCurrentUser();

    @NonNull
    public static String randomId() {
        return FirebaseFirestore.getInstance().collection("alpha").document().getId();
    }

    // user Collection
    public static final CollectionReference userCollection = FirebaseFirestore.getInstance().collection("users");

    @NonNull
    public static DocumentReference userDocument(String userId) {
        return userCollection.document(userId);
    }

    // userMessage collection
    @NonNull
    public static DocumentReference userChatIdDocument(String userId) {
        return FirebaseFirestore.getInstance().collection("userChatId").document(userId);
    }

    // message collection
    public static final CollectionReference messageCollection = FirebaseFirestore.getInstance().collection("messages");

    @NonNull
    public static DocumentReference messageDocument(String messageId) {
        return messageCollection.document(messageId);
    }

    @NonNull
    public static CollectionReference messageDataCollection(String messageId) {
        return messageDocument(messageId).collection("messagesData");
    }

    // notes collection
    @NonNull
    public static CollectionReference notesCollectionReference(String userId) {
        return FirebaseFirestore.getInstance().collection("users").document(userId).collection("notes");
    }
}

package com.stranger.chat.fuctionality;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseConnections {
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = mAuth.getCurrentUser();

    // user Collection
    public static final CollectionReference userCollection = FirebaseFirestore.getInstance().collection("users");

    public static DocumentReference userDocument(String userId) {
        return userCollection.document(userId);
    }

    public static DocumentReference userChatIdDocument(String userId) {
        return FirebaseFirestore.getInstance().collection("userChatId").document(userId);
    }

    // message collection
    public static final CollectionReference messageCollection = FirebaseFirestore.getInstance().collection("messages");

    public static DocumentReference messageDocument(String messageId) {
        return messageCollection.document(messageId);
    }

    public static CollectionReference messageDataCollection(String messageId) {
        return messageCollection.document(messageId).collection("messagesData");
    }

    public static CollectionReference notesCollectionReference(String userId) {
        return FirebaseFirestore.getInstance().collection("users").document(userId).collection("notes");
    }
}

package com.stranger.chat.fuctionality;

import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.currentUser;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.userDocument;

import android.content.Context;
import android.content.Intent;

import com.stranger.chat.MainActivity;
import com.stranger.chat.login_modules.Add_Profile_Detail;
import com.stranger.chat.login_modules.LogInPage;

public class Routes {
    private final Context context;

    public Routes(Context context) {
        this.context = context;
    }

    public void loginModuleRoughts() {
        if (currentUser != null) userDocument(currentUser.getUid())
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) return;

                    if (snapshot != null) {
                        if (snapshot.exists()) {
                            context.startActivity(new Intent(context, MainActivity.class));
                        } else {
                            context.startActivity(new Intent(context, Add_Profile_Detail.class));
                        }
                    }
                });
        else context.startActivity(new Intent(context, LogInPage.class));

    }
}

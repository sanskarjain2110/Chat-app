package com.stranger.chat.fuctionality;

import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.currentUser;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.userDocument;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.stranger.chat.HomeScreen;
import com.stranger.chat.login_modules.LoginScreen;
import com.stranger.chat.login_modules.SetProfileScreen;

public class Routes {
    private final Context context;
    private final Activity activity;

    public Routes(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void loginModuleRoutes() {
        if (currentUser != null) userDocument(currentUser.getUid())
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) return;

                    if (snapshot != null && snapshot.exists())
                        context.startActivity(new Intent(context, HomeScreen.class));

                    else context.startActivity(new Intent(context, SetProfileScreen.class));
                });
        else context.startActivity(new Intent(context, LoginScreen.class));

        activity.finish();
    }
}

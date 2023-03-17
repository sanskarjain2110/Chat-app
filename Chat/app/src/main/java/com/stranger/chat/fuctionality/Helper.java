package com.stranger.chat.fuctionality;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static Object timeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.US);
        return sdf.format(new Date());
    }

    public static void copyString(Context context, String message) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", message);
        clipboardManager.setPrimaryClip(clipData);
    }
}

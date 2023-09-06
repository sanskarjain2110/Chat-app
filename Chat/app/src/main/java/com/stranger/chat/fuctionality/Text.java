package com.stranger.chat.fuctionality;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Text {
    private final Context context;

    public Text(Context context) {
        this.context = context;
    }

    public void setClickableSpan(@NonNull TextView textView, @NonNull String nonSpanText, @NonNull String spanText, String url, Boolean textUnderline) {

        // Create a SpannableString with the normalText
        SpannableString spannableString = new SpannableString(nonSpanText + spanText);

        // Create a clickable span for the highlightedText
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(android.view.View widget) {
                Toast.makeText(context, "url" + url, Toast.LENGTH_SHORT).show();
                // redirect url
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(textUnderline);
            }
        };

        // Set the clickable span only for the "Learn" text
        spannableString.setSpan(clickableSpan, nonSpanText.length(), nonSpanText.length() + spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Enable clickable links in the TextView
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        // Set the SpannableString to the TextView
        textView.setText(spannableString);
    }

    public String timeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
        return sdf.format(new Date());
    }


    public static String getFormattedTimeDifference(String timestamp) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
            Date date = dateFormat.parse(timestamp);

            if (date == null) return null;

            long timeDifferenceMillis = System.currentTimeMillis() - date.getTime();
            long seconds = timeDifferenceMillis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long weeks = days / 7;

            if (weeks > 0) return weeks + "w";
            else if (days > 0) return days + "d";
            else if (hours > 0) return hours + "h";
            else if (minutes > 0) return minutes + "m";
            else if (timeDifferenceMillis > 0) return "Now";
            else return timestamp;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void copyString(String message) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", message);
        clipboardManager.setPrimaryClip(clipData);
    }
}

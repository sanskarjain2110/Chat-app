package com.stranger.chat.fuctionality;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeStamp {
    public static Object timeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.US);
        return sdf.format(new Date());
    }
}

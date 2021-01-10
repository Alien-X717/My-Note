package com.example.mynote.constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
    public static final int DB_VERSION =1;
    public static final String DB_NAME ="mynotesdb";
    public static final String TABLE_NAME = "notes";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_DATETIME = "datetime";
    public static final String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d yyyy',' h:mm a");
        Date date = new Date();
        return sdf.format(date);

    }

}

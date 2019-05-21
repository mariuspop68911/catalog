package com.example.mariuspop.catalog3;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

public class DateUtil {

    public static String getData(Context context, Date date){
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(date);
    }
}

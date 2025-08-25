package com.tp1202510030.backend.shared.interfaces.rest.transform;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    private static final String DATE_FORMAT = "dd-MM-yyyy'T'HH:mm:ss:SSS";

    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }
}
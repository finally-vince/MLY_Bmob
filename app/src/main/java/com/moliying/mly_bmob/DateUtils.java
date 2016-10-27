package com.moliying.mly_bmob;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * company moliying.com
 * author vince
 * 2016/10/26
 */
public class DateUtils {
    public static String toDate(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}

package com.mission.cricstat.Common;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class StringUtil {
    public static String toShort(String str) {
        String[] strings = str.split(Pattern.quote(" "));
        if (strings.length == 0) return str;
        else if (strings.length == 1) str =  strings[0].substring(0,3);
        else {
            StringBuilder output = new StringBuilder();
            for (String string : strings) {
                output.append(string.substring(0, 1));
            }
            str = output.toString();
        }
        return str.toUpperCase();
    }

    public static String toCamelCase(String str) {
        String res = "";
        String[] strings = str.split(Pattern.quote(" "));
        if (strings.length == 0) return str;
        for (int i=0; i < strings.length; i++) {
            if (strings[i].length() != 0) {
                res += strings[i].substring(0,1).toUpperCase() + strings[i].substring(1);
                if (i < strings.length-1) res += " ";
            }
        }
        return res;
    }
    public static String toOvers(int balls) {
        return (balls/6) + "." + balls%6;
    }

    public static String epochToDate(Long epoch) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return simpleDateFormat.format(new Date(epoch));
    }
}

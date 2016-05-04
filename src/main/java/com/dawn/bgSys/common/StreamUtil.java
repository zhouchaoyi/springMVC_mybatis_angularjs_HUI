package com.dawn.bgSys.common;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by zhouchaoyi on 2016/5/4.
 */
public class StreamUtil {
    public static String readString(BufferedReader reader) {
        String temp = "";
        String str = "";
        try {
            while((temp = reader.readLine()) != null) {
                str = str + temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}

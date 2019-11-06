package com.tongan.learn.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author chao
 * @date 2019/7/5 14:14
 */
public class StringUtil {


    protected static String getRetString(InputStream is) {
        String buf;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            buf = sb.toString();
            return buf;

        } catch (Exception e) {
            return null;
        }
    }
}

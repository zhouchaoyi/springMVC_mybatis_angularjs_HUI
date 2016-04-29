package com.dawn.bgSys.common.password;

import java.security.MessageDigest;

/**
 * Created with qiangxiaolong
 * User: P0031372
 * Date: 13-8-9
 * Time: 下午2:39.
 */
public class EncryptionUtil {
    /**
     * MD5 code generation
     *
     * source
     * @return string
     */
    public static String encryptByMD5(String source) {

        String resultString = null;
        try {
            resultString = source;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteArrayToHexString(byte b[]) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}

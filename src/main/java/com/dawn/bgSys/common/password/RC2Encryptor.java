package com.dawn.bgSys.common.password;

import javax.crypto.Cipher;
import javax.crypto.spec.RC2ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created with qiangxiaolong
 * User: P0031372
 * Date: 13-8-20
 * Time: 上午10:36.
 */
public class RC2Encryptor {
    private static final byte[] PASSWORD_KEY = {(byte) 0x85, (byte) 0x09, (byte) 0xef, (byte) 0xfc, (byte) 0xdd, (byte) 0xb3, (byte) 0x23, (byte) 0x77};

    public static String decrypt(String encrypted) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(PASSWORD_KEY, "RC2");
            RC2ParameterSpec rc2spec = new RC2ParameterSpec(64, PASSWORD_KEY);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, rc2spec);
            byte[] decrypted = cipher.doFinal(toByte(encrypted));
            result = new String(decrypted, "utf-8");
            return result;
        } catch (Exception e) {
            return "error";
        }

    }

    public static String encrypt(String encrypted) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(PASSWORD_KEY, "RC2");
            Cipher cipher = Cipher.getInstance("RC2/CBC/PKCS5Padding");
            RC2ParameterSpec rc2spec = new RC2ParameterSpec(64, PASSWORD_KEY);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, rc2spec);
            byte[] result = cipher.doFinal(encrypted.getBytes());
            return toHex(result);
        } catch (Exception e) {
            return "error";
        }
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}

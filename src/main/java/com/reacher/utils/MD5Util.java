package com.reacher.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MD5Util {

    public final static String md5(String source) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte[] byteArray = new byte[source.length()];
        for (int i = 0; i < source.length(); i++) {
            byteArray[i] = (byte) source.charAt(i);
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}

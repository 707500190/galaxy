package com.sun.content.util;


import com.sun.content.api.common.constant.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static String convertByteToHexString(byte[] bytes){
        String result = "";
        for (int i=0;i<bytes.length;i++){
            int temp = bytes[i] & 0xff;
            String tempHex = Integer.toHexString(temp);
            if(tempHex.length() < 2 ){
                result += "0" + tempHex;
            } else {
              result += tempHex;
            }
        }
        return result;
    }

    public static String md5(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return convertByteToHexString(md5.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    public static String getLoginVerification(String userName, String password){
        return MD5Util.md5(userName + "||"+ password + Constant.FACE_MD5_LOGIN);
    }

    public static void main(String[] args) {

        System.out.println(md5("123456"));
    }
}

package com.sun.content.util;

import com.sun.content.api.common.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AESUtil {

    /* Decrypt message with AES*/
    public static String decryptWithAES(String encryptMsg, String aesKey) throws Exception {
        String decrptyresult = null;
        try {
            Cipher cipher = Cipher.getInstance(Constant.AES);
            SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(aesKey), Constant.AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] encrypted = Base64.decodeBase64(encryptMsg.replace("PLUS", "+").replace("sLaSh","/"));
            decrptyresult = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
        return decrptyresult;
    }

    /* Encrypt message with AES*/
    public static String encryptMsgWithAES(String msg, String aesKey) {
        String encrptyresult = null;
        try {
            Cipher cipher = Cipher.getInstance(Constant.AES);
            SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(aesKey), Constant.AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));
            // Encode the encryption message by BASE64
            Base64 base64 = new Base64();
            encrptyresult = base64.encodeToString(encrypted);
            encrptyresult = encrptyresult.replace("+", "PLUS").replace("/","sLaSh");
        } catch (Exception e) {

        }
        return encrptyresult;
    }

    public static void main(String[] args) {
        String raw = "levi.ding";
        System.out.println("AES");
        System.out.println(encryptMsgWithAES(raw, Constant.AES_KEY_LOGIN));
        String pwd= "Aa123456";
        System.out.println("AES");
        System.out.println(encryptMsgWithAES(pwd, Constant.AES_KEY_LOGIN));
        System.out.println("MD5");
        System.out.println(MD5Util.md5("levi.ding||Aa123456" + Constant.FACE_MD5_LOGIN));
    }

}

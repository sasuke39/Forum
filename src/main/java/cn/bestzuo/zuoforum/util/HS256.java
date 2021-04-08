package cn.bestzuo.zuoforum.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class HS256 {

    public static String returnSign(String message) {
        String hash = "";
        String secret = "mystar";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
            sha256_HMAC.init(secret_key);
            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            hash="";
        }
        return hash;
    }

    public static void main(String[] args) {
        String s = returnSign("123456");
        System.out.println(s);
    }
}
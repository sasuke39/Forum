package cn.bestzuo.zuoforum.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
public class HS256 {

    public static String returnSign(String message) {
        String hash = "";
        //别人篡改数据，但是签名的密匙是在服务器存储，密匙不同，生成的sign也不同。
        //所以根据sign的不同就可以知道是否篡改了数据。
        String secret = "mystar";//密匙
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(),"HmacSHA256");
            sha256_HMAC.init(secret_key);
            hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
//            System.out.println(message+"#####"+hash);
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
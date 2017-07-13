package com.cc.api.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AESEncoder {

    private static final String ALGO = "AES";
    private static final String UTF_8 = "UTF-8";
    private static final String SHA_1 = "SHA-1";
    private static final String AESEncoderKey = "ILUKx93IfWDTs70EKVi32VuFwp0HiXx7xegzs5ISfwI=";

    /*
        http://www.digitalsanctuary.com/aes-key-generator.php
        Base64 Encoded AES-256 Key: ILUKx93IfWDTs70EKVi32VuFwp0HiXx7xegzs5ISfwI=
    */
    private static Key generateKey() throws Exception {
        byte[] keyValue = AESEncoderKey.getBytes(UTF_8);
        MessageDigest sha = MessageDigest.getInstance(SHA_1);
        keyValue = sha.digest(keyValue);
        keyValue = Arrays.copyOf(keyValue, 16); // use only first 128 bit
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }

    public static String encryptAndUrlEncode(String data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        // encrypt
        byte[] encVal = c.doFinal(data.getBytes(UTF_8));
        // url encode
        return Base64.getUrlEncoder().encodeToString(encVal);
    }

    public static String decryptAndUrlDecode(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        // url decode
        byte[] decVal = Base64.getUrlDecoder().decode(encryptedData);
        // decrypt
        decVal = c.doFinal(decVal);
        return new String(decVal, UTF_8);
    }
}

package com.nus.team4.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

// this class will be only used for (showing) generating the key and will be deleted in the production development
public class GenerateSecretKey {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // for example
        SecretKey secretKey = keyGen.generateKey();

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Base64 Encoded Secret Key: " + encodedKey);
    }
}

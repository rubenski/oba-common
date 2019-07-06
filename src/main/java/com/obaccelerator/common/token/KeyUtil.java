package com.obaccelerator.common.token;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {

    public static PublicKey stringToRsaPublicKey(String rsaKeyString) {
        String cleaned = rsaKeyString.replaceAll("\\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .trim();

        try {
            KeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(cleaned.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new InvalidKeyException(e);
        }
    }
}

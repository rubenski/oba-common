package com.obaccelerator.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class CryptoUtilTest {

    @Test
    public void testGcm() throws Exception {
        String plainText = "Zomaar wat";
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[16];
        secureRandom.nextBytes(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128 * Byte.SIZE, "kdjdjlwqijowljdlqi".getBytes());
        byte[] iv = parameterSpec.getIV();
        byte[] encrypted = CryptoUtil.encryptGcm(plainText.getBytes(), secretKeySpec, iv);
        String s = CryptoUtil.decryptGcm(encrypted, secretKeySpec, iv);
        assertEquals("Zomaar wat", s);
    }
}
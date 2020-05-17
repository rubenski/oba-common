package com.obaccelerator.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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
        CryptoUtil.EncryptionResult result = CryptoUtil.encryptGcm(plainText.getBytes(), secretKeySpec);
        String s = CryptoUtil.decryptGcm(result.getCipherText(), secretKeySpec, result.getIv());
        log.info(new String(result.getIv()));
        assertEquals("Zomaar wat", s);
    }
}
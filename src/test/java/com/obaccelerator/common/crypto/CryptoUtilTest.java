package com.obaccelerator.common.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
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

    @Test
    public void testGcm2() throws Exception {
        String plainText = "Zomaar wat";
        byte[] key = "PeShVmYp3s6v9y$B&E)H@McQfTjWnZr4".getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        CryptoUtil.EncryptionResult result = CryptoUtil.encryptGcm(plainText.getBytes(), secretKeySpec);

        String iv = new String(result.getIv(), "ISO-8859-1");
        byte[] bytes = iv.getBytes("ISO-8859-1");

        String s = CryptoUtil.decryptGcm(result.getCipherText(), secretKeySpec, bytes);
        log.info(new String(result.getIv()));
        assertEquals("Zomaar wat", s);
    }


}

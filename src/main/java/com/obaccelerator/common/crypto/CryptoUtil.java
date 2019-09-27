package com.obaccelerator.common.crypto;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class CryptoUtil {

    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    // Output contains the IV in the first block
    public static byte[] encryptCbc(SecretKey secretKey, String plainText) {
        try {
            byte[] ciphertext = null;
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();
            byte[] initVector = new byte[blockSize];
            (new SecureRandom()).nextBytes(initVector);
            IvParameterSpec ivSpec = new IvParameterSpec(initVector);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encoded = plainText.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            ciphertext = new byte[initVector.length + cipher.getOutputSize(encoded.length)];
            for (int i = 0; i < initVector.length; i++) {
                ciphertext[i] = initVector[i];
            }
            // Perform encryption
            cipher.doFinal(encoded, 0, encoded.length, ciphertext, initVector.length);
            return ciphertext;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    // Assumes the IV is present in the first block of the cipherText
    public static String decryptCbc(String secretKey, String cipherText) {
        try {
            byte[] cipherTextBytes = cipherText.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            final int blockSize = cipher.getBlockSize();
            byte[] initVector = Arrays.copyOfRange(cipherTextBytes, 0, blockSize);
            IvParameterSpec ivSpec = new IvParameterSpec(initVector);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            byte[] plaintext = cipher.doFinal(cipherTextBytes, blockSize, cipherTextBytes.length - blockSize);
            return new String(plaintext);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public SecretKey generateSymmetricAesKey() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(AES_KEY_SIZE);
        return keyGenerator.generateKey();
    }

    public byte[] generateIV() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    public static byte[] encrypt(byte[] plaintext, SecretKey key, byte[] iv) throws Exception {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        // Perform Encryption
        return cipher.doFinal(plaintext);
    }

    public static String decrypt(byte[] cipherText, SecretKey key, byte[] IV) throws Exception {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }

    // TODO: refactor : code below always uses an RSA key factory. Fine for now, but if we get non-RSA keys this should be fixed
    public static PrivateKey stringToPrivateKey(String privateKeyString) {
        byte[] plainTextKey = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(plainTextKey);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while creating PrivateKey from String");
        }
    }

    public static Certificate stringToCertificate(String certificateString) {
        if (certificateString.isEmpty()) {
            throw new IllegalArgumentException("Cannot create java.security.cert.Certificate from empty String");
        }
        InputStream is = new ByteArrayInputStream(certificateString.getBytes());
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return cf.generateCertificate(is);
        } catch (CertificateException e) {
            throw new RuntimeException("Could not convert String certificate to java.security.cert.Certificate");
        }
    }
}

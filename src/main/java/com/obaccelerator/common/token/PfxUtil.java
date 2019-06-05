package com.obaccelerator.common.token;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

public class PfxUtil {

    private KeyStore keyStore;
    private String pfxPassword;


    public PfxUtil(final String pfxPath, final String pfxPassword) {
        this.pfxPassword = pfxPassword;
        keyStore = loadKeyStore(pfxPath, pfxPassword);
    }

    public Certificate getCertificate(String alias) {
        try {
            return keyStore.getCertificate(alias);
        } catch (Exception e) {
            throw new RuntimeException("Could not load certificate", e);
        }
    }


    public PublicKey getPublicKey(String alias) {
        return getCertificate(alias).getPublicKey();
    }

    public static KeyStore loadKeyStore(String pfxPath, String pfxPassword) {
        InputStream keyFile = ObaAccessTokenCreator.class.getResourceAsStream(pfxPath);
        if (keyFile == null) {
            throw new RuntimeException("Could not find " + pfxPath);
        }

        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("PKCS12", "SunJSSE");
            ks.load(keyFile, pfxPassword.toCharArray());
            return ks;
        } catch (KeyStoreException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public Key getPrivateKey(String alias) {
        try {
            return keyStore.getKey(alias, pfxPassword.toCharArray());
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            throw new RuntimeException("Could not load private key", e);
        }
    }
}

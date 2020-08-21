package com.obaccelerator.common.http;

import lombok.Builder;
import lombok.Getter;
import org.apache.http.Consts;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CodingErrorAction;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Borrowed from https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientConfiguration.java
 */
public class ApacheHttpsClientFactory {

    private static String KEYSTORE_TYPE_PKCS12 = "PKCS12";

    @Builder
    @Getter
    public static class HttpsClientFactoryInput {
        private int defaultPoolSize;
        private int maxPoolSize;
        private long connectTimeOut;
        private long socketTimeOut;
        private String keyStoreClassPath;
        private String keyStorePw;
        private String trustStoreClassPath;
        private String trustStorePw;
        private boolean trustSelfSigned;
        private KeyStore keyStore;

        boolean isMtls() {
            return keyStoreClassPath != null || keyStore != null;
        }
    }

    public static CloseableHttpClient getHttpsClient(HttpsClientFactoryInput input) {

        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory();
        SSLContext sslContext = createSslContext(input);

        // Create a registry of custom connection socket factories for supported protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();

        // Create a connection manager with custom configuration.
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory);


        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();

        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connectionManager.setDefaultSocketConfig(socketConfig);

        // Validate connections after 1 sec of inactivity
        connectionManager.setValidateAfterInactivity(1000);

        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .build();

        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connectionManager.setDefaultConnectionConfig(connectionConfig);

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connectionManager.setMaxTotal(input.getMaxPoolSize());
        connectionManager.setDefaultMaxPerRoute(input.getDefaultPoolSize());

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    private static SSLContext createSslContext(HttpsClientFactoryInput input) {
        SSLContext sslContext = null;
        SSLContextBuilder sslContextBuilder = SSLContexts.custom();

        try {
            if (input.isMtls()) {
                // Load the keystore with client cert and private key. This will be an eIDAS client cert and key in PSD2 scenarios.
                sslContextBuilder.loadKeyMaterial(getKeyStore(input), input.getKeyStorePw() == null ? null : input.getKeyStorePw().toCharArray());
            }

            // Load the trusted servers
            if (!input.isTrustSelfSigned()) {
                KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE_PKCS12);
                trustStore.load(getClassPathFileAsStream(input.getTrustStoreClassPath()), input.getTrustStorePw().toCharArray());
                sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());
                sslContext = sslContextBuilder.build();
            } else {
                sslContext = sslContextBuilder
                        .loadTrustMaterial(new TrustSelfSignedStrategy())
                        .build();
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
        return sslContext;
    }

    private static KeyStore getKeyStore(HttpsClientFactoryInput input) {
        if (input.getKeyStore() != null) {
            return input.getKeyStore();
        } else {
            try (InputStream stream = getClassPathFileAsStream(input.getKeyStoreClassPath())) {
                if (stream == null) {
                    throw new IllegalArgumentException("Keystore not found at " + input.keyStoreClassPath);
                }
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(stream, input.getKeyStorePw().toCharArray());
                return keyStore;
            } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
                throw new IllegalArgumentException("Could not find or read keystore", e);
            }
        }
    }

    private static InputStream getClassPathFileAsStream(String classPath) {
        InputStream resourceAsStream = ApacheHttpsClientFactory.class.getResourceAsStream(classPath);
        return resourceAsStream;
    }
}


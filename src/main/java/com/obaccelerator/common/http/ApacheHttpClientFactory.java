package com.obaccelerator.common.http;

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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;
import java.security.*;

/**
 * Borrowed from https://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientConfiguration.java
 */
public class ApacheHttpClientFactory {

    public static CloseableHttpClient getHttpsClient(long connectTimeOut, long socketTimeOut) {
        return getHttpsClient(null, connectTimeOut, socketTimeOut);
    }

    public static CloseableHttpClient getHttpsClient(KeyStore keyStore, long connectTimeOut, long socketTimeOut) {

        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory();

        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = null;
        try {
            if (keyStore != null) {
                sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, null).build();
            } else {
                sslcontext = SSLContexts.createSystemDefault();
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();

        // Create a connection manager with custom configuration.
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connFactory);;

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
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);


        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }


}


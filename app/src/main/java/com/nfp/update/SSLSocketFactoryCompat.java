package com.nfp.update;

/**
 * @author MR.ZHANG
 * @create 2018-06-07
 */

import android.os.Build;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class SSLSocketFactoryCompat extends javax.net.ssl.SSLSocketFactory {
    private javax.net.ssl.SSLSocketFactory defaultFactory;
    // Android 5.0+ (API level21) provides reasonable default settings
    // but it still allows SSLv3
    // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
    static String protocols[] = null, cipherSuites[] = null;
    static {
        try {
            javax.net.ssl.SSLSocket socket = (javax.net.ssl.SSLSocket) javax.net.ssl.SSLSocketFactory.getDefault().createSocket();
            if (socket != null) {
                /* set reasonable protocol versions */
                // - enable all supported protocols (enables TLSv1.1 and TLSv1.2 on Android <5.0)
                // - remove all SSL versions (especially SSLv3) because they're insecure now
                java.util.List<String> protocols = new java.util.LinkedList<>();
                for (String protocol : socket.getSupportedProtocols())
                    if (!protocol.toUpperCase().contains("SSL"))
                        protocols.add(protocol);
                com.nfp.update.SSLSocketFactoryCompat.protocols = protocols.toArray(new String[protocols.size()]);
                /* set up reasonable cipher suites */
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // choose known secure cipher suites
                    java.util.List<String> allowedCiphers = java.util.Arrays.asList(
                            // TLS 1.2
                            "TLS_RSA_WITH_AES_256_GCM_SHA384",
                            "TLS_RSA_WITH_AES_128_GCM_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
                            "TLS_ECHDE_RSA_WITH_AES_128_GCM_SHA256",
                            // maximum interoperability
                            "TLS_RSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_RSA_WITH_AES_128_CBC_SHA",
                            // additionally
                            "TLS_RSA_WITH_AES_256_CBC_SHA",
                            "TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                            "TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
                            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA");
                    java.util.List<String> availableCiphers = java.util.Arrays.asList(socket.getSupportedCipherSuites());
                    // take all allowed ciphers that are available and put them into preferredCiphers
                    java.util.HashSet<String> preferredCiphers = new java.util.HashSet<>(allowedCiphers);
                    preferredCiphers.retainAll(availableCiphers);
                    /* For maximum security, preferredCiphers should *replace* enabled ciphers (thus disabling
                     * ciphers which are enabled by default, but have become unsecure), but I guess for
                     * the security level of DAVdroid and maximum compatibility, disabling of insecure
                     * ciphers should be a server-side task */
                    // add preferred ciphers to enabled ciphers
                    java.util.HashSet<String> enabledCiphers = preferredCiphers;
                    enabledCiphers.addAll(new java.util.HashSet<>(java.util.Arrays.asList(socket.getEnabledCipherSuites())));
                    com.nfp.update.SSLSocketFactoryCompat.cipherSuites = enabledCiphers.toArray(new String[enabledCiphers.size()]);
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
    public SSLSocketFactoryCompat(javax.net.ssl.X509TrustManager tm) {
        try {
            javax.net.ssl.SSLContext sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
            sslContext.init(null, (tm != null) ? new javax.net.ssl.X509TrustManager[] { tm } : null, null);
            defaultFactory = sslContext.getSocketFactory();
        } catch (java.security.GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }
    private void upgradeTLS(javax.net.ssl.SSLSocket ssl) {
        // Android 5.0+ (API level21) provides reasonable default settings
        // but it still allows SSLv3
        // https://developer.android.com/about/versions/android-5.0-changes.html#ssl
        if (protocols != null) {
            ssl.setEnabledProtocols(protocols);
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP && cipherSuites != null) {
            ssl.setEnabledCipherSuites(cipherSuites);
        }
    }
    @Override
    public String[] getDefaultCipherSuites() {
        return cipherSuites;
    }
    @Override
    public String[] getSupportedCipherSuites() {
        return cipherSuites;
    }
    @Override
    public java.net.Socket createSocket(java.net.Socket s, String host, int port, boolean autoClose) throws java.io.IOException {
        java.net.Socket ssl = defaultFactory.createSocket(s, host, port, autoClose);
        if (ssl instanceof javax.net.ssl.SSLSocket)
            upgradeTLS((javax.net.ssl.SSLSocket)ssl);
        return ssl;
    }
    @Override
    public java.net.Socket createSocket(String host, int port) throws java.io.IOException, java.net.UnknownHostException {
        java.net.Socket ssl = defaultFactory.createSocket(host, port);
        if (ssl instanceof javax.net.ssl.SSLSocket)
            upgradeTLS((javax.net.ssl.SSLSocket)ssl);
        return ssl;
    }
    @Override
    public java.net.Socket createSocket(String host, int port, java.net.InetAddress localHost, int localPort) throws java.io.IOException, java.net.UnknownHostException {
        java.net.Socket ssl = defaultFactory.createSocket(host, port, localHost, localPort);
        if (ssl instanceof javax.net.ssl.SSLSocket)
            upgradeTLS((javax.net.ssl.SSLSocket)ssl);
        return ssl;
    }
    @Override
    public java.net.Socket createSocket(java.net.InetAddress host, int port) throws java.io.IOException {
        java.net.Socket ssl = defaultFactory.createSocket(host, port);
        if (ssl instanceof javax.net.ssl.SSLSocket)
            upgradeTLS((javax.net.ssl.SSLSocket)ssl);
        return ssl;
    }
    @Override
    public java.net.Socket createSocket(java.net.InetAddress address, int port, java.net.InetAddress localAddress, int localPort) throws java.io.IOException {
        java.net.Socket ssl = defaultFactory.createSocket(address, port, localAddress, localPort);
        if (ssl instanceof javax.net.ssl.SSLSocket)
            upgradeTLS((javax.net.ssl.SSLSocket)ssl);
        return ssl;
    }

    @Override
    public java.net.Socket createSocket() throws java.io.IOException {
        java.net.Socket ssl = defaultFactory.createSocket();
        if (ssl instanceof javax.net.ssl.SSLSocket)
            upgradeTLS((javax.net.ssl.SSLSocket)ssl);
        return ssl;
    }
}

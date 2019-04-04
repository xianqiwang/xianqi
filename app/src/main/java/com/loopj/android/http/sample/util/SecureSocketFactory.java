/*
    Android Asynchronous Http Client Sample
    Copyright (c) 2014 Marek Sebera <marek.sebera@gmail.com>
    https://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.loopj.android.http.sample.util;

import android.os.Build;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * A class to authenticate a secured connection against a custom CA using a BKS store.
 *
 * @author Noor Dawod <github@fineswap.com>
 */
public class SecureSocketFactory extends cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory {

    private static final String LOG_TAG = "SecureSocketFactory";

    private final javax.net.ssl.SSLContext sslCtx;
    private final java.security.cert.X509Certificate[] acceptedIssuers;

    /**
     * Instantiate a new secured factory pertaining to the passed store. Be sure to initialize the
     * store with the password using {@link java.security.KeyStore#load(java.io.InputStream,
     * char[])} method.
     *
     * @param store The key store holding the certificate details
     * @param alias The alias of the certificate to use
     */
    public SecureSocketFactory(java.security.KeyStore store, String alias)
            throws
            java.security.cert.CertificateException,
            java.security.NoSuchAlgorithmException,
            java.security.KeyManagementException,
            java.security.KeyStoreException,
            java.security.UnrecoverableKeyException {

        super(store);

        // Loading the CA certificate from store.
        final java.security.cert.Certificate rootca = store.getCertificate(alias);

        // Turn it to X509 format.
        java.io.InputStream is = new java.io.ByteArrayInputStream(rootca.getEncoded());
        java.security.cert.X509Certificate x509ca = (java.security.cert.X509Certificate) java.security.cert.CertificateFactory.getInstance("X.509").generateCertificate(is);
        com.loopj.android.http.AsyncHttpClient.silentCloseInputStream(is);

        if (null == x509ca) {
            throw new java.security.cert.CertificateException("Embedded SSL certificate has expired.");
        }

        // Check the CA's validity.
        x509ca.checkValidity();

        // Accepted CA is only the one installed in the store.
        acceptedIssuers = new java.security.cert.X509Certificate[]{x509ca};

        sslCtx = javax.net.ssl.SSLContext.getInstance("TLS");
        sslCtx.init(
                null,
                new javax.net.ssl.TrustManager[]{
                        new javax.net.ssl.X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                                Exception error = null;

                                if (null == chain || 0 == chain.length) {
                                    error = new java.security.cert.CertificateException("Certificate chain is invalid.");
                                } else if (null == authType || 0 == authType.length()) {
                                    error = new java.security.cert.CertificateException("Authentication type is invalid.");
                                } else {
                                    android.util.Log.i(LOG_TAG, "Chain includes " + chain.length + " certificates.");
                                    try {
                                        for (java.security.cert.X509Certificate cert : chain) {
                                            android.util.Log.i(LOG_TAG, "Server Certificate Details:");
                                            android.util.Log.i(LOG_TAG, "---------------------------");
                                            android.util.Log.i(LOG_TAG, "IssuerDN: " + cert.getIssuerDN().toString());
                                            android.util.Log.i(LOG_TAG, "SubjectDN: " + cert.getSubjectDN().toString());
                                            android.util.Log.i(LOG_TAG, "Serial Number: " + cert.getSerialNumber());
                                            android.util.Log.i(LOG_TAG, "Version: " + cert.getVersion());
                                            android.util.Log.i(LOG_TAG, "Not before: " + cert.getNotBefore().toString());
                                            android.util.Log.i(LOG_TAG, "Not after: " + cert.getNotAfter().toString());
                                            android.util.Log.i(LOG_TAG, "---------------------------");

                                            // Make sure that it hasn't expired.
                                            cert.checkValidity();

                                            // Verify the certificate's public key chain.
                                            cert.verify(rootca.getPublicKey());
                                        }
                                    } catch (java.security.InvalidKeyException e) {
                                        error = e;
                                    } catch (java.security.NoSuchAlgorithmException e) {
                                        error = e;
                                    } catch (java.security.NoSuchProviderException e) {
                                        error = e;
                                    } catch (java.security.SignatureException e) {
                                        error = e;
                                    }
                                }
                                if (null != error) {
                                    android.util.Log.e(LOG_TAG, "Certificate error", error);
                                    throw new java.security.cert.CertificateException(error);
                                }
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return acceptedIssuers;
                            }
                        }
                },
                null
        );

        setHostnameVerifier(cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
    }

    @Override
    public java.net.Socket createSocket(java.net.Socket socket, String host, int port, boolean autoClose)
            throws java.io.IOException {

        injectHostname(socket, host);
        java.net.Socket sslSocket = sslCtx.getSocketFactory().createSocket(socket, host, port, autoClose);

        // throw an exception if the hostname does not match the certificate
        getHostnameVerifier().verify(host, (javax.net.ssl.SSLSocket) sslSocket);

        return sslSocket;
    }

    @Override
    public java.net.Socket createSocket() throws java.io.IOException {
        return sslCtx.getSocketFactory().createSocket();
    }

    /**
     * Pre-ICS Android had a bug resolving HTTPS addresses. This workaround fixes that bug.
     *
     * @param socket The socket to alter
     * @param host   Hostname to connect to
     * @see <a href="https://code.google.com/p/android/issues/detail?id=13117#c14">https://code.google.com/p/android/issues/detail?id=13117#c14</a>
     */
    private void injectHostname(java.net.Socket socket, String host) {
        try {
            if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 4) {
                java.lang.reflect.Field field = java.net.InetAddress.class.getDeclaredField("hostName");
                field.setAccessible(true);
                field.set(socket.getInetAddress(), host);
            }
        } catch (Exception ignored) {
        }
    }
}

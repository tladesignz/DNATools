/*
  DNA Android Tools.

  The MIT License (MIT)

  Copyright (c) 2015 - 2017 Die Netzarchitekten e.U., Benjamin Erhart

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
 */
package com.netzarchitekten.tools.security;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * <p>
 * A meta-trust manager, which can check multiple other {@link TrustManager}s, until one of them
 * actually trusts.
 * </p>
 * <p>
 * This is helpful, if you work with servers, which have a proper certificate chain to an official
 * CA which is Android's default key store, and, at the same time, work with servers, which don't
 * have that.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 * @see <a href="http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-httpclient-over-https/6378872#6378872"
 * >Stack Overflow: Trusting all certificates using HttpClient over HTTPS</a>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MultiTrustManager implements X509TrustManager {

    protected final List<X509TrustManager> mX509TrustManagers = new ArrayList<>();

    /**
     * Create a trust manager, which can use multiple key stores to check trust of a certificate
     * chain.
     *
     * @param useDefault
     *            Set to true, if the Android built-in keychain should also be used.
     * @param additionalKeyStores
     *            One ore more keystores to check against.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the default
     *            algorithm.
     * @throws KeyStoreException
     *            if a {@link TrustManager} could not be initialized for a given {@link KeyStore}.
     */
    public MultiTrustManager(boolean useDefault, KeyStore... additionalKeyStores)
        throws NoSuchAlgorithmException, KeyStoreException {

        List<TrustManagerFactory> factories = new ArrayList<>();

        if (useDefault) {
            factories.add(factoryFromKeyStore(null));
        }

        for (KeyStore k : additionalKeyStores) {
            factories.add(factoryFromKeyStore(k));
        }

        for (TrustManagerFactory tmf : factories) {
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    mX509TrustManagers.add((X509TrustManager) tm);
                }
            }
        }
    }

    /**
     * <p>
     * Create a trust manager, which can use multiple key stores to check trust of a certificate
     * chain.
     * </p>
     * <p>
     * Using this constructor, the Android built-in keychain will also be used.
     * </p>
     *
     * @param additionalKeyStores
     *            One ore more keystores to check against.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the default
     *            algorithm.
     * @throws KeyStoreException
     *            if a {@link TrustManager} could not be initialized for a given {@link KeyStore}.
     */
    public MultiTrustManager(KeyStore... additionalKeyStores)
        throws NoSuchAlgorithmException, KeyStoreException {

        this(true, additionalKeyStores);
    }

    /**
     * <p>
     * Given the partial or complete certificate chain provided by the peer, build a certificate
     * path to a trusted root and return if it can be validated and is trusted for client SSL
     * authentication based on the authentication type.
     * </p>
     * <p>
     * The authentication type is determined by the actual certificate used. For instance, if
     * RSAPublicKey is used, the authType should be "RSA". Checking is case-sensitive.
     * </p>
     *
     * @param chain
     *            the peer certificate chain
     * @param authType
     *            the authentication type based on the client certificate
     * @throws IllegalArgumentException
     *            if null or zero-length chain is passed in for the chain parameter or if null or
     *            zero-length string is passed in for the authType parameter.
     * @throws CertificateException
     *            if the certificate chain is not trusted by this TrustManager.
     */
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager tm : mX509TrustManagers) {
            try {
                tm.checkClientTrusted(chain, authType);
                return;
            } catch (CertificateException e) {
                // Ignore, so we can loop until we find a trusting one.
            }
        }

        // All searched, no trust could be established.
        throw new CertificateException(String.format(
            "No TrustManager found to trust this chain with authType \"%s\":\n%s",
            authType, Arrays.toString(chain)));
    }

    /**
     * <p>
     * Given the partial or complete certificate chain provided by the peer, build a certificate
     * path to a trusted root and return if it can be validated and is trusted for server SSL
     * authentication based on the authentication type.
     * </p>
     * <p>
     * The authentication type is the key exchange algorithm portion of the cipher suites
     * represented as a String, such as "RSA", "DHE_DSS".
     * </p>
     * <p>
     * Note: for some exportable cipher suites, the key exchange algorithm is determined at run
     * time during the handshake. For instance, for TLS_RSA_EXPORT_WITH_RC4_40_MD5, the authType
     * should be RSA_EXPORT when an ephemeral RSA key is used for the key exchange, and RSA when
     * the key from the server certificate is used. Checking is case-sensitive.
     * </p>
     *
     * @param chain
     *            the peer certificate chain
     * @param authType
     *            the key exchange algorithm used
     * @throws IllegalArgumentException
     *            if null or zero-length chain is passed in for the chain parameter or if null or
     *            zero-length string is passed in for the authType parameter.
     * @throws CertificateException
     *            if the certificate chain is not trusted by this TrustManager.
     */
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for (X509TrustManager tm : mX509TrustManagers) {
            try {
                tm.checkServerTrusted(chain, authType);
                return;
            } catch (CertificateException e) {
                // Ignore, so we can loop until we find a trusting one.
            }
        }

        // All searched, no trust could be established.
        throw new CertificateException(String.format(
            "No TrustManager found to trust this chain with authType \"%s\":\n%s",
            authType, Arrays.toString(chain)));
    }

    /**
     * Return an array of certificate authority certificates which are trusted for authenticating
     * peers.
     *
     * @return a non-null (possibly empty) array of acceptable CA issuer certificates.
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        List<X509Certificate> certificates = new ArrayList<>();

        for (X509TrustManager tm : mX509TrustManagers) {
            certificates.addAll(Arrays.asList(tm.getAcceptedIssuers()));
        }

        return certificates.toArray(new X509Certificate[certificates.size()]);
    }

    /**
     * @param keyStore
     *            A {@link KeyStore} or NULL.
     * @return a {@link TrustManagerFactory} using the given keystore, or the default keystore if
     *            argument is NULL.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the default
     *            algorithm.
     * @throws KeyStoreException
     *            if default algorithm is null.
     */
    private static TrustManagerFactory factoryFromKeyStore(KeyStore keyStore)
        throws NoSuchAlgorithmException, KeyStoreException {

        TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore == null ? null : keyStore.getUnderlyingKeyStore());

        return tmf;
    }
}

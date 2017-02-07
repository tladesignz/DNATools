/**
 * DNA Android Tools.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Die Netzarchitekten e.U., Benjamin Erhart
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.netzarchitekten.tools.security;

import android.content.Context;

import com.netzarchitekten.tools.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseAuthenticator {

    protected final Context mContext;

    /**
     * Cached socket factory. This is a lengthy operation, therefore, we cache
     * the result!
     */
    protected SSLSocketFactory mSocketFactory = null;

    /**
     * Cache for the keystore containing all public certificates the servers use.
     */
    protected KeyStore mServerCertKeyStore = null;

    /**
     * Cache for the keystore containing the user's public cert and private key.
     */
    protected KeyStore mUserCertKeyStore = null;

    protected BaseAuthenticator(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * Call {@link #build()} here, catch all exceptions and return one unified exception
     * with the appropriate error messages, so you don't have to handle 6+ different exceptions
     * all over the place.
     *
     * @return the {@link SSLSocketFactory}.
     * @throws CertificateException
     *            Throw this exception as a unified one, probably with an already translated
     *            message to show in the UI somewhere else.
     */
    public abstract SSLSocketFactory getFactory() throws CertificateException;

    /**
     * @return a list of filenames of certificate files to load, stored in the "assets" folder of
     * the app package or NULL, if you don't want to use the server certificate authentication
     * feature and use Android's default integrated root certificates for that, instead.
     */
    protected abstract String[] getCertFiles();

    /**
     * @return an open {@link InputStream} on the user's certificate file or NULL, if you don't
     * want to use the client authentication feature.
     * @throws IOException
     *            if the file cannot be found or any other read error occurs.
     */
    protected abstract InputStream getUserCertificate() throws IOException;

    /**
     * <p>
     * Override this to use a {@link MultiTrustManager} using the default Android keystore AND the
     * provided certificates.
     * </p>
     * <p>
     * DEFAULT is <b>false</b>.
     * </p>
     *
     * @return true, if a {@link MultiTrustManager} should be used, or false if not.
     */
    protected boolean useMultiTrustManager() {
        return false;
    }

    /**
     * <p>
     * Override this to define your own list of supported SSL/TLS protocols.
     * </p>
     * <p>
     * This can be used to switch on TLS 1.1 and 1.2 on Android API 16 - 19, which have that
     * disabled by default.
     * </p>
     *
     * @return a list of supported protocols.
     * @see TlsSocketFactory
     */
    protected String[] getSupportedProtocols() {
        return null;
    }

    /**
     * <p>
     * Return the user certificate password.
     * </p>
     * <p>
     * This method will never be called, if {@link #getUserCertificate()} returns NULL!
     * </p>
     *
     * @return the password for the user certificate as returned in {@link #getUserCertificate()}.
     * @throws UnrecoverableKeyException
     *            if the password is invalid (e.g. because it's empty.)
     */
    protected abstract String getUserCertificatePassword() throws UnrecoverableKeyException;

    /**
     * The main workhorse. Use the result of this in {@link #getFactory()}, where you can
     * catch all exceptions and return appropriate error messages.
     *
     * @return the {@link SSLSocketFactory} for use in {@link #getFactory()}.
     * @throws NoSuchAlgorithmException
     *            if TLS is not supported by the system, if there is no valid default algorithm for
     *            {@link TrustManager}s, if there is no valid default algorithm for
     *            {@link KeyManager}s or if the algorithm used to check the integrity of the
     *            keystore cannot be found.
     * @throws KeyManagementException
     *            if the {@link SSLContext} cannot be initialized.
     * @throws KeyStoreException
     *            if a {@link TrustManager} could not be initialized or if there is no valid
     *            default algorithm for {@link KeyStore}s,
     *            if the system does not support PKCS12 key stores or if the {@link KeyManager}
     *            could not be initialized.
     * @throws CertificateException
     *            if the system does not support X.509 certificates, if any of the certificates in
     *            the keystore could not be loaded or if certificate files could not be parsed.
     * @throws IOException
     *            if a certificate file could not be read,
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     * @throws UnrecoverableKeyException
     *            if the private key cannot be recovered (e.g. the given password is wrong).
     */
    protected SSLSocketFactory build() throws NoSuchAlgorithmException, KeyManagementException,
        KeyStoreException, CertificateException, IOException, UnrecoverableKeyException {

        if (mSocketFactory == null) {
            TrustManager[] tms = getCertFiles() == null
                ? null
                : (useMultiTrustManager()
                    ? getServerCertKeyStore().getMultiTrustManager()
                    : getServerCertKeyStore().getTrustManagers());

            KeyManager[] kms = getUserCertificate() == null
                ? null
                : getUserCertKeyStore().getKeyManagers();

            mSocketFactory = new TlsSocketFactory(getSupportedProtocols(), kms, tms);
        }

        return mSocketFactory;
    }

    /**
     * Create a keystore from the certificates in the "assets" folder defined in
     * {@link #getCertFiles()}.
     *
     * @return a keystore containing all certificates.
     * @throws CertificateException
     *            if the system does not support X.509 certificates, if any of the certificates in
     *            the keystore could not be loaded or if certificate files could not be parsed.
     * @throws KeyStoreException
     *            if there is no valid default algorithm for {@link KeyStore}s.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if a certificate file could not be read,
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an UnrecoverableKeyException.
     */
    protected KeyStore getServerCertKeyStore()
        throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException {
        if (mServerCertKeyStore == null) {
            mServerCertKeyStore = new KeyStore();

            // Load all certificates and add to a keystore.
            for (String file : getCertFiles()) {
                mServerCertKeyStore.addX509CertificateFromAssets(mContext, file);
            }
        }

        return mServerCertKeyStore;
    }

    /**
     * Create a keystore from the user's certificate file containing a private
     * key and a public certificate (which also contains the public key).
     *
     * @return a PKCS12 keystore from the user's certificate file.
     * @throws KeyStoreException
     *            if the system does not support PKCS12 key stores.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     * @throws UnrecoverableKeyException
     *            if the password is invalid (e.g. because it's empty.)
     */
    protected KeyStore getUserCertKeyStore() throws CertificateException, KeyStoreException,
        NoSuchAlgorithmException, IOException, UnrecoverableKeyException {

        InputStream certificate = getUserCertificate();
        String password = getUserCertificatePassword();

        if (mUserCertKeyStore == null) {
            mUserCertKeyStore = new KeyStore(certificate, password);
        }

        FileUtils.close(certificate);

        return mUserCertKeyStore;
    }
}

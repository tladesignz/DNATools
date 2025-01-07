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

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * <p>
 * A wrapper class to help create an empty {@link java.security.KeyStore} or from a complete PKCS12
 * keystore file, add certificates from Android's asset folder or from strings to it and create
 * {@link TrustManager}s and {@link KeyManager}s from it.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue", "SameParameterValue"})
public class KeyStore {

    public static final String TYPE_PKCS12 = "PKCS12";

    private final java.security.KeyStore mKeyStore;

    /**
     * Create a {@link KeyStore} from an {@link InputStream}.
     *
     * @param keyStoreType
     *            A keystore type suitable for {@link java.security.KeyStore#getInstance(String)}.
     *            May be NULL, in which case the {@link java.security.KeyStore#getDefaultType()}
     *            will be used.
     * @param keyStore
     *            The {@link InputStream} of a {@link KeyStore} file. May be NULL, in which case,
     *            an empty key store will be created.
     * @param password
     *            The password for the keystore. May be NULL.
     * @throws KeyStoreException
     *            if no Provider supports a KeyStoreSpi implementation for the specified type.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     */
    public KeyStore(String keyStoreType, InputStream keyStore, char[] password)
        throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        mKeyStore = java.security.KeyStore.getInstance(keyStoreType == null
            ? java.security.KeyStore.getDefaultType()
            : keyStoreType);

        mKeyStore.load(keyStore, password);
    }

    /**
     * Create a {@link KeyStore} from an {@link InputStream} pointing to a file in PKCS12 format.
     *
     * @param keyStore
     *            The {@link InputStream} of a {@link KeyStore} file. May be NULL, in which case,
     *            an empty key store will be created.
     * @param password
     *            The password for the keystore. May be NULL.
     * @throws KeyStoreException
     *            if no Provider supports a KeyStoreSpi implementation for the PKCS12 type.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     */
    public KeyStore(InputStream keyStore, char[] password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(TYPE_PKCS12, keyStore, password);
    }

    /**
     * Create a {@link KeyStore} from an {@link InputStream} pointing to a file in PKCS12 format.
     *
     * @param keyStore
     *            The {@link InputStream} of a {@link KeyStore} file. May be NULL, in which case,
     *            an empty key store will be created.
     * @param password
     *            The password for the keystore. May be NULL.
     * @throws KeyStoreException
     *            if no Provider supports a KeyStoreSpi implementation for the PKCS type.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     */
    public KeyStore(InputStream keyStore, String password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(keyStore, password == null ? null : password.toCharArray());
    }

    /**
     * Create a {@link KeyStore} from a {@link String} containing the keystore in PKCS12 format.
     *
     * @param keyStore
     *            The {@link KeyStore} as a {@link String}. May be NULL, in which case,
     *            an empty key store will be created.
     * @param password
     *            The password for the keystore. May be NULL.
     * @throws KeyStoreException
     *            if no Provider supports a KeyStoreSpi implementation for the PKCS type.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     */
    public KeyStore(String keyStore, String password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        //noinspection CharsetObjectCanBeUsed
        this(keyStore == null ? null : new ByteArrayInputStream(keyStore.getBytes("UTF-8")),
            password);
    }

    /**
     * Create a {@link KeyStore} from a {@link String} containing the keystore in PKCS12 format.
     *
     * @param keyStore
     *            The {@link KeyStore} as a {@link String}. May be NULL, in which case,
     *            an empty key store will be created.
     * @param password
     *            The password for the keystore. May be NULL.
     * @throws KeyStoreException
     *            if no Provider supports a KeyStoreSpi implementation for the PKCS type.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data, if a password is
     *            required but not given, or if the given password was incorrect.
     *            If the error is due to a wrong password, the cause of the IOException should be
     *            an {@link UnrecoverableKeyException}.
     */
    public KeyStore(String keyStore, char[] password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        //noinspection CharsetObjectCanBeUsed
        this(keyStore == null ? null : new ByteArrayInputStream(keyStore.getBytes("UTF-8")),
            password);
    }

    /**
     * Creates an empty {@link KeyStore} using {@link java.security.KeyStore#getDefaultType()}.
     *
     * @throws KeyStoreException
     *            if no Provider supports a KeyStoreSpi implementation for the PKCS type.
     * @throws CertificateException
     *            if any of the certificates in the keystore could not be loaded.
     * @throws NoSuchAlgorithmException
     *            if the algorithm used to check the integrity of the keystore cannot be found.
     * @throws IOException
     *            if there is an I/O or format problem with the keystore data.
     */
    public KeyStore()
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(null, null, null);
    }

    /**
     * <p>
     * Add a certificate to the {@link KeyStore} from Android's assets folder.
     * </p>
     * <p>
     * The fileName will become the certificate alias!
     * </p>
     *
     * @param context
     *            A {@link Context}.
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param fileName
     *            The file name of the app asset.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws IOException
     *            If the assets file could not be read.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addCertificateFromAssets(Context context, String certificateType,
                                                    String fileName)
        throws CertificateException, IOException, KeyStoreException {

        Certificate c = CertificateHelper.create(certificateType,
            context.getAssets().open(fileName));

        mKeyStore.setCertificateEntry(fileName, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from Android's assets folder.
     * </p>
     * <p>
     * The fileName will become the certificate alias!
     * </p>
     *
     * @param context
     *            A {@link Context}.
     * @param fileName
     *            The file name of the app asset.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws IOException
     *            If the assets file could not be read.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addX509CertificateFromAssets(Context context, String fileName)
        throws CertificateException, IOException, KeyStoreException {

        X509Certificate c = CertificateHelper.create(context.getAssets().open(fileName));

        mKeyStore.setCertificateEntry(fileName, c);

        return this;
    }

    /**
     * <p>
     * Add a certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * The provided alias will become the certificate alias!
     * </p>
     *
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param alias
     *            The designated certificate alias.
     * @param certificate
     *            The {@link String} representation of a {@link Certificate}.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addCertificateFromString(String certificateType, String alias,
                                                    String certificate)
        throws CertificateException, KeyStoreException {

        Certificate c = CertificateHelper.create(certificateType, certificate);

        mKeyStore.setCertificateEntry(alias, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * If NOT NULL, the provided alias will become the certificate alias,
     * else the certificate's hex serial will become the alias!
     * </p>
     *
     * @param alias
     *            The designated certificate alias.
     * @param certificate
     *            The {@link String} representation of a {@link Certificate}.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addX509CertificateFromString(String alias, String certificate)
        throws CertificateException, KeyStoreException {

        X509Certificate c = CertificateHelper.create(certificate);

        mKeyStore.setCertificateEntry(alias == null ? c.getSerialNumber().toString(16) : alias, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * The certificate's hex serial will become the alias!
     * </p>
     *
     * @param certificate
     *            The {@link String} representation of a {@link Certificate}.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addX509CertificateFromString(String certificate)
        throws CertificateException, KeyStoreException {
        return addX509CertificateFromString(null, certificate);
    }

    /**
     * <p>
     * Add a certificate to the {@link KeyStore} from an {@link InputStream}.
     * </p>
     * <p>
     * The provided alias will become the certificate alias!
     * </p>
     * <p>
     * The file handle will be closed automatically.
     * </p>
     *
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param alias
     *            The designated certificate alias.
     * @param certificate
     *            The {@link InputStream} pointing to a {@link Certificate}.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addCertificateFromStream(String certificateType, String alias,
                                             InputStream certificate)
        throws CertificateException, KeyStoreException {

        Certificate c = CertificateHelper.create(certificateType, certificate);

        mKeyStore.setCertificateEntry(alias, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * If NOT NULL, the provided alias will become the certificate alias,
     * else the certificate's hex serial will become the alias!
     * </p>
     * <p>
     * The file handle will be closed automatically.
     * </p>
     *
     * @param alias
     *            The designated certificate alias.
     * @param certificate
     *            The {@link InputStream} pointing to a {@link X509Certificate}.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addX509CertificateFromStream(String alias, InputStream certificate)
        throws CertificateException, KeyStoreException {

        X509Certificate c = CertificateHelper.create(certificate);

        mKeyStore.setCertificateEntry(alias == null ? c.getSerialNumber().toString(16) : alias, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * The certificate's hex serial will become the alias!
     * </p>
     * <p>
     * The file handle will be closed automatically.
     * </p>
     *
     * @param certificate
     *            The {@link String} representation of a {@link Certificate}.
     * @return this object for fluency.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     * @throws KeyStoreException
     *            if the keystore has not been initialized, or the given alias already exists and
     *            does not identify an entry containing a trusted certificate, or this operation
     *            fails for some other reason.
     */
    public KeyStore addX509CertificateFromStream(InputStream certificate)
        throws CertificateException, KeyStoreException {
        return addX509CertificateFromStream(null, certificate);
    }

    /**
     * @return the underlying {@link java.security.KeyStore}.
     */
    public java.security.KeyStore getUnderlyingKeyStore() {
        return mKeyStore;
    }

    /**
     * @return an array of {@link KeyManager}s using our {@link KeyStore} with the
     *            {@link KeyManagerFactory#getDefaultAlgorithm()}.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a KeyManagerFactorySpi implementation for the specified
     *            algorithm.
     * @throws UnrecoverableKeyException
     *            if the key cannot be recovered (e.g. the given password is wrong).
     * @throws KeyStoreException
     *            if this operation fails.
     */
    public KeyManager[] getKeyManagers() throws NoSuchAlgorithmException, UnrecoverableKeyException,
        KeyStoreException {

        KeyManagerFactory kmf = KeyManagerFactory
            .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(mKeyStore, null);

        return kmf.getKeyManagers();
    }

    /**
     * @return an array of {@link TrustManager}s using our {@link KeyStore} with the
     *            {@link TrustManagerFactory#getDefaultAlgorithm()}.
     * @throws NoSuchAlgorithmException
     *            if there is no valid default algorithm for {@link TrustManager}s.
     * @throws KeyStoreException
     *            if a {@link TrustManager} could not be initialized for this {@link KeyStore}.
     */
    protected TrustManager[] getTrustManagers() throws NoSuchAlgorithmException, KeyStoreException {

        TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(mKeyStore);

        return tmf.getTrustManagers();
    }

    /**
     * @return an array which contains exactly one {@link MultiTrustManager} which uses this
     *            keystore <b>and</b> Android's built-in keystore.
     * @throws NoSuchAlgorithmException
     *            if there is no valid default algorithm for {@link TrustManager}s.
     * @throws KeyStoreException
     *            if a {@link TrustManager} could not be initialized for a given {@link KeyStore}.
     */
    protected MultiTrustManager[] getMultiTrustManager()
        throws NoSuchAlgorithmException, KeyStoreException {

        return new MultiTrustManager[] { new MultiTrustManager(this) };
    }
}

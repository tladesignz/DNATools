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
package com.netzarchitekten.tools.ssl;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * <p>
 * A factory class to help create an empty {@link KeyStore} or from a complete PKCS12 keystore file
 * and add certificates from Android's asset folder or from strings to it.
 * </p>
 * <p>
 * Create fresh keystore containing one X.509 certificate from an asset file:
 * </p>
 * <pre>
 *     KeyStore ks = new KeyStoreFactory()
 *         .addX509CertificateFromAssets(context, "foobar.crt")
 *         .getKeyStore();
 * </pre>
 * <p>
 * Create fresh keystore containing one X.509 certificate contained in a string (e.g. from the
 * database):
 * </p>
 * <pre>
 *     KeyStore ks = new KeyStoreFactory()
 *         .addX509CertificateFromString(certificate)
 *         .getKeyStore();
 * </pre>
 * <p>
 * Create fresh keystore containing a certificate of your type contained in a string (e.g. from the
 * database):
 * </p>
 * <pre>
 *     KeyStore ks = new KeyStoreFactory()
 *         .addCertificateFromString(certificateType, alias, certificate)
 *         .getKeyStore();
 * </pre>
 * <p>
 * Create a keystore from a PKCS12 file:
 * </p>
 * <pre>
 *     KeyStore ks = new KeyStoreFactory(pkcs12file, password)
 *         .getKeyStore();
 * </pre>
 * <p>
 * Combine as you wish!
 * </p>
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class KeyStoreFactory {

    private static final String TYPE_PKCS12 = "PKCS12";

    private final KeyStore mKeyStore;

    /**
     * Create a {@link KeyStore} from an {@link InputStream}.
     *
     * @param keyStoreType
     *            A keystore type suitable for {@link KeyStore#getInstance(String)}. May be NULL,
     *            in which case the {@link KeyStore#getDefaultType()} will be used.
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
     *            an UnrecoverableKeyException.
     */
    public KeyStoreFactory(String keyStoreType, InputStream keyStore, char[] password)
        throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        mKeyStore = KeyStore.getInstance(keyStoreType == null
            ? KeyStore.getDefaultType()
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
     *            an UnrecoverableKeyException.
     */
    public KeyStoreFactory(InputStream keyStore, char[] password)
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
     *            an UnrecoverableKeyException.
     */
    public KeyStoreFactory(InputStream keyStore, String password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(keyStore, password.toCharArray());
    }

    /**
     * Create a {@link KeyStore} from an {@link InputStream} pointing to a file in PKCS12 format.
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
     *            an UnrecoverableKeyException.
     */
    public KeyStoreFactory(String keyStore, String password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(new ByteArrayInputStream(keyStore.getBytes("UTF-8")), password);
    }

    /**
     * Create a {@link KeyStore} from an {@link InputStream} pointing to a file in PKCS12 format.
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
     *            an UnrecoverableKeyException.
     */
    public KeyStoreFactory(String keyStore, char[] password)
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(new ByteArrayInputStream(keyStore.getBytes("UTF-8")), password);
    }

    /**
     * Creates an empty {@link KeyStore} using {@link KeyStore#getDefaultType()}.
     *
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
     *            an UnrecoverableKeyException.
     */
    public KeyStoreFactory()
        throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {

        this(null, null, null);
    }

    /**
     * <p>
     * Add a certificate to the {@link KeyStore} from Android's assets folder.
     * </p>
     * <p>
     * Will call {@link #addX509CertificateFromAssets(Context, String)} internally, if you define
     * certificateType as "X.509"!
     * </p>
     * <p>
     * The fileName will become the certificate alias, except, if you use the X.509 type!
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
    public KeyStoreFactory addCertificateFromAssets(Context context, String certificateType,
                                                    String fileName)
        throws CertificateException, IOException, KeyStoreException {

        if (certificateType != null && Certificates.TYPE_X509.equals(certificateType.toUpperCase()))
            return addX509CertificateFromAssets(context, fileName);

        Certificate c = Certificates.create(certificateType, context.getAssets().open(fileName));

        mKeyStore.setCertificateEntry(fileName, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from Android's assets folder.
     * </p>
     * <p>
     * The certificates hex serial will become the alias!
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
    public KeyStoreFactory addX509CertificateFromAssets(Context context, String fileName)
        throws CertificateException, IOException, KeyStoreException {

        X509Certificate c = Certificates.create(context.getAssets().open(fileName));

        mKeyStore.setCertificateEntry(c.getSerialNumber().toString(16), c);

        return this;
    }

    /**
     * <p>
     * Add a certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * Will call {@link #addX509CertificateFromString(String)} internally, if you define
     * certificateType as "X.509"!
     * </p>
     * <p>
     * The provided alias will become the certificate alias, except, if you use the X.509 type!
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
    public KeyStoreFactory addCertificateFromString(String certificateType, String alias,
                                                    String certificate)
        throws CertificateException, KeyStoreException {

        if (certificateType != null && Certificates.TYPE_X509.equals(certificateType.toUpperCase()))
            return addX509CertificateFromString(certificate);

        Certificate c = Certificates.create(certificateType, certificate);

        mKeyStore.setCertificateEntry(alias, c);

        return this;
    }

    /**
     * <p>
     * Add a X.509 certificate to the {@link KeyStore} from a {@link String}.
     * </p>
     * <p>
     * The certificates hex serial will become the alias!
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
    public KeyStoreFactory addX509CertificateFromString(String certificate)
        throws CertificateException, KeyStoreException {

        X509Certificate c = Certificates.create(certificate);

        mKeyStore.setCertificateEntry(c.getSerialNumber().toString(16), c);

        return this;
    }

    /**
     * @return the built {@link KeyStore}.
     */
    public KeyStore getKeyStore() {
        return mKeyStore;
    }
}

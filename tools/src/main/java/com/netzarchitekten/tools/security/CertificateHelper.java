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

import com.netzarchitekten.tools.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Helper class to create a {@link Certificate} or an {@link X509Certificate} from an
 * {@link InputStream} or a {@link String}.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class CertificateHelper {

    /**
     * Well-known type for X.509 certificates.
     */
    public static final String TYPE_X509 = "X.509";

    /**
     * Header for a BASE64 encoded certificate. Is needed for decoding and will be prepended to
     * a certificate provided as {@link String} if not contained in it, already.
     */
    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";

    /**
     * Footer for a BASE64 encoded certificate. Is needed for decoding and will be appended to
     * a certificate provided as {@link String} if not contained in it, already.
     */
    public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

    /**
     * <p>
     * Create a {@link Certificate} from an {@link InputStream}.
     * </p>
     * <p>
     * The file handle will be closed automatically.
     * </p>
     *
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param is
     *            The {@link InputStream} of a {@link Certificate} file.
     * @return a {@link Certificate}.
     * @throws CertificateException
     *            if no provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate file parsing errors.
     */
    public static Certificate create(String certificateType, InputStream is)
        throws CertificateException {

        CertificateFactory cf = CertificateFactory.getInstance(certificateType);

        Certificate c = cf.generateCertificate(is);
        FileUtils.close(is);

        return c;
    }

    /**
     * <p>
     * Create a {@link Certificate} from a {@link String} containing a BASE64 encoded certificate.
     * </p>
     * <p>
     * if {@link #BEGIN_CERTIFICATE} and/or {@link #END_CERTIFICATE} header/footer is not contained
     * in the provided string, they will be prepended/appended automatically!
     * </p>
     *
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param certificate
     *            The {@link String} representation of a {@link Certificate}.
     * @return a {@link Certificate}.
     * @throws CertificateException
     *            if no provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate string parsing errors.
     */
    public static Certificate create(String certificateType, String certificate)
        throws CertificateException {

        byte[] bytes = new byte[0];

        if (!certificate.toUpperCase().contains(BEGIN_CERTIFICATE)) {
            certificate = BEGIN_CERTIFICATE + "\n" + certificate;
        }

        if (!certificate.toUpperCase().contains(END_CERTIFICATE)) {
            certificate += "\n" + END_CERTIFICATE;
        }

        try {
            bytes = certificate.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // We can safely ignore this, since Android supports UTF-8 since API 1.
        }

        return create(certificateType, new ByteArrayInputStream(bytes));
    }

    /**
     * <p>
     * Create a {@link X509Certificate} from an {@link InputStream}.
     * </p>
     * <p>
     * The file handle will be closed automatically.
     * </p>
     *
     * @param is
     *            The {@link InputStream} of a {@link X509Certificate} file.
     * @return a {@link X509Certificate}.
     * @throws CertificateException
     *            if no provider supports a CertificateFactorySpi implementation for the X.509
     *            type OR on certificate file parsing errors.
     */
    public static X509Certificate create(InputStream is) throws CertificateException {
        return (X509Certificate) create(TYPE_X509, is);
    }

    /**
     * <p>
     * Create a {@link X509Certificate} from a {@link String} containing a BASE64 encoded
     * certificate.
     * </p>
     * <p>
     * if {@link #BEGIN_CERTIFICATE} and/or {@link #END_CERTIFICATE} header/footer is not contained
     * in the provided string, they will be prepended/appended automatically!
     * </p>
     *
     * @param certificate
     *            The {@link String} representation of a {@link X509Certificate}.
     * @return a {@link X509Certificate}.
     * @throws CertificateException
     *            if no provider supports a CertificateFactorySpi implementation for the X.509
     *            type OR on certificate string parsing errors.
     */
    public static X509Certificate create(String certificate) throws CertificateException {
        return (X509Certificate) create(TYPE_X509, certificate);
    }
}

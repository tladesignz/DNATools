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

import com.netzarchitekten.tools.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Helper class to create a {@link Certificate} or an {@link X509Certificate}from an
 * {@link InputStream} or a {@link String}.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Certificates {

    /**
     * Well-known type for X.509 certificates.
     */
    public static final String TYPE_X509 = "X.509";

    /**
     * Create a {@link Certificate} from an {@link InputStream}.
     *
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param is
     *            The {@link InputStream} of a {@link Certificate} file.
     * @return a {@link Certificate}.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
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
     * Create a {@link Certificate} from a {@link String}.
     *
     * @param certificateType
     *            A certificate type suitable for {@link CertificateFactory#getInstance(String)}.
     * @param certificate
     *            The {@link String} representation of a {@link Certificate}.
     * @return a {@link Certificate}.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the specified
     *            type OR on certificate string parsing errors.
     */
    public static Certificate create(String certificateType, String certificate)
        throws CertificateException {

        byte[] bytes = new byte[0];

        try {
            bytes = certificate.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // We can safely ignore this, since Android supports UTF-8 since API 1.
        }

        return create(certificateType, new ByteArrayInputStream(bytes));
    }

    /**
     * Create a {@link X509Certificate} from an {@link InputStream}.
     *
     * @param is
     *            The {@link InputStream} of a {@link X509Certificate} file.
     * @return a {@link X509Certificate}.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the X.509
     *            type OR on certificate file parsing errors.
     */
    public static X509Certificate create(InputStream is) throws CertificateException {
        return (X509Certificate) create(TYPE_X509, is);
    }

    /**
     * Create a {@link X509Certificate} from a {@link String}.
     *
     * @param certificate
     *            The {@link String} representation of a {@link X509Certificate}.
     * @return a {@link X509Certificate}.
     * @throws CertificateException
     *            if no Provider supports a CertificateFactorySpi implementation for the X.509
     *            type OR on certificate string parsing errors.
     */
    public static X509Certificate create(String certificate) throws CertificateException {
        return (X509Certificate) create(TYPE_X509, certificate);
    }
}

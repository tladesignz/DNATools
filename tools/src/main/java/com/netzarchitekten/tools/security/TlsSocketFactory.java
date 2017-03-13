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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * <p>
 * Socket factory which can be used to enable TLS 1.1 and 1.2 on Android API 16 - 19,
 * which normally don't use them by default or any other combination of device supported protocols.
 * </p>
 * <p>
 * This class filters the protocols provided by you against the device's capability list.
 * So you won't get exceptions about broken or unsupported protocol names but you can end up without
 * any supported protocol, which will also break your connection. So ensure, that you don't execute
 * on too old devices without protocol support!
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 * @see <a href="https://blog.dev-area.net/2015/08/13/android-4-1-enable-tls-1-1-and-tls-1-2/"
 * >blog.dev-area.net</a>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class TlsSocketFactory extends SSLSocketFactory {

    /**
     * SSL 3.0 identifier.
     *
     * @deprecated Don't use this in production any more, this protocol is highly insecure!
     */
    @Deprecated
    public static final String SSL_3 = "SSLv3";

    /**
     * TLS 1.0 identifier.
     *
     * @deprecated Better not to use this anymore, too, if it's avoidable.
     */
    @Deprecated
    public static final String TLS_10 = "TLSv1";

    /**
     * TLS 1.1 identifier.
     */
    public static final String TLS_11 = "TLSv1.1";

    /**
     * TLS 1.2 identifier.
     */
    public static final String TLS_12 = "TLSv1.2";

    /**
     * List of TLS 1.0, 1.1 and 1.2. (better compatibility)
     */
    @SuppressWarnings("deprecation")
    public static final String[] TLS_10_11_12 = { TLS_10, TLS_11, TLS_12 };

    /**
     * List of TLS 1.1 and 1.2. (better security)
     */
    public static final String[] TLS_11_12 = { TLS_11, TLS_12 };

    private final List<String> mProtocols;

    private String[] mSupportedFiltered;

    private final SSLSocketFactory mSocketFactory;

    /**
     * Initializes a {@link SSLSocketFactory}, and keeps it wrapped inside this object.
     *
     * @param protocols
     *            The list of protocols to enforce. Can be null. (See public constants for reference.)
     * @param keyManagers
     *            The sources of authentication keys or null.
     * @param trustManagers
     *            The sources of peer authentication trust decisions or null.
     * @param secureRandom
     *            The source of randomness for this generator or null.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the TLS
     *            protocol.
     * @throws KeyManagementException
     *            if the {@link SSLContext} initialization fails.
     */
    public TlsSocketFactory(String[] protocols, KeyManager[] keyManagers, TrustManager[] trustManagers,
                            SecureRandom secureRandom)
        throws NoSuchAlgorithmException, KeyManagementException {

        mProtocols = protocols == null ? null : Arrays.asList(protocols);

        // Create the SSL context.
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(keyManagers, trustManagers, secureRandom);

        mSocketFactory = sslcontext.getSocketFactory();
    }

    /**
     * Initializes a {@link SSLSocketFactory} with default {@link SecureRandom}, and keeps it
     * wrapped inside this object.
     *
     * @param protocols
     *            The list of supported protocols. (See public constants for reference.)
     * @param keyManagers
     *            The sources of authentication keys or null.
     * @param trustManagers
     *            The sources of peer authentication trust decisions or null.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the TLS
     *            protocol.
     * @throws KeyManagementException
     *            if the {@link SSLContext} initialization fails.
     */
    public TlsSocketFactory(String[] protocols, KeyManager[] keyManagers, TrustManager[] trustManagers)
        throws KeyManagementException, NoSuchAlgorithmException {
        this(protocols, keyManagers, trustManagers, null);
    }

    /**
     * Initializes a {@link SSLSocketFactory} with device default protocols and default
     * {@link SecureRandom} and keeps it wrapped inside this object.
     *
     * @param keyManagers
     *            The sources of authentication keys or null.
     * @param trustManagers
     *            The sources of peer authentication trust decisions or null.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the TLS
     *            protocol.
     * @throws KeyManagementException
     *            if the {@link SSLContext} initialization fails.
     */
    public TlsSocketFactory(KeyManager[] keyManagers, TrustManager[] trustManagers)
        throws KeyManagementException, NoSuchAlgorithmException {
        this(null, keyManagers, trustManagers, null);
    }

    /**
     * Initializes a {@link SSLSocketFactory} with default {@link SecureRandom} and without a client
     * certificate, and keeps it wrapped inside this object.
     *
     * @param protocols
     *            The list of supported protocols. (See public constants for reference.)
     * @param trustManagers
     *            The sources of peer authentication trust decisions or null.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the TLS
     *            protocol.
     * @throws KeyManagementException
     *            if the {@link SSLContext} initialization fails.
     */
    public TlsSocketFactory(String[] protocols, TrustManager[] trustManagers)
        throws KeyManagementException, NoSuchAlgorithmException {
        this(protocols, null, trustManagers);
    }

    /**
     * Initializes a {@link SSLSocketFactory} with device default protocols, default
     * {@link SecureRandom} and without a client certificate, and keeps it wrapped
     * inside this object.
     *
     * @param trustManagers
     *            The sources of peer authentication trust decisions or null.
     * @throws NoSuchAlgorithmException
     *            if no Provider supports a TrustManagerFactorySpi implementation for the TLS
     *            protocol.
     * @throws KeyManagementException
     *            if the {@link SSLContext} initialization fails.
     */
    public TlsSocketFactory(TrustManager[] trustManagers)
        throws KeyManagementException, NoSuchAlgorithmException {
        this((KeyManager[]) null, trustManagers);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return mSocketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return mSocketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
        throws IOException {

        return forceProtocols(mSocketFactory.createSocket(socket, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return forceProtocols(mSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
        throws IOException {

        return forceProtocols(mSocketFactory.createSocket(host, port, localHost, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return forceProtocols(mSocketFactory.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
                               int localPort) throws IOException {

        return forceProtocols(mSocketFactory.createSocket(address, port, localAddress, localPort));
    }

    /**
     * <p>
     * Forces support of the given protocol list, as far as the device supports it.
     *
     * @param socket
     *            A {@link Socket}.
     * @return the same {@link Socket} with enforced protocol list, if an {@link SSLSocket} and
     * {@link #mProtocols} isn't null.
     */
    private Socket forceProtocols(Socket socket) {
        if(mProtocols != null && socket != null && (socket instanceof SSLSocket)) {
            SSLSocket sslSocket = (SSLSocket) socket;

            // We do this only once, it's expensive.
            if (mSupportedFiltered == null) {
                List<String> supported = new ArrayList<>(Arrays.asList(sslSocket.getSupportedProtocols()));
                supported.retainAll(mProtocols);

                mSupportedFiltered = supported.toArray(new String[supported.size()]);
            }

            sslSocket.setEnabledProtocols(mSupportedFiltered);
        }

        return socket;
    }
}

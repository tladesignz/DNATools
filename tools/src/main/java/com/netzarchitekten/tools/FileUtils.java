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
package com.netzarchitekten.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * <p>
 * Save a lot of boilerplate code when handling file copying.
 * </p>
 * <p>
 * Use like this:
 * </p>
 *
 * <pre>
 * InputStream is = null;
 * OutputStream os = null;
 *
 * try {
 *      is = ...
 *      os = ...
 *
 *      FileUtils.copy(is, os);
 * } catch (IOException e) {
 *      throw e;
 * } finally {
 *      FileUtils.close(is, os);
 * }
 * </pre>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FileUtils {

    /**
     * Copy content from on file to another using a 1024 byte cache.
     *
     * @param is
     *            The {@link InputStream} to read from.
     * @param os
     *            The {@link OutputStream} to write to.
     * @throws IOException
     *             if an error occurs while reading from the {@link InputStream}
     *             or writing to the {@link OutputStream}.
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = is.read(buffer)) > -1) {
            os.write(buffer, 0, len);
        }
    }

    /**
     * Tries to close given resources. Handles null and {@link IOException} on
     * close gracefully.
     *
     * @param closeables
     *            Objects which can be closed.
     */
    public static void close(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable c : closeables) {
                try {
                    if (c != null) c.close();
                } catch (IOException e) {
                    // Fine, then we don't close it - at least, we tried...
                }
            }
        }
    }

    /**
     * <p>
     * Returns the specified {@link SharedPreferences} {@link File} using the internal
     * <code>Context#getSharedPrefsFile</code> via reflection.
     * </p>
     * <p>
     * If that fails, tries a semi-hardcoded fallback.
     * </p>
     * <p>
     * Actual existence is not guaranteed!
     * </p>
     *
     * @param context
     *            Your current {@link Context}.
     * @param name
     *            The name of the {@link SharedPreferences} file.
     * @return a {@link File} object pointing to the actual {@link SharedPreferences} file.
     */
    public static File getSharedPrefsFile(Context context, String name) {
        File file;

        try {
            Method method = context.getClass().getMethod("getSharedPrefsFile", String.class);
            file = (File) method.invoke(context, name);
        } catch (Exception e) {
            file = new File(context.getFilesDir(), String.format("../shared_prefs/%s.xml", name));
        }

        return file;
    }

    /**
     * <p>
     * Returns the specified default {@link SharedPreferences} {@link File} using the internal
     * <code>Context#getSharedPrefsFile</code> via reflection.
     * </p>
     * <p>
     * If that fails, tries a semi-hardcoded fallback.
     * </p>
     * <p>
     * Actual existence is not guaranteed!
     * </p>
     *
     * @param context
     *            Your current {@link Context}.
     * @return a {@link File} object pointing to the actual {@link SharedPreferences} file.
     */
    public static File getDefaultSharedPrefsFile(Context context) {
        return getSharedPrefsFile(context, String.format("%s_preferences", context.getPackageName()));
    }
}

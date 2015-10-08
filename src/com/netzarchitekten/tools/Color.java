/**
 * DNA Android Tools.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Die Netzarchitekten e.U., Benjamin Erhart
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
package com.netzarchitekten.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

/**
 * <p>
 * Encapsulates deprecation warning for {@link Resources#getColor(int)} since
 * Android Marshmallow (API 23).
 * </p>
 * <p>
 * Contains a static and an OO interface.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public class Color {

    private final Resources mResources;

    private static Color sInstance;

    /**
     * Singleton instance.
     *
     * @param context
     *            A context object to access the {@link Resources} of the app.
     * @return a singleton instance
     */
    public static Color getInstance(Context context) {
        if (sInstance == null) sInstance = new Color(context);

        return sInstance;
    }

    /**
     * @param context
     *            A context object to access the {@link Resources} of the app.
     */
    public Color(Context context) {
        mResources = context.getResources();
    }

    /**
     * Honor deprecation of {@link Resources#getColor(int)} since API 23.
     *
     * @param id
     *            The desired resource identifier, as generated by the aapt
     *            tool. This integer encodes the package, type, and resource
     *            entry. The value 0 is an invalid identifier.
     * @return a single color value in the form 0xAARRGGBB.
     * @see Resources#getColor(int, android.content.res.Resources.Theme)
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public int get(int id) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            return mResources.getColor(id, null);

        return mResources.getColor(id);
    }

    /**
     * @param context
     *            A context object to access the {@link Resources} of the app.
     * @param id
     *            The desired resource identifier, as generated by the aapt
     *            tool. This integer encodes the package, type, and resource
     *            entry. The value 0 is an invalid identifier.
     * @return a single color value in the form 0xAARRGGBB.
     */
    public static int get(Context context, int id) {
        return getInstance(context).get(id);
    }
}

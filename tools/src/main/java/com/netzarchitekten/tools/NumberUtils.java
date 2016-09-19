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

import android.text.TextUtils;

/**
 * Utility methods for numbers.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public class NumberUtils {


    /**
     * Try to parse an integer out of a string or return the default value if
     * this fails.
     *
     * @param value
     *            The integer contained in a {@link String}.
     * @param defaultValue
     *            The default value to return if value is empty or doesn't
     *            contain a valid integer.
     * @return the parsed integer value or the defaultValue.
     */
    public static Integer parseInt(String value, Integer defaultValue) {
        Integer v = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            try {
                v = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Do nothing, keep default.
            }
        }

        return v;
    }

    /**
     * Try to parse a double out of a string or return the default value if this
     * fails.
     *
     * @param value
     *            The double contained in a {@link String}.
     * @param defaultValue
     *            The default value to return if value is empty or doesn't
     *            contain a valid double.
     * @return the parsed double value or the defaultValue.
     */
    public static Double parseDouble(String value, Double defaultValue) {
        Double v = defaultValue;

        if (!TextUtils.isEmpty(value)) {
            try {
                v = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                // Do nothing, keep default.
            }
        }

        return v;
    }
}

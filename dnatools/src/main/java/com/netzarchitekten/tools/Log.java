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

import java.util.Locale;

import android.text.TextUtils;

/**
 * <p>
 * Logging helper which wraps {@link android.util.Log}.
 * </p>
 * <ul>
 * <li>Allows consuming apps to control, which log levels are actually printed.
 * </li>
 * <li>Doesn't print anything in {@link #VERBOSE} and {@link #DEBUG} if
 * {@code BuildConfig#DEBUG} is not true, meaning app is not in development
 * mode.</li>
 * <li>Log tag is automatically generated like this
 * "com.example.CallingClass#callingMethod#123".</li>
 * <li>Log message is wrapped in
 * {@link String#format(Locale, String, Object...)}, where {@link Locale} is
 * {@link Locale#US} per default, but can be changed.
 * </ul>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public class Log {

    /**
     * Proxy for {@link android.util.Log#VERBOSE}.
     */
    public static final int VERBOSE = android.util.Log.VERBOSE;

    /**
     * Proxy for {@link android.util.Log#DEBUG}.
     */
    public static final int DEBUG = android.util.Log.DEBUG;

    /**
     * Proxy for {@link android.util.Log#INFO}.
     */
    public static final int INFO = android.util.Log.INFO;

    /**
     * Proxy for {@link android.util.Log#WARN}.
     */
    public static final int WARN = android.util.Log.WARN;

    /**
     * Proxy for {@link android.util.Log#ERROR}.
     */
    public static final int ERROR = android.util.Log.ERROR;

    /**
     * Proxy for {@link android.util.Log#ASSERT}.
     */
    public static final int ASSERT = android.util.Log.ASSERT;

    /**
     * Current log level.
     */
    private static int sLogLevel = VERBOSE;

    /**
     * Current {@link Locale} to use in
     * {@link String#format(Locale, String, Object...)}.
     */
    private static Locale sLocale = Locale.US;

    /**
     * Set current log level. Every subsequent call with a lower log level will
     * be ignored.
     *
     * @param level
     *            the new log level
     */
    public static void setLogLevel(int level) {
        sLogLevel = level;
    }

    /**
     * <p>
     * Returns the <em>real</em> log level: If we're not in debug mode, the log
     * level cannot be lower than {@link #INFO}.
     * </p>
     * <p>
     * All calls with a lower log level will be ignored.
     * </p>
     *
     * @return the real current log level.
     */
    @SuppressWarnings("unused")
    public static int getLogLevel() {
        if (!BuildConfig.DEBUG && sLogLevel < INFO) return INFO;

        return sLogLevel;
    }

    /**
     * <p>
     * Set the {@link Locale}, which is used in
     * {@link String#format(Locale, String, Object...)} calls to a given
     * {@link Locale}.
     * </p>
     * <p>
     * Default is {@link Locale#US}.
     * </p>
     * <p>
     * You most probably should log in english, so as many IT people as possible
     * can read this. But if you really really want to output your log messages
     * in your native language or something - here's your chance to format the
     * strings accordingly...
     * </p>
     *
     * @param locale
     *            the new {@link Locale}
     */
    public static void setLocale(Locale locale) {
        sLocale = locale;
    }

    /**
     * @return the current {@link Locale}, which is used in
     *         {@link String#format(Locale, String, Object...)} calls. Defaults
     *         to {@link Locale#US}.
     */
    public static Locale getLocale() {
        return sLocale;
    }

    /**
     * Log a message with a given level. Use caller as log tag, format message
     * with {@link String#format(Locale, String, Object...)}.
     *
     * @param level
     *            the log level
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#println(int, String, String)
     */
    public static int println(int level, String msg, Object... args) {
        if ((level > DEBUG || BuildConfig.DEBUG) && sLogLevel <= level)
            return android.util.Log.println(level, getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log a message with level {@link #VERBOSE}, but only if we're running in
     * development. Use caller as log tag, format message with
     * {@link String#format(Locale, String, Object...)}.
     *
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#v(String, String)
     */
    public static int v(String msg, Object... args) {
        if (BuildConfig.DEBUG && sLogLevel <= VERBOSE)
            return android.util.Log.v(getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log an exception with level {@link #VERBOSE}, but only if we're running
     * in development.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     * @see android.util.Log#v(String, String, Throwable)
     */
    public static int v(Throwable tr) {
        if (BuildConfig.DEBUG && sLogLevel <= VERBOSE)
            return android.util.Log.v(getTag(), getMsg(tr), tr);

        return 0;
    }

    /**
     * Log a message with level {@link #DEBUG}, but only if we're running in
     * development. Use caller as log tag, format message with
     * {@link String#format(Locale, String, Object...)}.
     *
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#d(String, String)
     */
    public static int d(String msg, Object... args) {
        if (BuildConfig.DEBUG && sLogLevel <= DEBUG)
            return android.util.Log.d(getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log an exception with level {@link #DEBUG}, but only if we're running in
     * development.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     * @see android.util.Log#d(String, String, Throwable)
     */
    public static int d(Throwable tr) {
        if (BuildConfig.DEBUG && sLogLevel <= DEBUG)
            return android.util.Log.d(getTag(), getMsg(tr), tr);

        return 0;
    }

    /**
     * Log a message with level {@link #INFO}. Use caller as log tag, format
     * message with {@link String#format(Locale, String, Object...)}.
     *
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#i(String, String)
     */
    public static int i(String msg, Object... args) {
        if (sLogLevel <= INFO)
            return android.util.Log.i(getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log an exception with level {@link #INFO}.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     * @see android.util.Log#i(String, String, Throwable)
     */
    public static int i(Throwable tr) {
        if (sLogLevel <= INFO) return android.util.Log.i(getTag(), getMsg(tr), tr);

        return 0;
    }

    /**
     * Log a message with level {@link #WARN}. Use caller as log tag, format
     * message with {@link String#format(Locale, String, Object...)}.
     *
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#w(String, String)
     */
    public static int w(String msg, Object... args) {
        if (sLogLevel <= WARN)
            return android.util.Log.w(getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log an exception with level {@link #WARN}.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     * @see android.util.Log#w(String, String, Throwable)
     */
    public static int w(Throwable tr) {
        if (sLogLevel <= WARN) return android.util.Log.w(getTag(), getMsg(tr), tr);

        return 0;
    }

    /**
     * Log a message with level {@link #ERROR}. Use caller as log tag, format
     * message with {@link String#format(Locale, String, Object...)}.
     *
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#e(String, String)
     */
    public static int e(String msg, Object... args) {
        if (sLogLevel <= ERROR)
            return android.util.Log.e(getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log an exception with level {@link #ERROR}.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     * @see android.util.Log#e(String, String, Throwable)
     */
    public static int e(Throwable tr) {
        if (sLogLevel <= ERROR) return android.util.Log.e(getTag(), getMsg(tr), tr);

        return 0;
    }

    /**
     * Log a message with level {@link #ASSERT}. Use caller as log tag, format
     * message with {@link String#format(Locale, String, Object...)}.
     *
     * @param msg
     *            first argument of {@link String#format(String, Object...)}
     * @param args
     *            additional arguments for
     *            {@link String#format(String, Object...)}
     * @return the number of bytes written.
     * @see android.util.Log#wtf(String, String)
     */
    public static int wtf(String msg, Object... args) {
        if (sLogLevel <= ASSERT)
            return android.util.Log.wtf(getTag(), String.format(sLocale, msg, args));

        return 0;
    }

    /**
     * Log an exception with level {@link #ASSERT}.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     * @see android.util.Log#wtf(String, Throwable)
     */
    public static int wtf(Throwable tr) {
        if (sLogLevel <= ASSERT) return android.util.Log.wtf(getTag(), getMsg(tr), tr);

        return 0;
    }

    /**
     * Proxy to {@link android.util.Log#getStackTraceString(Throwable)}.
     *
     * @param tr
     *            an exception to log
     * @return the number of bytes written.
     */
    public static String getStackTrace(Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    /**
     * Proxy to {@link android.util.Log#isLoggable(String, int)}. Use caller as
     * log tag.
     *
     * @param level
     *            the level to check
     * @return whether or not this is allowed to be logged.
     */
    public static boolean isLoggable(int level) {
        String tag = getTag();

        // Avoid IllegalArgumentException in #isLoggable.
        if (tag != null && tag.length() > 23) tag = tag.substring(0, 23);

        return android.util.Log.isLoggable(tag, level);
    }

    /**
     * @return a String like "com.example.CallingClass#callingMethod#123".
     */
    private static String getTag() {
        StackTraceElement trace[] = Thread.currentThread().getStackTrace();

        return trace[4].getClassName() + "#" + trace[4].getMethodName() + "#"
            + trace[4].getLineNumber();
    }

    /**
     * @param tr
     *            an exception to log
     * @return a proper error message from a {@link Throwable}.
     */
    private static String getMsg(Throwable tr) {
        return tr.getClass().getSimpleName()
            + (TextUtils.isEmpty(tr.getMessage()) ? "" : ": \"" + tr.getMessage() + "\"");
    }
}

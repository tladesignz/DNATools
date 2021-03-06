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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Encapsulates deprecation warning for
 * <ul>
 *     <li>{@link android.content.res.Resources#getColor(int)} since Marshmallow (API 23)</li>
 *     <li>{@link android.content.res.Resources#getDrawable(int)} since Lollipop MR1 (API 22)</li>
 *     <li>{@link android.content.res.Configuration#locale} since N (API 24)</li>
 * </ul>
 * <p>
 * Contains a static and an OO interface.
 * </p>
 * <p>
 * Also, provides facilities to work with a different locale, then set by the user.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Resources {

    private final Context mContext;

    private final android.content.res.Resources mResources;

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     */
    public Resources(Context context) {
        mContext = context;
        mResources = mContext.getResources();
    }

    /**
     * Honor deprecation of {@link android.content.res.Resources#getColor(int)}
     * since API 23.
     *
     * @param id
     *            The desired resource identifier, as generated by the aapt
     *            tool. This integer encodes the package, type, and resource
     *            entry. The value 0 is an invalid identifier.
     * @return a single color value in the form 0xAARRGGBB.
     * @see android.content.res.Resources#getColor(int, android.content.res.Resources.Theme)
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return mResources.getColor(id, null);

        return mResources.getColor(id);
    }

    /**
     * Honor deprecation of
     * {@link android.content.res.Resources#getDrawable(int)} since API 22.
     *
     * @param id
     *            The desired resource identifier, as generated by the aapt
     *            tool. This integer encodes the package, type, and resource
     *            entry. The value 0 is an invalid identifier.
     * @return an object that can be used to draw this resource.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public Drawable getDrawable(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return mResources.getDrawable(id, null);

        return mResources.getDrawable(id);
    }

    /**
     * Honor deprecation of {@link Configuration#locale} since API 24.
     *
     * @return the currently used primary locale.
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public Locale getPrimaryLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return mResources.getConfiguration().getLocales().get(0);

        return mResources.getConfiguration().locale;
    }

    /**
     * <p>
     * Sets a new (and only) locale for this app until it is reset using {@link #resetLocale()},
     * <b>as long</b>, as the primary locale isn't already the same.
     * </p>
     * <p>
     * Makes use of a side-effect of
     * {@link android.content.res.Resources#Resources(AssetManager, DisplayMetrics, Configuration)},
     * which propagates the localization change.
     * </p>
     *
     * @param newLocale
     *            The new {@link Locale}.
     * @return this object for fluency.
     * @deprecated Using this side-effect is deprecated. Use {@link #getContextWithNewLocale(Locale)}
     *      instead, which dynamically uses a better method, if available.
     */
    @SuppressWarnings({"deprecation", "DeprecatedIsStillUsed"})
    @SuppressLint("NewApi")
    @Deprecated
    public Resources setLocale(Locale newLocale) {
        if (!getPrimaryLocale().equals(newLocale)) {
            Configuration newConfig = new Configuration(mResources.getConfiguration());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                newConfig.setLocales(new LocaleList(newLocale));
            } else {
                newConfig.locale = newLocale;
            }

            new android.content.res.Resources(mResources.getAssets(),
                mResources.getDisplayMetrics(),
                newConfig);
        }

        return this;
    }

    /**
     * <p>
     * Sets a new (and only) locale for this app until it is reset using {@link #resetLocale()}.
     * </p>
     * <p>
     * Makes use of a side-effect of
     * {@link android.content.res.Resources#Resources(AssetManager, DisplayMetrics, Configuration)},
     * which propagates the localization change.
     * </p>
     *
     * @param newLocale
     *            The new locale as {@link String}.
     * @return this object for fluency.
     * @deprecated Using this side-effect is deprecated. Use {@link #getContextWithNewLocale(String)}
     *      instead, which dynamically uses a better method, if available.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Resources setLocale(String newLocale) {
        return setLocale(new Locale(newLocale));
    }

    /**
     * <p>
     * Creates a new {@link Context} which uses a given {@link Locale} instead of the default one
     * used.
     * </p>
     * <p>
     * As a compatibility fallback for API &lt; 17, where this is not possible, instead injects the
     * given Locale into the <b>current</b> context and returns that.
     * </p>
     * <p>
     * <b>ATTENTION</b>: Because of the fallback, make sure to call
     * {@link #giveUpNewLocaleContext()} at the  end of your usage and beware, that if you
     * don't or hold on too long to this, it can happen on API &lt; 17, that your complete app will
     * show in a different language!
     * </p>
     *
     * @param newLocale
     *            The new {@link Locale}.
     * @return a {@link Context} using the given {@link Locale}.
     */
    @SuppressWarnings("deprecation")
    public Context getContextWithNewLocale(Locale newLocale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration override = new Configuration();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                override.setLocales(new LocaleList(newLocale));
            } else {
                override.locale = newLocale;
            }

            return mContext.createConfigurationContext(override);
        }

        // Fallback for older versions, using the side-effect.

        setLocale(newLocale);

        return mContext;
    }

    /**
     * <p>
     * Creates a new {@link Context} which uses a given locale instead of the default one
     * used.
     * </p>
     * <p>
     * As a compatibility fallback for API &lt; 17, where this is not possible, instead injects the
     * given Locale into the <b>current</b> context and returns that.
     * </p>
     * <p>
     * <b>ATTENTION</b>: Because of the fallback, make sure to call
     * {@link #giveUpNewLocaleContext()} at the  end of your usage and beware, that if you
     * don't or hold on too long to this, it can happen on API &lt; 17, that your complete app will
     * show in a different language!
     * </p>
     *
     * @param newLocale
     *            The new locale as a {@link String}.
     * @return a {@link Context} using the given locale.
     */
    public Context getContextWithNewLocale(String newLocale) {
        return getContextWithNewLocale(new Locale(newLocale));
    }

    /**
     * Reset the current primary locale to the originally set device's locale using a
     * side-effect of
     * {@link android.content.res.Resources#Resources(AssetManager, DisplayMetrics, Configuration)}.
     *
     * @return this object for fluency.
     * @deprecated Using this side-effect is deprecated. Use {@link #giveUpNewLocaleContext()}
     *      instead, which dynamically uses a better method, if available.
     */
    @SuppressWarnings({"deprecation", "DeprecatedIsStillUsed"})
    @Deprecated
    public Resources resetLocale() {
        new android.content.res.Resources(mResources.getAssets(),
            mResources.getDisplayMetrics(),
            mResources.getConfiguration());

        return this;
    }

    /**
     * <p>
     * Give up the context with the new locale.
     * </p>
     * <p>
     * Actually, this does nothing on API &gt;= 17, since we just have to stop using the given
     * {@link Context}. Below, though, it resets the language on the main context to the original
     * one, since that is the workaround for older API versions.
     * </p>
     *
     * @return the original {@link Context}, which you might assign to your local context variable.
     */
    @SuppressWarnings("deprecation")
    public Context giveUpNewLocaleContext() {
        // Only, if we had to use the locale-injection into the normal context, this is useful.
        // Otherwise, we just do nothing.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            resetLocale();
        }

        return mContext;
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @param id
     *            The desired resource identifier, as generated by the aapt
     *            tool. This integer encodes the package, type, and resource
     *            entry. The value 0 is an invalid identifier.
     * @return a single color value in the form 0xAARRGGBB.
     */
    public static int getColor(Context context, int id) {
        return new Resources(context).getColor(id);
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @param id
     *            The desired resource identifier, as generated by the aapt
     *            tool. This integer encodes the package, type, and resource
     *            entry. The value 0 is an invalid identifier.
     * @return an object that can be used to draw this resource.
     */
    public static Drawable getDrawable(Context context, int id) {
        return new Resources(context).getDrawable(id);
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @return the currently used primary locale.
     */
    public static Locale getPrimaryLocale(Context context) {
        return new Resources(context).getPrimaryLocale();
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @param newLocale
     *            The new {@link Locale}.
     * @return this object for fluency.
     * @deprecated Using this side-effect is deprecated. Use
     *      {@link #getContextWithNewLocale(Context, Locale)} instead, which dynamically uses a
     *      better method, if available.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static Resources setLocale(Context context, Locale newLocale) {
        return new Resources(context).setLocale(newLocale);
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @param newLocale
     *            The new locale as {@link String}.
     * @return this object for fluency.
     * @deprecated Using this side-effect is deprecated. Use
     *      {@link #getContextWithNewLocale(Context, String)} instead, which dynamically uses a
     *      better method, if available.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static Resources setLocale(Context context, String newLocale) {
        return new Resources(context).setLocale(newLocale);
    }

    /**
     * <p>
     * Creates a new {@link Context} which uses a given {@link Locale} instead of the default one
     * used.
     * </p>
     * <p>
     * As a compatibility fallback for API &lt; 17, where this is not possible, instead injects the
     * given Locale into the <b>current</b> context and returns that.
     * </p>
     * <p>
     * <b>ATTENTION</b>: Because of the fallback, make sure to call
     * {@link #giveUpNewLocaleContext(Context)} at the  end of your usage and beware, that
     * if you  don't or hold on too long to this, it can happen on API &lt; 17, that your complete
     * app will show in a different language!
     * </p>
     *
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @param newLocale
     *            The new {@link Locale}.
     * @return a {@link Context} using the given {@link Locale}.
     */
    public static Context getContextWithNewLocale(Context context, Locale newLocale) {
        return new Resources(context).getContextWithNewLocale(newLocale);
    }

    /**
     * <p>
     * Creates a new {@link Context} which uses a given locale instead of the default one
     * used.
     * </p>
     * <p>
     * As a compatibility fallback for API &lt; 17, where this is not possible, instead injects the
     * given Locale into the <b>current</b> context and returns that.
     * </p>
     * <p>
     * <b>ATTENTION</b>: Because of the fallback, make sure to call
     * {@link #giveUpNewLocaleContext(Context)} at the  end of your usage and beware, that
     * if you  don't or hold on too long to this, it can happen on API &lt; 17, that your complete
     * app will show in a different language!
     * </p>
     *
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @param newLocale
     *            The new locale as a {@link String}.
     * @return a {@link Context} using the given locale.
     */
    public static Context getContextWithNewLocale(Context context, String newLocale) {
        return new Resources(context).getContextWithNewLocale(newLocale);
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     * @return this object for fluency.
     * @deprecated Using this side-effect is deprecated. Use
     *      {@link #giveUpNewLocaleContext(Context)} instead, which dynamically uses a
     *      better method, if available.
     */
    @SuppressWarnings("deprecation")
    public static Resources resetLocale(Context context) {
        return new Resources(context).resetLocale();
    }

    /**
     * @param context
     *            A context object to access the
     *            {@link android.content.res.Resources} of the app.
     *
     * @return NULL, which you must assign to your local context variable.
     */
    public static Context giveUpNewLocaleContext(Context context) {
        return new Resources(context).giveUpNewLocaleContext();
    }
}

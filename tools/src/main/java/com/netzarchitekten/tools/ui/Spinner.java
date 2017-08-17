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
package com.netzarchitekten.tools.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;

/**
 * <p>
 * A {@link android.widget.Spinner} that has a {@link #setSelection(int, boolean, boolean)} method
 * which can suppress calls to a connected {@link OnItemSelectedListener}.
 * </p>
 * <p>
 * In order to use this, you have to declare Spinners as
 * "com.netzarchitekten.tools.ui.Spinner" in the layout xml file.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings("unused")
public class Spinner extends android.widget.Spinner {
    public Spinner(Context context) {
        super(context);
    }

    /**
     * Constructs a new spinner with the given context's theme and the supplied
     * mode of displaying choices. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN}.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param mode Constant describing how the user will select choices from
     *             the spinner.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public Spinner(Context context, int mode) {
        super(context, mode);
    }

    /**
     * Constructs a new spinner with the given context's theme and the supplied
     * attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public Spinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, and default style attribute.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default
     *                     values for the view. Can be 0 to not look for
     *                     defaults.
     */
    public Spinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, and default style attribute. <code>mode</code> may be one
     * of {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN} and determines how the
     * user will select choices from the spinner.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default
     *                     values for the view. Can be 0 to not look for defaults.
     * @param mode Constant describing how the user will select choices from the
     *             spinner.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    /**
     * Constructs a new spinner with the given context's theme, the supplied
     * attribute set, and default styles. <code>mode</code> may be one of
     * {@link #MODE_DIALOG} or {@link #MODE_DROPDOWN} and determines how the
     * user will select choices from the spinner.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default
     *                     values for the view. Can be 0 to not look for
     *                     defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *                    supplies default values for the view, used only if
     *                    defStyleAttr is 0 or can not be found in the theme.
     *                    Can be 0 to not look for defaults.
     * @param mode Constant describing how the user will select choices from
     *             the spinner.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    /**
     * Constructs a new spinner with the given context, the supplied attribute
     * set, default styles, popup mode (one of {@link #MODE_DIALOG} or
     * {@link #MODE_DROPDOWN}), and the theme against which the popup should be
     * inflated.
     *
     * @param context The context against which the view is inflated, which
     *                provides access to the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default
     *                     values for the view. Can be 0 to not look for
     *                     defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *                    supplies default values for the view, used only if
     *                    defStyleAttr is 0 or can not be found in the theme.
     *                    Can be 0 to not look for defaults.
     * @param mode Constant describing how the user will select choices from
     *             the spinner.
     * @param popupTheme The theme against which the dialog or dropdown popup
     *                   should be inflated. May be {@code null} to use the
     *                   view theme. If set, this will override any value
     *                   specified by
     *                   {@code android.R.styleable#Spinner_popupTheme}.
     *
     * @see #MODE_DIALOG
     * @see #MODE_DROPDOWN
     */
    @TargetApi(Build.VERSION_CODES.M)
    public Spinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
    }

    /**
     * Jump directly to a specific item in the adapter data.
     *
     * @param position
     *            The item number to jump to.
     * @param animate
     *            True, if jump shall be animated.
     * @param suppressCallback
     *            True, if {@link OnItemSelectedListener} connected via
     *            {@link #setOnItemSelectedListener(OnItemSelectedListener)} should not be called.
     */
    public void setSelection(int position, boolean animate, boolean suppressCallback) {
        OnItemSelectedListener listener = null;

        if (suppressCallback) {
            listener = getOnItemSelectedListener();
            setOnItemSelectedListener(null);
        }

        setSelection(position, animate);

        if (suppressCallback) {
            setOnItemSelectedListener(listener);
        }
    }
}

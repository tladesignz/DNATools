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
package com.netzarchitekten.tools.ui;

import java.util.Arrays;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

/**
 * <p>
 * A {@link android.preference.Preference} that displays a number picker as a
 * dialog.
 * </p>
 * <p>
 * The picker is configurable using the following attributes:
 * </p>
 * <ul>
 * <li><i>android:minDate="&lt;integer&gt;"</i>: Set the minimum value, the
 * picker allows. (DEFAULT 0)</li>
 * <li><i>android:max="&lt;integer&gt;"</i>: Set the maximum value, the picker
 * allows. (DEFAULT 100)</li>
 * <li><i>android:selectable="&lt;boolean&gt;"</i>: Set the wrap style of the
 * picker: <i>true</i> (DEFAULT): picker wraps around, <i>false</i>: picker
 * doesn't wrap around.</li>
 * </ul>
 * <p>
 * The re-/mis-use of the android attributes is done, in order to avoid needing
 * to declare own attributes which you would need to do in your own project.
 * </p>
 *
 * @author <a href="http://stackoverflow.com/users/3455016/rob-meeuwisse">Rob
 *         Meeuwisse</a>
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 * @see <a href=
 *      "http://stackoverflow.com/questions/20758986/android-preferenceactivity-dialog-with-number-picker">
 *      Stack Overflow: Android PreferenceActivity dialog with number picker</a>
 * @see <a href=
 *      "http://droidux.com/programmatically-declare-styleable-custom-view/">
 *      DroidUX: Programmatically Declare Styleable In Custom View</a>
 * @see <a href=
 *      "http://developer.android.com/training/custom-views/create-view.html">
 *      Android Training: Creating a View Class</a>
 */
public class NumberPickerPreference extends DialogPreference {

    private static final int[] sStyleable = {
            android.R.attr.minDate,
            android.R.attr.max,
            android.R.attr.selectable };

    private int mMin;

    private int mMax;

    private boolean mWrap = true;

    private NumberPicker mPicker;

    private int mValue;

    static {
        // This is important. The styleable must be sorted.
        // Otherwise you'll get unexpected result.
        Arrays.sort(sStyleable);
    }

    /**
     * @param context
     *              The Context this is associated with, through which it can access the current
     *              theme, resources, SharedPreferences, etc.
     * @param attrs
     *              The attributes of the XML tag that is inflating the preference.
     */
    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyAttributes(attrs);
    }

    /**
     * @param context
     *              The Context this is associated with, through which it can access the current
     *              theme, resources, SharedPreferences, etc.
     * @param attrs
     *              The attributes of the XML tag that is inflating the preference.
     * @param defStyleAttr
     *              An attribute in the current theme that contains a reference to a style resource
     *              that supplies default values for the view. Can be 0 to not look for defaults.
     */
    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyAttributes(attrs);
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        mPicker = new NumberPicker(getContext());
        mPicker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(mPicker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mPicker.setMinValue(mMin);
        mPicker.setMaxValue(mMax);
        mPicker.setWrapSelectorWheel(mWrap);
        mPicker.setValue(getValue());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mPicker.clearFocus();

            int newValue = mPicker.getValue();

            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, mMin);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(mMin) : (Integer) defaultValue);
    }

    /**
     * Set the value of this preference directly.
     *
     * @param value
     *              The value.
     * @return this object for fluency.
     */
    public NumberPickerPreference setValue(int value) {
        mValue = value;
        persistInt(mValue);

        return this;
    }

    /**
     * @return the currently selected value.
     */
    public int getValue() {
        return mValue;
    }

    /**
     * @return the currently set minimal value.
     */
    public int getMin() {
        return mMin;
    }

    /**
     * Set the minimally selectable value.
     *
     * @param min
     *              The minimum selectable value.
     * @return this object for fluency.
     */
    public NumberPickerPreference setMin(int min) {
        mMin = min;

        mPicker.setMinValue(mMin);
        mPicker.invalidate();
        mPicker.requestLayout();

        return this;
    }

    /**
     * @return the currently set maximal value.
     */
    public int getMax() {
        return mMax;
    }

    /**
     * Set the maximally selectable value.
     *
     * @param max
     *              The maximally selectable value.
     * @return this object for fluency.
     */
    public NumberPickerPreference setMax(int max) {
        mMax = max;

        mPicker.setMaxValue(mMax);
        mPicker.invalidate();
        mPicker.requestLayout();

        return this;
    }

    /**
     * @return if the selector wheel can wrap around.
     */
    public boolean isWrapped() {
        return mWrap;
    }

    /**
     * Set the wrap mode of the selector wheel.
     *
     * @param wrap
     *              true, if wrap should be allowed.
     * @return this object for fluency.
     */
    public NumberPickerPreference setWrap(boolean wrap) {
        mWrap = wrap;

        mPicker.setWrapSelectorWheel(mWrap);
        mPicker.invalidate();
        mPicker.requestLayout();

        return this;
    }

    /**
     * Apply the given attributes to this preference.
     *
     * @param attrs
     *              The attributes of the XML tag that is inflating the preference.
     */
    private void applyAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, sStyleable, 0, 0);

        for (int i = 0; i < sStyleable.length; i++) {
            switch (sStyleable[i]) {
                case android.R.attr.minDate:
                    mMin = a.getInt(i, 0);
                    break;

                case android.R.attr.max:
                    mMax = a.getInt(i, 100);
                    break;

                case android.R.attr.selectable:
                    mWrap = a.getBoolean(i, true);
                    break;
            }
        }

        a.recycle();
    }
}
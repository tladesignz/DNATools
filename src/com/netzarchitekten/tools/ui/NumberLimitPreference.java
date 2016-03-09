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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * <p>
 * A {@link EditTextPreference} that enforces number values inside a range.
 * </p>
 * <p>
 * The preference is configurable using the following attributes:
 * <ul>
 * <li><i>android:minDate="&lt;integer&gt;"</i>: Set the minimum value, the
 * preference allows. (DEFAULT 0)</li>
 * <li><i>android:max="&lt;integer&gt;"</i>: Set the maximum value, the
 * preference allows. (DEFAULT 100)</li>
 * <li><i>android:text="&lt;String&gt;"</i>: Set the error message, if the value
 * is not in the range. (DEFAULT: non-localized, non-specific error message)
 * </li>
 * </ul>
 * </p>
 * <p>
 * The re-/mis-use of the android attributes is done, in order to avoid needing
 * to declare own attributes which you would need to do in your own project.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 * @see NumberPickerPreference
 */
public class NumberLimitPreference extends EditTextPreference implements OnClickListener {

    private static final int[] sStyleable = {
            android.R.attr.minDate,
            android.R.attr.max,
            android.R.attr.text };

    private int mMin;

    private int mMax;

    private String mErrorText;

    static {
        // This is important. The styleable must be sorted.
        // Otherwise you'll get unexpected result.
        Arrays.sort(sStyleable);
    }

    /**
     * @param context
     * @param attrs
     */
    public NumberLimitPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyAttributes(attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public NumberLimitPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyAttributes(attrs);
    }

    /**
     * Set a normal {@link OnClickListener} to the positive button in order to
     * intercept, *before* the dialog is closed.
     */
    @Override
    public void showDialog(Bundle state) {
        super.showDialog(state);

        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
    }

    /**
     * Remove the positive button {@link DialogInterface.OnClickListener}, to
     * stop the dialog closing automatically.
     */
    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setPositiveButton(android.R.string.ok, null);
    }

    /**
     * Check the value. If it's not within bounds or not a number at all, show
     * an error on the {@link EditText}. If ok, save the value and close the
     * dialog.
     */
    @Override
    public void onClick(View v) {
        Double value = null;

        try {
            value = Double.valueOf(getEditText().getText().toString());
        } catch (Exception e) {
            // Trigger an error message further down.
            value = (double) (mMin - 1);
        }

        if (value != null && (value < mMin || value > mMax)) {
            getEditText().requestFocus();
            getEditText().setError(mErrorText == null
                ? String.format("Value needs to be between %d and %d!", mMin, mMax) : mErrorText);
            return;
        }

        onDialogClosed(true);

        getDialog().dismiss();
    }

    /**
     * Apply the given attributes to this preference.
     *
     * @param attrs
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

                case android.R.attr.text:
                    mErrorText = a.getString(i);
                    break;
            }
        }

        a.recycle();
    }
}

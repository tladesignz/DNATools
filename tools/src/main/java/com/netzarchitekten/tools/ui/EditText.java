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

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/**
 * <p>
 * A {@link android.widget.EditText} that has a proper
 * {@link #setReadOnly(boolean)} mode.
 * </p>
 * <p>
 * In order to use this, you have to declare EditTexts as
 * "com.netzarchitekten.tools.ui.EditText" in the layout xml file.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 * @see <a href=
 *      "https://github.com/delight-im/Android-BaseLib/blob/62299c79d100e38627600907e755d563de072234/Source/src/im/delight/android/baselib/UI.java#L264">
 *      GitHub: delight-im/Android-BaseLib</a>
 */
public class EditText extends android.widget.EditText {

    /**
     * Interface definition for a callback to be invoked when a key is pressed
     * on the soft keyboard or the Android buttons and before anything else
     * handles it.
     */
    public interface OnKeyPreImeListener {

        /**
         * Called when the focus state of a key was pressed.
         *
         * @param v
         *            The view where the key was pressed.
         * @param event
         *            Description of the key event.
         * @return If you handled the event, return true. If you want to allow
         *         the event to be handled by the next receiver, return false.
         */
        boolean onKeyPreIme(View v, KeyEvent event);
    }

    protected OnKeyPreImeListener mOnKeyPreImeListener;

    /**
     * @param context
     *              The Context this is associated with, through which it can access the current
     *              theme, resources, SharedPreferences, etc.
     */
    public EditText(Context context) {
        super(context);
    }

    /**
     * @param context
     *              The Context this is associated with, through which it can access the current
     *              theme, resources, SharedPreferences, etc.
     * @param attrs
     *              The attributes of the XML tag that is inflating the preference.
     */
    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
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
    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set an EditText read-only if true.
     *
     * @param readOnly
     *              true, if read-only, false if writable.
     * @return this object for fluency.
     */
    public EditText setReadOnly(boolean readOnly) {
        setFocusable(!readOnly);
        setFocusableInTouchMode(!readOnly);
        setClickable(!readOnly);
        setLongClickable(!readOnly);
        setCursorVisible(!readOnly);

        return this;
    }

    /**
     * Call a {@link OnKeyPreImeListener}, if connected.
     */
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (mOnKeyPreImeListener != null) {
            if (mOnKeyPreImeListener.onKeyPreIme(this, event)) return true;
        }

        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * Register a callback to be invoked when a key is pressed on the soft
     * keyboard or the Android buttons and before anything else handles it.
     *
     * @param l
     *            The callback that will run.
     */
    public void setOnKeyPreImeListener(OnKeyPreImeListener l) {
        mOnKeyPreImeListener = l;
    }
}

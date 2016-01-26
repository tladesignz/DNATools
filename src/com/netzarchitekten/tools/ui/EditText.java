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
     * @param context
     */
    public EditText(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set an EditText read-only if true.
     *
     * @param readOnly
     * @return this object for fluency.
     */
    public EditText setReadOnly(boolean readOnly) {
        this.setFocusable(!readOnly);
        this.setFocusableInTouchMode(!readOnly);
        this.setClickable(!readOnly);
        this.setLongClickable(!readOnly);
        this.setCursorVisible(!readOnly);

        return this;
    }
}

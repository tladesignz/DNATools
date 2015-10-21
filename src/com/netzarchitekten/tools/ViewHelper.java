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

import android.app.Activity;
import android.view.View;
import android.view.ViewParent;

/**
 * <p>
 * Alleviates the pain of handling {@link View#setVisibility(int)} through
 * various static and object methods.
 * </p>
 * <p>
 * Contains a static and an OO interface.
 * </p>
 * <p>
 * Inspired by <a href="http://api.jquery.com/toggle/">jQuery</a>.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public class ViewHelper {

    /**
     * The current activity used in {@link #setView(int)}.
     */
    private Activity mActivity;

    /**
     * The current view to work on.
     */
    private View mView;

    /**
     * Instanciate this class.
     */
    public ViewHelper() {
    }

    /**
     * Instanciate this class and call {@link #setView(View)} immediately.
     *
     * @param v
     *            A {@link View}.
     */
    public ViewHelper(View v) {
        setView(v);
    }

    /**
     * Instanciate this class and call {@link #setView(ViewParent)} immediately.
     *
     * @param vp
     *            A {@link ViewParent}.
     */
    public ViewHelper(ViewParent vp) {
        setView(vp);
    }

    /**
     * Instanciate this class and call {@link #setActivity(Activity)}
     * immediately.
     *
     * @param a
     *            An {@link Activity}.
     */
    public ViewHelper(Activity a) {
        setActivity(a);
    }

    /**
     * Instanciate this class and call {@link #setActivity(Activity)} and
     * {@link #setView(int)} immediately.
     *
     * @param a
     *            An {@link Activity}.
     * @param resId
     *            A resource ID.
     */
    public ViewHelper(Activity a, int resId) {
        setActivity(a).setView(resId);
    }

    /**
     * Set the activity to the given one. Needed for {@link #setView(int)}.
     *
     * @param a
     *            An {@link Activity}.
     * @return this object for fluency.
     */
    public ViewHelper setActivity(Activity a) {
        mActivity = a;

        return this;
    }

    /**
     * Set the view to work on to the given {@link View}.
     *
     * @param v
     *            A {@link View}.
     * @return this object for fluency.
     */
    public ViewHelper setView(View v) {
        mView = v;

        return this;
    }

    /**
     * <p>
     * Set the view to work on to the given {@link ViewParent}.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent} is also a
     * {@link View}! This method merely spares you the cast. No other magic
     * here.
     * </p>
     *
     * @param vp
     *            A {@link ViewParent}.
     * @return this object for fluency.
     */
    public ViewHelper setView(ViewParent vp) {
        mView = (View) vp;

        return this;
    }

    /**
     * <p>
     * Set the view to work on by finding it in the activity's bound layout
     * using it's resource ID.
     * </p>
     * <p>
     * ATTENTION: You need to call {@link #setActivity(Activity)} or instanciate
     * via {@link #ViewHelper(Activity a)} before you call this, otherwise a
     * {@link NullPointerException} will happen!
     * </p>
     *
     * @param resId
     *            A resource ID.
     * @return this object for fluency.
     */
    public ViewHelper setView(int resId) {
        mView = mActivity.findViewById(resId);

        return this;
    }

    /**
     * Hides a {@link View}.
     *
     * @param v
     *            A {@link View} to show.
     */
    public static void hide(View v) {
        toggle(v, false);
    }

    /**
     * <p>
     * Hides a {@link ViewParent}.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent} is also a
     * {@link View}! This method merely spares you the cast. No other magic
     * here.
     * </p>
     *
     * @param vp
     *            A {@link ViewParent} to show or hide.
     */
    public static void hide(ViewParent vp) {
        toggle(vp, false);
    }

    /**
     * Hides the current {@link View}.
     *
     * @return this object for fluency.
     */
    public ViewHelper hide() {
        return toggle(false);
    }

    /**
     * Sets a {@link View} invisible.
     *
     * @param vp
     *            A {@link View} to cloak.
     */
    public static void cloak(View v) {
        new ViewHelper(v).cloak();
    }

    /**
     * <p>
     * Sets a {@link ViewParent} invisible.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent} is also a
     * {@link View}! This method merely spares you the cast. No other magic
     * here.
     * </p>
     *
     * @param vp
     *            A {@link ViewParent} to cloak.
     */
    public static void cloak(ViewParent vp) {
        new ViewHelper(vp).cloak();
    }

    /**
     * Sets the current {@link View} invisible.
     *
     * @return this object for fluency.
     */
    public ViewHelper cloak() {
        mView.setVisibility(View.INVISIBLE);

        return this;
    }

    /**
     * Shows a {@link View}.
     *
     * @param v
     *            A {@link View} to show.
     */
    public static void show(View v) {
        toggle(v, true);
    }

    /**
     * <p>
     * Shows a {@link ViewParent}.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent} is also a
     * {@link View}! This method merely spares you the cast. No other magic
     * here.
     * </p>
     *
     * @param vp
     *            A {@link ViewParent} to show or hide.
     */
    public static void show(ViewParent vp) {
        toggle(vp, true);
    }

    /**
     * Shows the current {@link View}.
     */
    public ViewHelper show() {
        return toggle(true);
    }

    /**
     * Shows or hides the current {@link View}, depending on the toggle
     * parameter.
     *
     * @param toggle
     *            true => show, false => hide
     * @return this object for fluency.
     */
    public ViewHelper toggle(boolean toggle) {
        mView.setVisibility(toggle ? View.VISIBLE : View.GONE);

        return this;
    }

    /**
     * Shows or hides a {@link View}, depending on the toggle parameter.
     *
     * @param v
     *            A {@link View} to show or hide.
     * @param toggle
     *            true => show, false => hide
     */
    public static void toggle(View v, boolean toggle) {
        new ViewHelper(v).toggle(toggle);
    }

    /**
     * <p>
     * Shows or hides a {@link ViewParent}, depending on the toggle parameter.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent} is also a
     * {@link View}! This method merely spares you the cast. No other magic
     * here.
     * </p>
     *
     * @param vp
     *            A {@link ViewParent} to show or hide.
     * @param toggle
     *            true => show, false => hide
     */
    public static void toggle(ViewParent vp, boolean toggle) {
        new ViewHelper(vp).toggle(toggle);
    }
}

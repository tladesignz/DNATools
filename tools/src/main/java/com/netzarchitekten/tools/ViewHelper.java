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
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
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
     * @param view
     *            A {@link View}.
     */
    public ViewHelper(View view) {
        setView(view);
    }

    /**
     * Instanciate this class and call {@link #setView(ViewParent)} immediately.
     *
     * @param viewParent
     *            A {@link ViewParent}.
     */
    public ViewHelper(ViewParent viewParent) {
        setView(viewParent);
    }

    /**
     * Instanciate this class and call {@link #setActivity(Activity)}
     * immediately.
     *
     * @param activity
     *            An {@link Activity}.
     */
    public ViewHelper(Activity activity) {
        setActivity(activity);
    }

    /**
     * Instanciate this class and call {@link #setActivity(Activity)} and
     * {@link #setView(int)} immediately.
     *
     * @param activity
     *            An {@link Activity}.
     * @param resId
     *            A resource ID.
     */
    public ViewHelper(Activity activity, int resId) {
        setActivity(activity).setView(resId);
    }

    /**
     * Set the activity to the given one. Needed for {@link #setView(int)}.
     *
     * @param activity
     *            An {@link Activity}.
     * @return this object for fluency.
     */
    public ViewHelper setActivity(Activity activity) {
        mActivity = activity;

        return this;
    }

    /**
     * Set the view to work on to the given {@link View}.
     *
     * @param view
     *            A {@link View}.
     * @return this object for fluency.
     */
    public ViewHelper setView(View view) {
        mView = view;

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
     * @param viewParent
     *            A {@link ViewParent}.
     * @return this object for fluency.
     */
    public ViewHelper setView(ViewParent viewParent) {
        mView = (View) viewParent;

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
        mView = mActivity != null ? mActivity.findViewById(resId) : null;

        return this;
    }

    /**
     * Hides one or more {@link View}s.
     *
     * @param views
     *            One or more {@link View}s to hide.
     */
    public static void hide(View... views) {
        toggle(false, views);
    }

    /**
     * <p>
     * Hides one or more {@link ViewParent}s.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent}s are also {@link View}s!
     * This method merely spares you the cast. No other magic here.
     * </p>
     *
     * @param viewParents
     *            One or more {@link ViewParent}s to hide.
     */
    public static void hide(ViewParent... viewParents) {
        toggle(false, viewParents);
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
     * Sets one or more {@link View}s invisible.
     *
     * @param views
     *            One or more {@link View}s to cloak.
     */
    public static void cloak(View... views) {
        for (View v : views) {
            new ViewHelper(v).cloak();
        }
    }

    /**
     * <p>
     * Sets one or more {@link ViewParent}s invisible.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent}s are also {@link View}s!
     * This method merely spares you the cast. No other magic here.
     * </p>
     *
     * @param viewParents
     *            One ore more {@link ViewParent}s to cloak.
     */
    public static void cloak(ViewParent... viewParents) {
        for (ViewParent vp : viewParents) {
            new ViewHelper(vp).cloak();
        }
    }

    /**
     * Sets the current {@link View} invisible.
     *
     * @return this object for fluency.
     */
    public ViewHelper cloak() {
        if (mView != null) mView.setVisibility(View.INVISIBLE);

        return this;
    }

    /**
     * Shows one ore more {@link View}s.
     *
     * @param views
     *            One ore more {@link View}s to show.
     */
    public static void show(View... views) {
        toggle(true, views);
    }

    /**
     * <p>
     * Shows one ore more {@link ViewParent}s.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent}s are also {@link View}s!
     * This method merely spares you the cast. No other magic here.
     * </p>
     *
     * @param viewParents
     *            One or more {@link ViewParent}s to show.
     */
    public static void show(ViewParent... viewParents) {
        toggle(true, viewParents);
    }

    /**
     * Shows the current {@link View}.
     *
     * @return this object for fluency.
     */
    public ViewHelper show() {
        return toggle(true);
    }

    /**
     * Shows or hides the current {@link View}, depending on the toggle
     * parameter.
     *
     * @param toggle
     *            Show if true, hide if false.
     * @return this object for fluency.
     */
    public ViewHelper toggle(boolean toggle) {
        if (mView != null) mView.setVisibility(toggle ? View.VISIBLE : View.GONE);

        return this;
    }

    /**
     * Shows or hides a {@link View}, depending on the toggle parameter.
     *
     * @param view
     *            A {@link View} to show or hide.
     * @param toggle
     *            Show if true, hide if false.
     */
    public static void toggle(View view, boolean toggle) {
        new ViewHelper(view).toggle(toggle);
    }

    /**
     * Shows or hides one or more {@link View}s, depending on the toggle parameter.
     *
     * @param toggle
     *            Show if true, hide if false.
     * @param views
     *            One or more {@link View}s to show or hide.
     */
    public static void toggle(boolean toggle, View... views) {
        for (View v : views) {
            toggle(v, toggle);
        }
    }

    /**
     * <p>
     * Shows or hides a {@link ViewParent}, depending on the toggle parameter.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent} is also a {@link View}!
     * This method merely spares you the cast. No other magic here.
     * </p>
     *
     * @param viewParent
     *            A {@link ViewParent} to show or hide.
     * @param toggle
     *            Show if true, hide if false.
     */
    public static void toggle(ViewParent viewParent, boolean toggle) {
        new ViewHelper(viewParent).toggle(toggle);
    }

    /**
     * <p>
     * Shows or hides one or more {@link ViewParent}s, depending on the toggle parameter.
     * </p>
     * <p>
     * ATTENTION: This can only work, if the {@link ViewParent}s are also {@link View}s!
     * This method merely spares you the cast. No other magic here.
     * </p>
     *
     * @param toggle
     *            Show if true, hide if false.
     * @param viewParents
     *            One or more {@link ViewParent}s to show or hide.
     */
    public static void toggle(boolean toggle, ViewParent... viewParents) {
        for (ViewParent vp : viewParents) {
            toggle(vp, toggle);
        }
    }
}

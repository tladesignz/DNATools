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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Class to encapsulate options menu item animation handling.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 * @see <a href=
 *      "http://stackoverflow.com/questions/9731602/animated-icon-for-actionitem">
 *      Stack Overflow: Animated Icon for ActionItem</a>
 * @see <a href=
 *      "http://stackoverflow.com/questions/19139775/animated-menu-item-jumps-
 *      when-animation-starts">Stack Overflow: Animated menu item “jumps” when
 *      animation starts</a>
 */
public class AnimatedMenuItem {

    private final MenuItem mItem;
    private final Animation mAnimation;

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param item
     *            The actual menu item to animate.
     * @param actionLayoutId
     *            The layout used for animation. This can also be set in the
     *            menu.xml file with the "android:actionLayout" attribute.
     * @param animationId
     *            The animation used.
     */
    public AnimatedMenuItem(Activity activity, final Menu menu, MenuItem item,
        Integer actionLayoutId, Integer animationId) {
        mItem = item;

        if (actionLayoutId != null) {
            mItem.setActionView(activity.getLayoutInflater().inflate(actionLayoutId,
                (ViewGroup) mItem.getActionView(), false));
        }

        View actionView = mItem.getActionView();

        if (actionView != null) {
            // When we provide our own layout, this connection has to be done manually.
            actionView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    menu.performIdentifierAction(mItem.getItemId(), 0);
                }
            });
        }

        if (animationId == null) {
            mAnimation = new RotateAnimation(0,
                360,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
            mAnimation.setDuration(1000);
            mAnimation.setInterpolator(new LinearInterpolator());
        } else {
            mAnimation = AnimationUtils.loadAnimation(activity, animationId);
        }

        mAnimation.setRepeatCount(Animation.INFINITE);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param itemId
     *            The actual menu item to animate, provided as resource ID.
     * @param actionLayoutId
     *            The layout used for animation. This can also be set in the
     *            menu.xml file with the "android:actionLayout" attribute.
     * @param animationId
     *            The animation used.
     */
    public AnimatedMenuItem(Activity activity, final Menu menu, int itemId, Integer actionLayoutId,
        int animationId) {
        this(activity, menu, menu.findItem(itemId), actionLayoutId, animationId);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param item
     *            The actual menu item to animate.
     * @param actionLayoutId
     *            The layout used for animation. This can also be set in the
     *            menu.xml file with the "android:actionLayout" attribute.
     */
    public AnimatedMenuItem(Activity activity, Menu menu, MenuItem item, Integer actionLayoutId) {
        this(activity, menu, item, actionLayoutId, null);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param itemId
     *            The actual menu item to animate, provided as resource ID.
     * @param actionLayoutId
     *            The layout used for animation. This can also be set in the
     *            menu.xml file with the "android:actionLayout" attribute.
     */
    public AnimatedMenuItem(Activity activity, Menu menu, int itemId, Integer actionLayoutId) {
        this(activity, menu, menu.findItem(itemId), actionLayoutId, null);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param item
     *            The actual menu item to animate.
     * @param animationId
     *            The animation used.
     */
    public AnimatedMenuItem(Activity activity, Menu menu, MenuItem item, int animationId) {
        this(activity, menu, item, null, animationId);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param itemId
     *            The actual menu item to animate, provided as resource ID.
     * @param animationId
     *            The animation used.
     */
    public AnimatedMenuItem(Activity activity, Menu menu, int itemId, int animationId) {
        this(activity, menu, menu.findItem(itemId), null, animationId);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param item
     *            The actual menu item to animate.
     */
    public AnimatedMenuItem(Activity activity, Menu menu, MenuItem item) {
        this(activity, menu, item, null, null);
    }

    /**
     * Initialize animation on a given {@link MenuItem}.
     *
     * @param activity
     *            Context used for various operations in constructor. (Reference
     *            not kept!)
     * @param menu
     *            Menu used for various operations in constructor. (Reference
     *            not kept!)
     * @param itemId
     *            The actual menu item to animate, provided as resource ID.
     */
    public AnimatedMenuItem(Activity activity, Menu menu, int itemId) {
        this(activity, menu, menu.findItem(itemId), null, null);
    }

    /**
     * Start the animation.
     */
    public void startAnimation() {
        View actionView = mItem.getActionView();

        if (actionView != null) {
            actionView.startAnimation(mAnimation);
        }
    }

    /**
     * Stop the animation.
     */
    public void stopAnimation() {
        View actionView = mItem.getActionView();

        if (actionView != null) actionView.clearAnimation();
    }

    /**
     * @return the actual item.
     */
    public MenuItem get() {
        return mItem;
    }
}

/*
  DNA Android Tools.

  The MIT License (MIT)

  Copyright (c) 2015 - 2018 Die Netzarchitekten e.U., Benjamin Erhart

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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * <p>
 * Model class which extract information about device, Android and app, which can be sent
 * to a server for debugging purposes, e.g. using Gson.
 * </p>
 * <p>
 * Java/Android property naming convention was purposefully broken, to produce a nicer JSON file.
 * </p>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DebugData {
    public final String device_board;

    public final String device_brand;

    public final String device_design;

    public final String device_manufacturer;

    public final String device_model;

    public final String device_product;

    public final String android_version;

    public final int android_sdk;

    public final String app_id;

    public final Integer app_version_code;

    public final String app_version_name;

    public DebugData(Context context) {
        device_board = Build.BOARD;
        device_brand = Build.BRAND;
        device_design = Build.DEVICE;
        device_manufacturer = Build.MANUFACTURER;
        device_model = Build.MODEL;
        device_product = Build.PRODUCT;

        android_version = Build.VERSION.RELEASE;
        android_sdk = Build.VERSION.SDK_INT;

        String appId = null;
        Integer appVersionCode = null;
        String appVersionName = null;

        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            appId = pi.packageName;
            appVersionCode = pi.versionCode;
            appVersionName = pi.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            // Well, we tried...
        }

        app_id = appId;
        app_version_code = appVersionCode;
        app_version_name = appVersionName;
    }
}

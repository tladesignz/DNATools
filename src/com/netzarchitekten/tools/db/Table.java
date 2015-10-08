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
package com.netzarchitekten.tools.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.SimpleCursorAdapter;

/**
 * Base class for SQLite database table models.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class Table {

    /**
     * <p>
     * The table name.
     * </p>
     * <p>
     * Add this constant in your implementation!
     * </p>
     */
    public static final String NAME = null;

    /**
     * <p>
     * The table URI.
     * </p>
     * <p>
     * Add this constant in your implementation!
     * </p>
     */
    public static final Uri URI = null;

    /**
     * <p>
     * The ID column.
     * </p>
     * <p>
     * Add this constant and constants for every other row in your
     * implementation!
     * </p>
     * <p>
     * Note: This example is SQLite's hidden row, which every table has. If your
     * model has an ID row, you should name it "_id", since some parts of
     * Android like to have a so called row (e.g. {@link SimpleCursorAdapter} )
     * to keep track of the loaded rows.
     * </p>
     */
    public static final String COL_ID = "_rowid_";

    /**
     * <p>
     * The query identifying a row uniquely.
     * </p>
     * <p>
     * Add this constant in your implementation!
     * </p>
     */
    public static final String UNIQUE_ROW_QUERY = String.format("%s = ?", COL_ID);

    protected final Context mContext;
    protected final ContentResolver mCr;

    /**
     * Creates this table in the latest version.
     *
     * @param db
     */
    public static void onCreate(SQLiteDatabase db) {
        throw new RuntimeException("Missing implementation!");
    }

    /**
     * Upgrades this table to newVersion.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("Missing implementation!");
    }

    /**
     * Instantiate table model for CRUD operations.
     *
     * @param context
     */
    public Table(Context context) {
        mContext = context.getApplicationContext();
        mCr = mContext.getContentResolver();
    }
}

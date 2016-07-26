/**
 * DNA Android Tools.
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 - 2016 Die Netzarchitekten e.U., Benjamin Erhart
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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.net.URI;

/**
 * Base class for SQLite database content providers.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class DbContentProvider extends ContentProvider {

    /**
     * Pseudo-table which allows raw SELECT queries.
     */
    protected static final int RAW_QUERY = Integer.MAX_VALUE;

    /**
     * A reference to the {@link ContentResolver}.
     */
    protected ContentResolver mCr;

    /**
     * A reference to your {@link SQLiteOpenHelper} implementation provided by
     * {@link #getHelper()}.
     */
    protected SQLiteOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mCr = getContext() != null ? getContext().getContentResolver() : null;

        mHelper = getHelper();

        return false;
    }

    /**
     * This will be called once in {@link #onCreate()}.
     *
     * @return an instance of your SQLiteOpenHelper.
     */
    protected abstract SQLiteOpenHelper getHelper();

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * SELECT rows from a table.
     *
     * @param uri
     *            The table URI.
     * @param projection
     *            The list of columns to put into the cursor. If null all
     *            columns are included.
     * @param selection
     *            A selection criteria to apply when filtering rows. If null
     *            then all rows are included.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the
     *            values from selectionArgs, in order that they appear in the
     *            selection. The values will be bound as Strings.
     * @param sortOrder
     *            How the rows in the cursor should be sorted. If null then the
     *            provider is free to define the sort order.
     * @return a Cursor or null.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        String table = uri.getLastPathSegment();

        Cursor c;

        if (Data.RAW_QUERY.equals(table)) {
            c = getDb().rawQuery(selection, selectionArgs);
        } else {
            c = getDb().query(table, projection, selection, selectionArgs,
                    null, null, sortOrder);
            c.setNotificationUri(mCr, uri);
        }

        return c;
    }
    /**
     * <p>
     * INSERT a table row.
     * </p>
     * <p>
     * For simplification purposes, no real URI is returned, but instead just
     * the row ID itself packaged in a {@link URI} object to suffice the API.
     * </p>
     * <p>
     * So if you want to access the just inserted row, don't use the URI
     * directly, but use the return value as a selectionArg in
     * {@link #query(Uri, String[], String, String[], String)}.
     * </p>
     *
     * @param uri
     *            The table URI.
     * @return only the row ID packaged in a {@link URI}.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = uri.getLastPathSegment();

        if (Data.RAW_QUERY.equals(table)) {
            throw new IllegalArgumentException("Inserts only on allowed on Table models.");
        }

        long id = getDb().insert(table, null, values);
        mCr.notifyChange(uri, null);

        return Uri.parse(String.valueOf(id));
    }

    /**
     * UPDATE table rows.
     *
     * @param uri
     *            The table URI.
     * @param values
     *            A set of column_name/value pairs to update in the database.
     *            This must not be null.
     * @param selection
     *            An optional filter to match rows to update.
     * @return the number of rows affected.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = uri.getLastPathSegment();

        if (Data.RAW_QUERY.equals(table)) {
            throw new IllegalArgumentException("Updates only on allowed on Table models.");
        }

        int rows = getDb().update(table, values, selection, selectionArgs);
        mCr.notifyChange(uri, null);

        return rows;
    }

    /**
     * DELETE table rows.
     *
     * @param uri
     *            The table URI.
     * @param selection
     *            An optional restriction to apply to rows when deleting.
     * @return The number of rows affected.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = uri.getLastPathSegment();

        if (Data.RAW_QUERY.equals(table)) {
            throw new IllegalArgumentException("Delete only on allowed on Table models.");
        }

        int rows = getDb().delete(table, selection, selectionArgs);
        mCr.notifyChange(uri, null);

        return rows;
    }

    /**
     * Ensures, a database, which has to be initialized prior use (e.g. copied from assets),
     * is initialized and returns the handle to it.
     *
     * @return the handle to the database.
     */
    protected SQLiteDatabase getDb() {
        if (mHelper instanceof SQLitePreloadHelper && ((SQLitePreloadHelper)mHelper).needsInit()) {
            ((SQLitePreloadHelper)mHelper).init();
        }

        return mHelper.getWritableDatabase();
    }
}

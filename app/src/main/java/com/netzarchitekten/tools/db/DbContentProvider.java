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

import java.net.URI;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Base class for SQLite database content providers.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class DbContentProvider extends ContentProvider {

    /**
     * <p>
     * The content provider's full qualified name.
     * </p>
     * <p>
     * Override this constant in your implementation!
     * </p>
     */
    private static final String AUTHORITY = null;

    /**
     * <p>
     * The URI to use in queries.
     * </p>
     * <p>
     * Override this constant in your implementation with the same
     * implementation, otherwise this won't work!
     * </p>
     */
    public static final String URI = "content://" + AUTHORITY + "/";

    /**
     * A reference to the {@link ContentResolver}.
     */
    protected ContentResolver mCr;

    /**
     * A reference to your {@link SQLiteOpenHelper} implementation provided by
     * {@link #getHelper()}.
     */
    protected SQLiteOpenHelper mHelper;

    /**
     * A reference to the actual {@link SQLiteDatabase}. This will always be a
     * writable reference and will only be fetched once. (But lazily on first
     * real DB access.)
     */
    protected SQLiteDatabase mDb;

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

    /**
     * Implement your URI-to-table-model matching here.
     *
     * @param uri
     *            The table URI.
     * @return a table matching this URI or NULL.
     */
    protected abstract Class<? extends Table> getTable(Uri uri);

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
        if (mDb == null) mDb = mHelper.getWritableDatabase();

        String[] args = getArgs(uri);

        Cursor c = mDb.query(args[0], projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(mCr, Uri.parse(args[1]));

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
        if (mDb == null) mDb = mHelper.getWritableDatabase();

        String[] args = getArgs(uri);

        long id = mDb.insert(args[0], null, values);
        mCr.notifyChange(Uri.parse(args[1]), null);

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
        if (mDb == null) mDb = mHelper.getWritableDatabase();

        String[] args = getArgs(uri);

        int rows = mDb.update(args[0], values, selection, selectionArgs);
        mCr.notifyChange(Uri.parse(args[1]), null);

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
        if (mDb == null) mDb = mHelper.getWritableDatabase();

        String[] args = getArgs(uri);

        int rows = mDb.delete(args[0], selection, selectionArgs);
        mCr.notifyChange(Uri.parse(args[1]), null);

        return rows;
    }

    /**
     * Fetches the table name and the notification URI of a table matching a
     * given URI.
     *
     * @param uri
     *            The table URI.
     * @return the table name (first element) and notification URI (second element of array).
     */
    private String[] getArgs(Uri uri) {
        Class<? extends Table> table = getTable(uri);

        if (table == null)
            throw new IllegalArgumentException(String.format("Unknown URI: \"%s\"", uri));

        Object name = null;
        Object notificationUri = null;

        try {
            name = table.getField("NAME").get(null);
            notificationUri = table.getField("URI").get(null);
        } catch (IllegalAccessException|IllegalArgumentException|NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (name == null
            || notificationUri == null) { throw new IllegalArgumentException(String.format(
                "The class \"%s\" must contain the static field \"%s\" pointing to a valid object instance!",
                table.getCanonicalName(), name == null ? "NAME" : "URI")); }

        return new String[] { name.toString(), notificationUri.toString() };
    }
}

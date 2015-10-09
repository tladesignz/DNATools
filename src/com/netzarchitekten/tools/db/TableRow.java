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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;

/**
 * Abstract model for table rows, implements some commonly used stuff, like
 * handling the reference to {@link Context} and {@link ContentResolver} and
 * handling the decision if INSERTs or UPDATEs are in order on
 * {@link #save(Uri, Integer, ContentValues, String, String...)}.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class TableRow {

    protected boolean mPersisted = false;

    protected boolean mChanged = false;

    protected Context mContext = null;

    protected ContentResolver mCr = null;

    /**
     * Explicit empty constructor, otherwise compiler cries.
     */
    protected TableRow() {
    }

    /**
     * Set the context.
     *
     * @param context
     */
    protected TableRow(Context context) {
        setContext(context);
    }

    /**
     * Set the context and {@link #mPersisted} to true.
     *
     * @param context
     * @param cursor
     */
    protected TableRow(Context context, Cursor cursor) {
        this(context);

        mPersisted = true;
    }

    /**
     * Set the context. This is needed after object creation from a parcel,
     * because we can't store the context in the parcel.
     *
     * @param context
     * @return this object for fluency.
     */
    public TableRow setContext(Context context) {
        mContext = context == null ? null : context.getApplicationContext();
        mCr = mContext == null ? null : mContext.getContentResolver();

        return this;
    }

    /**
     * @return true, if row is persisted and unchanged, false otherwise.
     */
    public boolean isSaved() {
        return mPersisted && !mChanged;
    }

    /**
     * <p>
     * Store object properties to database table row, if the object is not
     * persisted, yet, or if it was changed.
     * </p>
     * <p>
     * If this object is flagged as {@link #mPersisted}, will do an UPDATE, else
     * checks, if a row with the same primary key(s) value(s) exists. If not,
     * INSERTs, else also UPDATEs and <b>therefor will overwrite rows
     * silently</b>!
     * </p>
     * <p>
     * ATTENTION: If a {@link Context} object wasn't provided via
     * {@link #setContext(Context)}after recreation from a {@link Parcel}, this
     * fails silently!
     * </p>
     *
     * @param uri
     *            The table URI.
     * @param values
     *            The column values.
     * @param where
     *            The selection criteria to identify this row uniquely.
     * @param ids
     *            The column values to identify this row uniquely in the order
     *            used in the where argument.
     * @return The new auto-increment ID of this row, if it was succesfully
     *         INSERTed, or the first ids argument, if it was an UPDATE or if
     *         the INSERT failed. Ignore this on compound primary keys. It won't
     *         make sense in that case.
     */
    protected Long save(Uri uri, ContentValues values, String where, Long... ids) {
        if (mCr != null && (!mPersisted || mChanged)) {
            String[] selectionArgs = new String[ids.length];

            for (int i = 0; i < ids.length; i++) {
                selectionArgs[i] = String.valueOf(ids[i]);
            }

            if (mPersisted) {
                if (mCr.update(uri, values, where, selectionArgs) > 0) {
                    mChanged = false;
                }
            } else {
                // Check, if we can find this row already.
                Cursor c = mCr.query(uri, null, where, selectionArgs, null);

                if (c.getCount() > 0) {
                    // This row is existing, already!
                    mPersisted = true;
                    mChanged = true;

                    // Recall this method, do an UPDATE instead.
                    ids[0] = save(uri, values, where, ids);
                } else {
                    Uri idUri = mCr.insert(uri, values);

                    if (idUri != null) {
                        mPersisted = true;
                        mChanged = false;

                        ids[0] = Long.valueOf(idUri.toString());
                    }
                }
            }
        }

        return ids[0];
    }

    /**
     * Store object to database.
     *
     * @return this object for fluency.
     * @see #save(android.net.Uri, Integer, ContentValues, String, String...)
     */
    public abstract TableRow save();

    /**
     * Delete object from database.
     *
     * @param uri
     *            The table URI.
     * @param where
     *            The selection criteria to identify this row uniquely.
     * @param ids
     *            The column values to identify this row uniquely in the order
     *            used in the where argument.
     * @return true on success, false on failure.
     */
    protected boolean delete(Uri uri, String where, Long... ids) {
        boolean deleted = false;

        if (mCr != null && mPersisted) {
            String[] selectionArgs = new String[ids.length];

            for (int i = 0; i < ids.length; i++) {
                selectionArgs[i] = String.valueOf(ids[i]);
            }

            int count = mCr.delete(uri, where, selectionArgs);

            deleted = count == 1;

            if (deleted) mPersisted = false;
        }

        return deleted;
    }

    /**
     * Delete object from database.
     *
     * @return true on success, false on failure.
     */
    public abstract boolean delete();

    /**
     * <p>
     * Treats an {@link Cursor#FIELD_TYPE_INTEGER} column as boolean: 0, NULL,
     * inexistent column will return false, everything else will return true.
     * </p>
     * <p>
     * Effectively a proxy to {@link Cursor#getColumnIndex(String)} and
     * {@link Cursor#getInt(int)}.
     * </p>
     * <p>
     * Handles inexistent column gracefully.
     * </p>
     *
     * @param c
     *            Cursor pointing to a specific row.
     * @param column
     *            The column name.
     * @return false if 0 or NULL or column inexistent, true else.
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getInt(int)
     */
    protected static boolean getBoolean(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? false : c.getInt(idx) != 0;
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getInt(int)}.
     * </p>
     * <p>
     * Handles inexistent column gracefully.
     * </p>
     *
     * @param c
     *            Cursor pointing to a specific row.
     * @param column
     *            The column name.
     * @return 0 if NULL or column inexistent, or the real column value
     *         otherwise. (Which could be 0...)
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getInt(int)
     */
    protected static int getInt(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? 0 : c.getInt(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getInt(int)}.
     * </p>
     * <p>
     * Handles inexistent column gracefully.
     * </p>
     *
     * @param c
     *            Cursor pointing to a specific row.
     * @param column
     *            The column name.
     * @return NULL if NULL or column inexistent, or the real column value
     *         otherwise. (Which could be 0...)
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getInt(int)
     */
    protected static Integer getIntObject(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getInt(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getLong(int)}.
     * </p>
     * <p>
     * Handles inexistent column gracefully.
     * </p>
     *
     * @param c
     *            Cursor pointing to a specific row.
     * @param column
     *            The column name.
     * @return 0 if NULL or column inexistent, or the real column value
     *         otherwise. (Which could be 0...)
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getLong(int)
     */
    protected static long getLong(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? 0 : c.getLong(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getLong(int)}.
     * </p>
     * <p>
     * Handles inexistent column gracefully.
     * </p>
     *
     * @param c
     *            Cursor pointing to a specific row.
     * @param column
     *            The column name.
     * @return NULL if NULL or column inexistent, or the column value otherwise.
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getLong(int)
     */
    protected static Long getLongObject(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getLong(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getString(int)}.
     * </p>
     * <p>
     * Handles inexistent column gracefully.
     * </p>
     *
     * @param c
     *            Cursor pointing to a specific row.
     * @param column
     *            The column name.
     * @return NULL if NULL or column inexistent, or the real column value.
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getString(int)
     */
    protected static String getString(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getString(idx);
    }

    /**
     * Proxy to {@link Cursor#getColumnIndex(String)} and
     * {@link Cursor#isNull(int)}.
     *
     * @param c
     * @param column
     * @return -1 if column inexistent or it's value is NULL. The column index
     *         otherwise.
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#isNull(int)
     */
    private static int getIndex(Cursor c, String column) {
        int idx = c.getColumnIndex(column);

        return (idx < 0 || c.isNull(idx)) ? -1 : idx;
    }
}
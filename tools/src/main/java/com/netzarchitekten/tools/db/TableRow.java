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
package com.netzarchitekten.tools.db;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;

/**
 * Abstract model for table rows, implements some commonly used stuff, like
 * handling the reference to {@link Context} and {@link ContentResolver} and
 * handling the decision if INSERTs or UPDATEs are in order on
 * {@link #save()}.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings("WeakerAccess")
public abstract class TableRow {

    protected transient boolean mStored = false;

    protected transient boolean mChanged = false;

    protected transient Table<TableRow> mTable = null;

    /**
     * Explicit empty constructor, so subclasses don't need to call other constructors.
     */
    @SuppressWarnings("unused")
    protected TableRow() {
    }

    /**
     * Set the table.
     *
     * @param table
     *              The {@link Table} this {@link TableRow} belongs to.
     */
    protected TableRow(Table<TableRow> table) {
        setTable(table);
    }

    /**
     * Set {@link #mStored} to true.
     *
     * @param cursor
     *              A {@link Cursor} pointing to this row's data in the database.
     */
    @SuppressWarnings("UnusedParameters")
    protected TableRow(Cursor cursor) {
        mStored = true;
    }

    /**
     * Set the table and {@link #mStored} to true.
     *
     * @param table
     *              The {@link Table} this {@link TableRow} belongs to.
     * @param cursor
     *              A {@link Cursor} pointing to this row's data in the database.
     */
    @SuppressWarnings("UnusedParameters")
    protected TableRow(Table<TableRow> table, Cursor cursor) {
        this(table);

        mStored = true;
    }

    /**
     * Set the context. This is needed after object creation from a parcel,
     * because we can't store the context in the parcel.
     *
     * @param table
     *              The {@link Table} this {@link TableRow} belongs to.
     * @return this object for fluency.
     */
    public TableRow setTable(Table<TableRow> table) {
        mTable = table;

        return this;
    }

    /**
     * @return the table belonging to this row or NULL in case this object was created
     *         without one and it wasn't set, yet.
     */
    @SuppressWarnings("unused")
    public Table<TableRow> getTable() {
        return mTable;
    }

    /**
     * Returns the application context, in case you need it somewhere, so you
     * don't have to pass around additional references.
     *
     * @return the application context or NULL in case this object was created
     *         without one and it wasn't set, yet.
     */
    public Context getContext() {
        return mTable != null ? mTable.getContext() : null;
    }

    /**
     * <b>Stored</b> is, when there is a row in the database. It can potentially
     * be different to the currently hold values in the object.
     *
     * @return true, if this row is present in the database already.
     * @see #isSaved()
     */
    @SuppressWarnings("unused")
    public boolean isStored() {
        return mStored;
    }

    /**
     * @return true, if this object was modified via its setters.
     */
    @SuppressWarnings("unused")
    public boolean isChanged() {
        return mChanged;
    }

    /**
     * <b>Saved</b> is, when there is a row in the database <b>and</b> the
     * values in the object definitely contain the same values as in the
     * database.
     *
     * @return true, if row is stored and unchanged, false otherwise.
     * @see #isStored()
     * @see #isChanged()
     */
    @SuppressWarnings("unused")
    public boolean isSaved() {
        return mStored && !mChanged;
    }

    /**
     * <p>
     * Store object properties to database table row, if the object is not
     * {@link #isSaved()}.
     * </p>
     * <p>
     * If this object is flagged as {@link #mStored}, will do an UPDATE, else
     * checks, if a row with the same primary key(s) value(s) exists. If not,
     * INSERTs, else also UPDATEs and <b>therefor will overwrite rows
     * silently</b>!
     * </p>
     * <p>
     * ATTENTION: If a {@link Table} object wasn't provided via
     * {@link #setTable(Table)}after recreation from a {@link Parcel}, this
     * fails silently!
     * </p>
     * 
     * @param values
     *            The column values.
     * @param ids
     *            The column values to identify this row uniquely in the order
     *            used in the where argument.
     * @return The new auto-increment ID of this row, if it was successfully
     *         INSERTed, or the first ids argument, if it was an UPDATE or if
     *         the INSERT failed. Ignore this on compound primary keys. It won't
     *         make sense in that case.
     */
    @SuppressWarnings("unused")
    protected Long save(ContentValues values, Long... ids) {
        return Long.valueOf(save(values, toStrings(ids)));
    }

    /**
     * <p>
     * Store object properties to database table row, if the object is not
     * {@link #isSaved()}.
     * </p>
     * <p>
     * If this object is flagged as {@link #mStored}, will do an UPDATE, else
     * checks, if a row with the same primary key(s) value(s) exists. If not,
     * INSERTs, else also UPDATEs and <b>therefore will overwrite rows
     * silently</b>!
     * </p>
     * <p>
     * ATTENTION: If a {@link Table} object wasn't provided via
     * {@link #setTable(Table)}after recreation from a {@link Parcel}, this
     * fails silently!
     * </p>
     *
     * @param values
     *            The column values.
     * @param ids
     *            The column values to identify this row uniquely in the order
     *            used in the where argument.
     * @return The new auto-increment ID of this row, if it was succesfully
     *         INSERTed, or the first ids argument, if it was an UPDATE or if
     *         the INSERT failed. Ignore this on compound primary keys. It won't
     *         make sense in that case.
     */
    protected String save(ContentValues values, String... ids) {
        if (mTable != null && (!mStored || mChanged)) {
            if (mStored) {
                if (mTable.updateUniqueRow(values, ids) > 0) {
                    mChanged = false;
                }
            } else {
                // Check, if we can find this row already.
                if (mTable.has(ids)) {
                    // This row is existing, already!
                    mStored = true;
                    mChanged = true;

                    // Recall this method, do an UPDATE instead.
                    ids[0] = save(values, ids);
                } else {
                    String id = mTable.insert(values);

                    if (id != null) {
                        mStored = true;
                        mChanged = false;

                        ids[0] = id;
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
     * @see #save(ContentValues, String...)
     */
    @SuppressWarnings("unused")
    public abstract TableRow save();

    /**
     * Delete object from database.
     *
     * @param ids
     *            The column values to identify this row uniquely in the order
     *            used in the where argument.
     * @return true on success, false on failure.
     */
    @SuppressWarnings("unused")
    protected boolean delete(Object... ids) {
        boolean deleted = false;

        if (mTable != null && mStored) {
            int count = mTable.deleteUniqueRow(toStrings(ids));

            deleted = count > 0;

            if (deleted) mStored = false;
        }

        return deleted;
    }

    /**
     * Delete object from database.
     *
     * @return true on success, false on failure.
     */
    @SuppressWarnings("unused")
    public abstract boolean delete();

    /**
     * <p>
     * Treats an {@link Cursor#FIELD_TYPE_INTEGER} column as boolean: 0, NULL,
     * inexistent column will return false, everything else will return true.
     * </p>
     * <p>
     * Effectively a proxy to {@link Cursor#getColumnIndex(String)}, {@link Cursor#isNull(int)} and
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
    @SuppressWarnings("unused")
    public static boolean getBoolean(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx >= 0 && c.getInt(idx) != 0;
    }

    /**
     * <p>
     * Treats an {@link Cursor#FIELD_TYPE_INTEGER} column as Boolean object: NULL,
     * inexistent column will return null, 0 will return false, everything else will return true.
     * </p>
     * <p>
     * Effectively a proxy to {@link Cursor#getColumnIndex(String)}, {@link Cursor#isNull(int)} and
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
     * @return null if NULL or column inexistent, false if 0, true else.
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#getInt(int)
     */
    @SuppressWarnings("unused")
    public static Boolean getBooleanObj(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getInt(idx) != 0;
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
    @SuppressWarnings("unused")
    public static int getInt(Cursor c, String column) {
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
    @SuppressWarnings("unused")
    public static Integer getIntObj(Cursor c, String column) {
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
    @SuppressWarnings("unused")
    public static long getLong(Cursor c, String column) {
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
    public static Long getLongObj(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getLong(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getFloat(int)}.
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
     * @see Cursor#getFloat(int)
     */
    @SuppressWarnings("unused")
    public static float getFloat(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? 0 : c.getFloat(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getFloat(int)}.
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
     * @see Cursor#getFloat(int)
     */
    @SuppressWarnings("unused")
    public static Float getFloatObj(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getFloat(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getDouble(int)}.
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
     * @see Cursor#getDouble(int)
     */
    @SuppressWarnings("unused")
    public static double getDouble(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? 0 : c.getDouble(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getDouble(int)}.
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
     * @see Cursor#getDouble(int)
     */
    @SuppressWarnings("unused")
    public static Double getDoubleObj(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getDouble(idx);
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
    @SuppressWarnings("unused")
    public static String getString(Cursor c, String column) {
        int idx = getIndex(c, column);

        return idx < 0 ? null : c.getString(idx);
    }

    /**
     * <p>
     * Proxy to {@link Cursor#getColumnIndex(String)},
     * {@link Cursor#isNull(int)} and {@link Cursor#getLong(int)} and finally
     * create a {@link Calendar} object out of it, where the long value is
     * considered to be milliseconds since Jan., 1st 1970.
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
    @SuppressWarnings("unused")
    public static Calendar getCalendar(Cursor c, String column) {
        Long timestamp = getLongObj(c, column);

        if (timestamp == null) return null;

        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(timestamp);

        return cal;
    }

    /**
     * Proxy to {@link Cursor#getColumnIndex(String)} and
     * {@link Cursor#isNull(int)}.
     *
     * @param c
     *              A {@link Cursor}.
     * @param column
     *              The name of the target column.
     * @return -1 if column inexistent or it's value is NULL. The column index
     *         otherwise.
     * @see Cursor#getColumnIndex(String)
     * @see Cursor#isNull(int)
     */
    private static int getIndex(Cursor c, String column) {
        int idx = c.getColumnIndex(column);

        return (idx < 0 || c.isNull(idx)) ? -1 : idx;
    }

    /**
     * Convert an array of objects to an array of strings properly.
     *
     * @param objects
     *            The array of objects.
     * @return an array of stringified objects.
     */
    private static String[] toStrings(Object[] objects) {
        String[] strings = new String[objects.length];

        for (int i = 0; i < objects.length; i++) {
            strings[i] = String.valueOf(objects[i]);
        }

        return strings;
    }
}

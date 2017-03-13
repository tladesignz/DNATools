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

import android.database.Cursor;

import java.util.Locale;

/**
 * Implementation of a result row for the SQLite <code>PRAGMA table_info(table)</code>.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ColumnInfo extends TableRow {

    public static final String COL_ID = "cid";
    public static final String COL_NAME = "name";
    public static final String COL_TYPE = "type";
    public static final String COL_NOT_NULL = "notnull";
    public static final String COL_DEFAULT_VALUE = "dflt_value";
    public static final String COL_PRIMARY_KEY = "pk";

    private final long mId;

    private final String mName;

    private final String mType;

    private final boolean mNotNull;

    private final String mDefaultValue;

    private final boolean mPrimaryKey;

    /**
     * Construct from a {@link Cursor}.
     *
     * @param c
     *            A {@link Cursor} pointing to a result row of <code>PRAGMA table_info()</code>.
     */
    public ColumnInfo(Cursor c) {
        super(c);

        mId = getLong(c, COL_ID);
        mName = getString(c, COL_NAME);
        mType = getString(c, COL_TYPE);
        mNotNull = getBoolean(c, COL_NOT_NULL);
        mDefaultValue = getString(c, COL_DEFAULT_VALUE);
        mPrimaryKey = getBoolean(c, COL_PRIMARY_KEY);
    }

    /**
     * <p>
     * Construct from just a name to be used in comparison operations.
     * </p>
     * <p>
     * All other field will be set to null, false or MIN_VALUE.
     * </p>
     *
     * @param name
     *            The column name.
     */
    public ColumnInfo(String name) {
        mId = Long.MIN_VALUE;
        mName = name;
        mType = null;
        mNotNull = false;
        mDefaultValue = null;
        mPrimaryKey = false;
    }

    /**
     * <p>
     * Construct from given arguments.
     * </p>
     * <p>
     * This is just here for the sake of completeness for whatever reasons you may have to use this.
     * </p>
     * @param id
     *            The column ID.
     * @param name
     *            The column name.
     * @param type
     *            The column type.
     * @param notNull
     *            true if not allowed to contain NULL.
     * @param defaultValue
     *            The column's default value.
     * @param primaryKey
     *            true, if part of a primary key.
     */
    public ColumnInfo(long id, String name, String type, boolean notNull, String defaultValue,
                      boolean primaryKey) {
        mId = id;
        mName = name;
        mType = type;
        mNotNull = notNull;
        mDefaultValue = defaultValue;
        mPrimaryKey = primaryKey;
    }

    /**
     * @return the column ID.
     */
    public long getId() {
        return mId;
    }

    /**
     * @return the column name.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the column type.
     */
    public String getType() {
        return mType;
    }

    /**
     * @return true, if column is not nullable.
     */
    public boolean isNotNull() {
        return mNotNull;
    }

    /**
     * @return the column's default value.
     */
    public String getDefaultValue() {
        return mDefaultValue;
    }

    /**
     * @return true, if column is a primary key or part of it.
     */
    public boolean isPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * Read-only class! This method has no effect!
     *
     * @return this object for fluency.
     */
    @Override
    public ColumnInfo save() {
        return this;
    }

    /**
     * Read-only class! This method has no effect!
     *
     * @return always false.
     */
    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
            "%s[mId=%d, mName=%s, mType=%s, mNotNull=%b, mDefaultValue=%s, mPrimaryKey=%b]",
            getClass().getName(), mId, mName, mType, mNotNull, mDefaultValue, mPrimaryKey);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnInfo that = (ColumnInfo) o;

        return mName != null ? mName.equals(that.mName) : that.mName == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mType != null ? mType.hashCode() : 0);
        result = 31 * result + (mNotNull ? 1 : 0);
        result = 31 * result + (mDefaultValue != null ? mDefaultValue.hashCode() : 0);
        result = 31 * result + (mPrimaryKey ? 1 : 0);
        return result;
    }
}

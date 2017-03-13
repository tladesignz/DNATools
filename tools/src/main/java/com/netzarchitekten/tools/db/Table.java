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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for SQLite database table models.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings("WeakerAccess")
public abstract class Table<T extends TableRow> extends Data {

    protected final Class<T> mTableRowClass;

    protected final Uri mUri;

    protected final String mUniqueRowSelection;

    /**
     * Creates this table in the latest version.
     *
     * @param db
     *              The database.
     */
    @SuppressWarnings("unused")
    public static void onCreate(SQLiteDatabase db) {
        throw new RuntimeException("Missing implementation!");
    }

    /**
     * Upgrades this table to newVersion.
     *
     * @param db
     *              The database.
     * @param oldVersion
     *              The old database version.
     * @param newVersion
     *              The new database version.
     */
    @SuppressWarnings("unused")
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("Missing implementation!");
    }

    /**
     * Instantiate table model for CRUD operations.
     *
     * @param tableRowClass
     *              The class of the {@link TableRow}. Needs to be explicitly given here, because
     *              at runtime this information is sometimes lost, otherwise.
     * @param context
     *              A {@link Context}.
     * @param baseUri
     *              The base URI like this: "content://com.example.app.db.contentprovider/"
     * @param name
     *              The table name.
     * @param uniqueRowKeys
     *              The columns names which are needed to identify a row uniquely.
     * @see <a href="http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime"
     * >Stackoverflow</a>
     */
    public Table(Class<T> tableRowClass, Context context, String baseUri, String name,
                 String[] uniqueRowKeys) {
        super(context, baseUri);

        mTableRowClass = tableRowClass;

        mUri = Uri.parse(baseUri + name);

        String[] keys = new String[uniqueRowKeys.length];

        for (int i = 0; i < uniqueRowKeys.length; i++) {
            keys[i] = String.format("%s = ?", uniqueRowKeys[i]);
        }

        mUniqueRowSelection = TextUtils.join(" AND ", keys);
    }

    /**
     * Instantiate table model for CRUD operations.
     *
     * @param tableRowClass
     *              The class of the {@link TableRow}. Needs to be explicitly given here, because
     *              at runtime this information is sometimes lost, otherwise.
     * @param context
     *              A {@link Context}.
     * @param baseUri
     *              The base URI like this: "content://com.example.app.db.contentprovider/"
     * @param name
     *              The table name.
     * @param uniqueRowKey
     *              The column name which is needed to identify a row uniquely.
     * @see <a href="http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime"
     * >Stackoverflow</a>
     */
    @SuppressWarnings("unused")
    public Table(Class<T> tableRowClass, Context context, String baseUri, String name,
                 String uniqueRowKey) {
        this(tableRowClass, context, baseUri, name, new String[] { uniqueRowKey });
    }

    /**
     * @return the URI for this table.
     */
    @SuppressWarnings("unused")
    public Uri getUri() {
        return mUri;
    }

    /**
     * @return the selection {@link String} which identifies a row in this table uniquely.
     */
    @SuppressWarnings("unused")
    public String getUniqueRowSelection() {
        return mUniqueRowSelection;
    }

    /**
     * <p>
     * Instantiates a new, unstored row containing only default values.
     * </p>
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table)} – otherwise this method will fail horribly, in order to
     * warn you as early as possible about the design failure.
     * </p>
     *
     * @return a new, unstored {@link TableRow} subclass.
     */
    @SuppressWarnings("unused")
    public T newRow() {
        //noinspection TryWithIdenticalCatches
        try {
            return mTableRowClass.getConstructor(this.getClass()).newInstance(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    /**
     * <p>
     * Instantiates a new, stored row from a given {@link Cursor} which has to point to a
     * database table row.
     * </p>
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param c
     *            A {@link Cursor} pointing to a row in this table.
     *
     * @return a {@link TableRow} subclass containing values from the database.
     */
    @SuppressWarnings("unused")
    public T newRow(Cursor c) {
        //noinspection TryWithIdenticalCatches
        try {
            return mTableRowClass.getConstructor(this.getClass(), Cursor.class).newInstance(this, c);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    /**
     * <p>
     * Fetch the selected rows ordered by the given order with limit.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param projection
     *            A list of which columns to return. Passing null will return all columns, which
     *            is inefficient.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @param sortOrder
     *            How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *            ORDER BY itself). Passing null will use the default sort order,
     *            which may be unordered.
     * @param limit
     *            A maximum amount of result entries.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String[] projection, String selection, String[] selectionArgs,
                              String sortOrder, Integer limit) {

        List<T> rows = new ArrayList<>();

        //noinspection TryWithIdenticalCatches
        try {
            Cursor c = mCr.query(mUri, projection, selection, selectionArgs, sortOrder);

            if (c != null) {
                if (c.moveToFirst()) {
                    int i = 0;
                    Constructor<T> constructor =
                        mTableRowClass.getConstructor(this.getClass(), Cursor.class);

                    do {
                        rows.add(constructor.newInstance(this, c));
                        i++;
                    } while ((limit == null || limit > i) && c.moveToNext());
                }

                c.close();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

            // We don't want to pass the exceptions up or rethrow a more generic exception, since
            // then, we would have to handle this on every object instantiation, but these
            // exceptions should never throw in production, since these are static code issues
            // (wrongly defined classes) which should be handled once, straight away and then
            // never change again.
            System.exit(-1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return rows;
    }

    /**
     * <p>
     * Fetch the selected rows ordered by the given order with limit.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @param sortOrder
     *            How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *            ORDER BY itself). Passing null will use the default sort order,
     *            which may be unordered.
     * @param limit
     *            A maximum amount of result entries.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection, String[] selectionArgs, String sortOrder,
                              Integer limit) {

        return getList(null, selection, selectionArgs, sortOrder, limit);
    }

    /**
     * <p>
     * Fetch all selected rows ordered by the given order.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @param sortOrder
     *            How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *            ORDER BY itself). Passing null will use the default sort order,
     *            which may be unordered.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection, String[] selectionArgs,
                              String sortOrder) {
        return getList(selection, selectionArgs, sortOrder, null);
    }

    /**
     * <p>
     * Fetch the selected rows unordered and unlimited.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection, String[] selectionArgs) {
        return getList(selection, selectionArgs, null);
    }

    /**
     * <p>
     * Fetch the selected rows unordered and unlimited.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @return a list of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection) {
        return getList(selection, null);
    }

    /**
     * <p>
     * Fetch all rows in the table, unordered.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    @SuppressWarnings("unused")
    protected List<T> getList() {
        return getList(null);
    }

    /**
     * <p>
     * Fetch the selected row.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param projection
     *            A list of which columns to return. Passing null will return all columns, which
     *            is inefficient.
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @param sortOrder
     *            How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *            ORDER BY itself). Passing null will use the default sort order,
     *            which may be unordered.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        List<T> rows = getList(projection, selection, selectionArgs, sortOrder, 1);

        return rows != null && rows.size() > 0 ? rows.get(0) : null;
    }

    /**
     * <p>
     * Fetch the selected row.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @param sortOrder
     *            How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *            ORDER BY itself). Passing null will use the default sort order,
     *            which may be unordered.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String selection, String[] selectionArgs, String sortOrder) {
        return get(null, selection, selectionArgs, sortOrder);
    }

    /**
     * <p>
     * Fetch the selected row.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String selection, String[] selectionArgs) {
        return get(selection, selectionArgs, null);
    }

    /**
     * <p>
     * Fetch the selected row.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String selection) {
        return get(selection, null);
    }

    /**
     * <p>
     * Fetch a unique row as defined in the constructor.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @param selectionArgs
     *            Values for all columns which identify a row uniquely.
     *
     * @return a {@link TableRow}, possibly null.
     */
    @SuppressWarnings("unused")
    protected T get(String[] selectionArgs) {
        return get(mUniqueRowSelection, selectionArgs);
    }

    /**
     * <p>
     * Fetch a random row.
     * <p>
     * Your {@link TableRow} implementation will need a constructor like this:
     * {@link TableRow#TableRow(Table, Cursor)} – otherwise this method will fail horribly, in order
     * to warn you as early as possible about the design failure.
     * </p>
     *
     * @return a {@link TableRow}, possibly null.
     */
    @SuppressWarnings("unused")
    protected T get() {
        return get((String) null);
    }

    /**
     * Count the number of selected rows.
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return the number of selected rows.
     */
    public int count(String selection, String[] selectionArgs) {
        int count = 0;

        Cursor c = mCr.query(mUri, null, selection, selectionArgs, null);

        if (c != null) {
            count = c.getCount();

            c.close();
        }

        return count;
    }

    /**
     * Count the number of selected rows.
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @return the number of selected rows.
     */
    public int count(String selection) {
        return count(selection, null);
    }

    /**
     * Count the number of all rows.
     *
     * @return the number of all rows.
     */
    @SuppressWarnings("unused")
    public int count() {
        return count(null);
    }

    /**
     * Check, if there are selected rows available.
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return true, if there are rows, false if not.
     */
    public boolean has(String selection, String[] selectionArgs) {
        return count(selection, selectionArgs) > 0;
    }

    /**
     * Check, if there are selected rows available.
     *
     * @param selection
     *            A filter declaring which rows to return, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     *            Passing null will return all rows for the given URI.
     * @return true, if there are rows, false if not.
     */
    public boolean has(String selection) {
        return has(selection, null);
    }

    /**
     * Check, if there is a unique (as defined in the constructor) row available.
     *
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return true, if there are rows, false if not.
     */
    public boolean has(String[] selectionArgs) {
        return has(mUniqueRowSelection, selectionArgs);
    }

    /**
     * Check, if there are any rows available.
     *
     * @return true, if there are rows, false if not.
     */
    @SuppressWarnings("unused")
    public boolean has() {
        return has((String) null);
    }

    /**
     * Update row(s) of this table.
     *
     * @param values
     *            The new field values. The key is the column name for the field. A null value will
     *            remove an existing field value.
     * @param where
     *            A filter to apply to rows before updating, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return the number of rows updated.
     */
    public int update(ContentValues values, String where, String[] selectionArgs) {
        return mCr.update(mUri, values, where, selectionArgs);
    }

    /**
     * Update row(s) of this table.
     *
     * @param values
     *            The new field values. The key is the column name for the field. A null value will
     *            remove an existing field value.
     * @param where
     *            A filter to apply to rows before updating, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     * @return the number of rows updated.
     */
    @SuppressWarnings("unused")
    public int update(ContentValues values, String where) {
        return update(values, where, null);
    }

    /**
     * Update a unique row as defined in the constructor.
     *
     * @param values
     *            The new field values. The key is the column name for the field. A null value will
     *            remove an existing field value.
     * @param selectionArgs
     *            Values for all columns which identify a row uniquely.
     *
     * @return the number of rows deleted.
     */
    public int updateUniqueRow(ContentValues values, String[] selectionArgs) {
        return update(values, mUniqueRowSelection, selectionArgs);
    }

    /**
     * Delete row(s) from this table.
     *
     * @param where
     *            A filter to apply to rows before updating, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     * @param selectionArgs
     *            You may include ?s in selection, which will be replaced by the values
     *            from selectionArgs, in the order that they appear in the selection.
     *            The values will be bound as Strings.
     * @return the number of rows deleted.
     */
    public int delete(String where, String[] selectionArgs) {
        return mCr.delete(mUri, where, selectionArgs);
    }

    /**
     * Delete row(s) from this table.
     *
     * @param where
     *            A filter to apply to rows before updating, formatted as an SQL WHERE clause
     *            (excluding the WHERE itself).
     * @return the number of rows deleted.
     */
    @SuppressWarnings("unused")
    public int delete(String where) {
        return delete(where, null);
    }

    /**
     * Delete a unique row as defined in the constructor.
     *
     * @param selectionArgs
     *            Values for all rows which identify a row uniquely.
     *
     * @return the number of rows deleted.
     */
    public int deleteUniqueRow(String[] selectionArgs) {
        return delete(mUniqueRowSelection, selectionArgs);
    }

    /**
     * Insert a row into this table.
     *
     * @param values
     *            The new field values. The key is the column name for the field. A null value will
     *            remove an existing field value.
     * @return the primary key as {@link String} or NULL if insert wasn't successful.
     */
    public String insert(ContentValues values) {
        Uri uri = mCr.insert(mUri, values);

        return uri != null ? uri.toString() : null;
    }
}

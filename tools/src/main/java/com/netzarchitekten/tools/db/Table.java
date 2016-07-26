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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for SQLite database table models.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class Table<T extends TableRow> extends Data {

    protected final Uri mUri;

    protected final Class<T> mTableRowClass;

    /**
     * Creates this table in the latest version.
     *
     * @param db
     *              The database.
     */
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
     * @see <a href="http://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime"
     * >Stackoverflow</a>
     */
    public Table(Class<T> tableRowClass, Context context, String baseUri, String name) {
        super(context, baseUri);

        mUri = Uri.parse(baseUri + name);

        mTableRowClass = tableRowClass;
    }

    /**
     * Fetch the selected rows ordered by the given order with limit.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                  ORDER BY itself). Passing null will use the default sort order,
     *                  which may be unordered.
     * @param limit A maximum amount of result entries.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection, String[] selectionArgs, String sortOrder,
                              Integer limit) {

        List<T> rows = new ArrayList<>();

        try {
            Cursor c = mCr.query(mUri, null, selection, selectionArgs, sortOrder);

            if (c != null) {
                if (c.moveToFirst()) {
                    int i = 0;
                    Constructor<T> constructor =
                            mTableRowClass.getConstructor(Context.class, Cursor.class);

                    do {
                        rows.add(constructor.newInstance(mContext, c));
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
     * Fetch all selected rows ordered by the given order.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                  ORDER BY itself). Passing null will use the default sort order,
     *                  which may be unordered.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection, String[] selectionArgs,
                              String sortOrder) {
        return getList(selection, selectionArgs, sortOrder, null);
    }

    /**
     * Fetch the selected rows unordered and unlimited.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection, String[] selectionArgs) {
        return getList(selection, selectionArgs, null);
    }

    /**
     * Fetch the selected rows, unordered and unlimited.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @return a list of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList(String selection) {
        return getList(selection, null);
    }

    /**
     * Fetch all rows in the table, unordered.
     *
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    protected List<T> getList() {
        return getList(null);
    }

    /**
     * Fetch the selected row.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                  ORDER BY itself). Passing null will use the default sort order,
     *                  which may be unordered.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String selection, String[] selectionArgs, String sortOrder) {
        List<T> rows = getList(selection, selectionArgs, sortOrder, 1);

        return rows != null && rows.size() > 0 ? rows.get(0) : null;
    }

    /**
     * Fetch the selected row.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String selection, String[] selectionArgs) {
        return get(selection, selectionArgs, null);
    }

    /**
     * Fetch the selected row.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @return a {@link TableRow}, possibly null.
     */
    protected T get(String selection) {
        return get(selection, null);
    }

    /**
     * Fetch a random row.
     *
     * @return a {@link TableRow}, possibly null.
     */
    protected T get() {
        return get(null);
    }
}

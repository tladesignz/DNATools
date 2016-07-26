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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for SQLite database models.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class Data {

    /**
     * Identifier indicating a raw query, does not necessarily produce a complete table row.
     */
    public static final String RAW_QUERY = "_raw_query_";

    protected final Context mContext;
    protected final ContentResolver mCr;

    protected final Uri mRawUri;

    public Data(Context context, String baseUri) {
        mContext = context.getApplicationContext();
        mCr = mContext.getContentResolver();
        mRawUri = Uri.parse(baseUri + RAW_QUERY);
    }

    /**
     * Execute a raw query.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @return a {@link Cursor} pointing to the result set.
     */
    public Cursor rawQuery(String selection, String[] selectionArgs) {
        return mCr.query(mRawUri, null, selection, selectionArgs, null);
    }

    /**
     * Execute a raw query.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param limit A maximum amount of result entries.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    public <T extends TableRow> List<T> getRawList(Class<T> tableRowClass, String selection,
                                                String[] selectionArgs, Integer limit) {

        List<T> rows = new ArrayList<>();

        try {
            Cursor c = rawQuery(selection, selectionArgs);

            if (c != null) {
                if (c.moveToFirst()) {
                    int i = 0;
                    Constructor<T> constructor =
                            tableRowClass.getConstructor(Context.class, Cursor.class);

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
     * Fetch the selected rows, unordered and unlimited.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in the order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    public <T extends TableRow> List<T> getRawList(Class<T> tableRowClass, String selection, String[] selectionArgs) {
        return getRawList(tableRowClass, selection, selectionArgs, null);
    }

    /**
     * Fetch the selected rows, unordered and unlimited.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @return a {@link List} of {@link TableRow}s, possibly empty, never null.
     */
    public <T extends TableRow> List<T> getRawList(Class<T> tableRowClass, String selection) {
        return getRawList(tableRowClass, selection, null);
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
    public <T extends TableRow> T getRaw(Class<T> tableRowClass, String selection, String[] selectionArgs) {
        List<T> rows = getRawList(tableRowClass, selection, selectionArgs, 1);

        return rows != null && rows.size() > 0 ? rows.get(0) : null;
    }

    /**
     * Fetch the selected row.
     *
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself).
     *                  Passing null will return all rows for the given URI.
     * @return a {@link TableRow}, possibly null.
     */
    public <T extends TableRow> T getRaw(Class<T> tableRowClass, String selection) {
        return getRaw(tableRowClass, selection, null);
    }
}

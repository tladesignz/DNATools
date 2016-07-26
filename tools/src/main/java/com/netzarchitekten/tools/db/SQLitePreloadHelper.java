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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Base class for SQLite databases which files have to be initialized <b>before</b> a handle to
 * it is opened.
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
public abstract class SQLitePreloadHelper extends SQLiteOpenHelper {

    /**
     * The application {@link Context} for further use, e.g. in {@link #needsInit()} and
     * {@link #init()}.
     */
    protected final Context mContext;

    /**
     *  Create a helper object to create, open, and/or manage a database.
     *  This method always returns very quickly.
     *  The database is not actually created or opened until one of {@link #getWritableDatabase()}
     *  or {@link #getReadableDatabase()} is called.
     *
     * @param context
     *            to use to open or create the database
     * @param name
     *            of the database file, or null for an in-memory database
     * @param factory
     *            to use for creating cursor objects, or null for the default
     * @param version
     *            number of the database (starting at 1); if the database is older,
     *            {@link #onUpgrade(SQLiteDatabase, int, int)} will be used to upgrade the database;
     *            if the database is newer, {@link #onDowngrade(SQLiteDatabase, int, int)} will be
     *            used to downgrade the database
     */
    public SQLitePreloadHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                               int version) {
        super(context, name, factory, version);

        mContext = context != null ? context.getApplicationContext() : null;
    }

    /**
     * <p>
     * Implement your check here, if you need to do something, <b>before</b> the handle to the
     * database is opened.
     * </p>
     * <p>
     * <b>ATTENTION</b>: This is called right before <b>every</b> database access, so keep this as
     * performant as possible!
     * </p>
     *
     * @return true, if {@link #init()} needs to be called.
     */
    public abstract boolean needsInit();

    /**
     * Implement your database initialization code here. (e.g. copy over from assets)
     */
    public abstract void init();
}

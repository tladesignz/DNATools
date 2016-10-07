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
package com.netzarchitekten.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

/**
 * This helper simplifies the handling of adapters and data Lists for ListViews.
 * Use like this:
 *
 * <pre>
 * ListAdapterHelper lah = new ListAdapterHelper(this,
 *     R.layout.foo,
 *     new String[] { &quot;foo&quot;, &quot;bar&quot; },
 *     new int[] { R.id.foo, R.id.bar });
 *
 * mListView.setAdapter(lah.adapter);
 *
 * lah.add(&quot;baz&quot;, &quot;bam&quot;);
 *
 * lah.commit();
 * </pre>
 *
 * @author Benjamin Erhart {@literal <berhart@netzarchitekten.com>}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class ListAdapterHelper {

    public final SimpleAdapter adapter;

    public final List<HashMap<String, String>> data = new ArrayList<>();

    private final Context mContext;

    private final String[] mFrom;

    public ListAdapterHelper(Context context, int resource, String[] from, int[] to) {
        mContext = context;
        mFrom = from;
        adapter = new SimpleAdapter(context, data, resource, from, to);
    }

    /**
     * Add a new HashMap entry to the List. Populate the HashMap with the values
     * given using the keys from the constructor.
     *
     * @param strings
     *              Texts to add.
     */
    public void add(String... strings) {
        HashMap<String, String> map = new HashMap<>();
        int i = 0;

        for (String string : strings) {
            map.put(mFrom[i], string);
            i++;
        }

        data.add(map);
    }

    /**
     * Automatically converts resource IDs to strings.
     *
     * @see #add(String...)
     * @param resIds
     *            resource IDs
     */
    public void add(int... resIds) {
        String[] strings = new String[resIds.length];

        for (int i = 0; i < resIds.length; i++) {
            strings[i] = mContext.getString(resIds[i]);
        }

        add(strings);
    }

    /**
     * Convenience method. Often times, the first argument is a resource ID,
     * while the others are strings.
     *
     * @param resId
     *            a resource ID
     * @param strings
     *            more strings
     */
    public void add(int resId, String... strings) {
        String[] newStrings = new String[strings.length + 1];
        newStrings[0] = mContext.getString(resId);

        System.arraycopy(strings, 0, newStrings, 1, strings.length);

        add(newStrings);
    }

    /**
     * Clear the List.
     */
    public void clear() {
        data.clear();
    }

    /**
     * Notify the adapter of the changes.
     */
    public void commit() {
        adapter.notifyDataSetChanged();
    }

    /**
     * Returns the data associated with the given position.
     *
     * @see AdapterView.OnItemClickListener#onItemClick
     * @param position
     *              Position of the item whose data we want within the adapter's data set.
     * @return the data at the specified position.
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getItem(int position) {
        return (Map<String, String>) adapter.getItem(position);
    }
}

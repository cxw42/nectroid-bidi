// This file is part of Nectroid.
//
// Nectroid is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Nectroid is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Nectroid.  If not, see <http://www.gnu.org/licenses/>.

package com.kvance.Nectroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.util.Log;


class Cache
{
    public enum DocId {
        QUEUE,
        STREAMS,
        ONELINER,
    }

    private static final String TAG = "NectroidCache";


    /** Return the data from this document's cache or null if not cached */
    public static String read(DocId id, Context ctx)
    {
        String result = null;
        File cacheDir = ctx.getCacheDir();
        File fullPath = new File(cacheDir, getFilenameForDocId(id, ctx));
        try {
            FileInputStream fis = new FileInputStream(fullPath);
            byte[] data = new byte[(int)fullPath.length()];
            fis.read(data);
            result = new String(data);
        } catch(IOException e) {
            // result is already set to null, so we will return null
        }
        return result;
    }


    /** Write data for this document to the cache */
    public static boolean write(DocId id, byte[] data, Context ctx)
    {
        boolean success = true;
        File cacheDir = ctx.getCacheDir();
        File fullPath = new File(cacheDir, getFilenameForDocId(id, ctx));
        try {
            FileOutputStream fos = new FileOutputStream(fullPath);
            fos.write(data);
        } catch(IOException e) {
            success = false;
        }
        return success;
    }

    public static boolean write(DocId id, String data, Context ctx)
    {
        return write(id, data.getBytes(), ctx);
    }


    /** Return true if a cached version of this doc is available */
    public static boolean available(DocId id, Context ctx)
    {
        String result = null;
        File cacheDir = ctx.getCacheDir();
        File fullPath = new File(cacheDir, getFilenameForDocId(id, ctx));
        return fullPath.canRead();
    }


    /** Return the URL for this document */
    public static URL getUrlForDocId(DocId id, Context ctx)
    {
        URL result = null;
        String urlString;

        switch(id) {
            case QUEUE:
                urlString = ctx.getString(R.string.url_queue);
                break;

            case STREAMS:
                urlString = ctx.getString(R.string.url_streams);
                break;

            case ONELINER:
                urlString = ctx.getString(R.string.url_oneliner);
                break;

            default:
                urlString = null;
                Log.e(TAG, String.format("No URL for DocId %d", id.ordinal()));
                break;
        }

        if(urlString != null) {
            try {
                result = new URL(urlString);
            } catch(MalformedURLException e) {
                Log.e(TAG, String.format("Malformed URL \"%s\" for DocId %d", urlString,
                            id.ordinal()));
            }
        }

        return result;
    }


    /** Return the local path of the cached file for this document */
    public static String getFilenameForDocId(DocId id, Context ctx)
    {
        switch(id) {
            case QUEUE:
                return ctx.getString(R.string.filename_queue);

            case STREAMS:
                return ctx.getString(R.string.filename_streams);

            case ONELINER:
                return ctx.getString(R.string.filename_oneliner);
        }
        Log.e(TAG, String.format("No path for DocId %d", id.ordinal()));
        return null;
    }
}

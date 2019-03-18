package pro.axonomy.www;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Used to store the {@link AsyncTask} with key linked to the img url
 * When jumped to other views, will automatically cancel all current AsyncTask
 * to avoid potential stuck or significantly slow down of the app
 */
public class WebImageHandler {

    public static HashMap<String, AsyncTask> UNFINISHED_ASYNC_TASKS = new HashMap<>();

    // Served as local cache for all image/logo bitmap
    public static HashMap<String, Bitmap> WEB_IMAGE_MAP = new HashMap<>();

    public static void clearUnfinishedAsyncTaskList() {
        Log.i("ClearAsyncTask", "Clearing Unfinished AsyncTasks...");
        while (!UNFINISHED_ASYNC_TASKS.isEmpty()) {
            Iterator it = UNFINISHED_ASYNC_TASKS.entrySet().iterator();
            Map.Entry pair = (Map.Entry)it.next();
            AsyncTask task = (AsyncTask) pair.getValue();
            task.cancel(true);
            UNFINISHED_ASYNC_TASKS.remove(pair.getKey());
        }
        Log.i("ClearAsyncTask", "Clearance finished.");
    }
}

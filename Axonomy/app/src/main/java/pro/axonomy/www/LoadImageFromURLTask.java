package pro.axonomy.www;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import pro.axonomy.www.project.ProjectFragment;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class LoadImageFromURLTask extends AsyncTask<String, Void, Bitmap> {

    private static final int LOAD_IMAGE_TIME_OUT = 3000;

    private ProjectFragment fragment;
    private ImageView imageView;
    private String id;

    public LoadImageFromURLTask(ImageView imageView, String id, ProjectFragment fragment) {
        this.imageView = imageView;
        this.id = id;
        this.fragment = fragment;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.d("LoadImageTask", "doInBackground " + id);
        Bitmap result = null;
        try {
            Bitmap cache = fragment.retrieveBitmapForURL(params[0]);
            if (cache != null) {
                result = cache;
                Log.i("LoadImageTask", "Cache hit for URL: " + params[0]);
            } else {
                URL url = new URL(params[0]);
                URLConnection conn = url.openConnection();
                conn.setUseCaches(true);
                conn.setConnectTimeout(LOAD_IMAGE_TIME_OUT);
                conn.setReadTimeout(LOAD_IMAGE_TIME_OUT);
                if (conn.getContent() != null) {
                    result = BitmapFactory.decodeStream((InputStream) conn.getContent());
                    fragment.addCacheForProjectImage(params[0], result);
                    Log.i("LoadImageTask", "SUCCEED in loading image from URL: " + params[0]);
                } else {
                    Log.i("LoadImageTask", "NULL content for content with URL: " + params[0]);
                }
            }
        } catch (SocketTimeoutException e) {
            Log.i("LoadImageTask", "TIMEOUT in loading image from URL: " + params[0]);
            fragment.removeFromUnfinishedAsyncTaskList(this.id);
        } catch (IOException e) {
            e.printStackTrace();
            fragment.removeFromUnfinishedAsyncTaskList(this.id);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.d("LoadImageTask", "onPostExecute " + id);
        if (result != null) {
            imageView.setImageBitmap(result);
        }
        fragment.removeFromUnfinishedAsyncTaskList(this.id);
    }
}

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

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class LoadImageFromURLTask extends AsyncTask<String, Void, Bitmap> {

    private static final int LOAD_IMAGE_TIME_OUT = 3000;

    private ImageView imageView;
    private String id;

    public LoadImageFromURLTask(ImageView imageView, String id) {
        this.imageView = imageView;
        this.id = id;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Log.d("LoadImageTask", "doInBackground " + id);
        Bitmap result = null;

        try {
            Bitmap cache = WebImageHandler.WEB_IMAGE_MAP.get(params[0]);
            if (cache != null) {
                result = cache;
                Log.i("LoadImageTask", "Cache hit for URL: " + params[0]);
            } else {
                if (isCancelled()) {
                    Log.d("LoadImageTask", "isCancelled");
                    return null;
                }
                URL url = new URL(params[0]);
                if (isCancelled()) {
                    Log.d("LoadImageTask", "isCancelled");
                    return null;
                }
                URLConnection conn = url.openConnection();
                if (isCancelled()) {
                    Log.d("LoadImageTask", "isCancelled");
                    return null;
                }
                conn.setUseCaches(true);
                if (isCancelled()) {
                    Log.d("LoadImageTask", "isCancelled");
                    return null;
                }
                conn.setConnectTimeout(LOAD_IMAGE_TIME_OUT);
                if (isCancelled()) {
                    Log.d("LoadImageTask", "isCancelled");
                    return null;
                }
                conn.setReadTimeout(LOAD_IMAGE_TIME_OUT);
                if (isCancelled()) {
                    Log.d("LoadImageTask", "isCancelled");
                    return null;
                }
                while (conn.getContent() == null) {
                    if (isCancelled()) {
                        Log.d("LoadImageTask", "isCancelled");
                        return null;
                    }
                }
                if (conn.getContent() != null) {
                    if (isCancelled()) {
                        Log.d("LoadImageTask", "isCancelled");
                        return null;
                    }
                    result = BitmapFactory.decodeStream((InputStream) conn.getContent());
                    if (isCancelled()) {
                        Log.d("LoadImageTask", "isCancelled");
                        return null;
                    }
                    WebImageHandler.WEB_IMAGE_MAP.put(params[0], result);
                    Log.i("LoadImageTask", "SUCCEED in loading image from URL: " + params[0]);
                } else {
                    Log.i("LoadImageTask", "NULL content for content with URL: " + params[0]);
                }
            }
        } catch (SocketTimeoutException e) {
            Log.i("LoadImageTask", "TIMEOUT in loading image from URL: " + params[0]);
            WebImageHandler.UNFINISHED_ASYNC_TASKS.remove(this.id);
        } catch (IOException e) {
            e.printStackTrace();
            WebImageHandler.UNFINISHED_ASYNC_TASKS.remove(this.id);
        }
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.d("LoadImageTask", "onPostExecute " + id);
        if (result != null) {
            imageView.setImageBitmap(result);
        }
        WebImageHandler.UNFINISHED_ASYNC_TASKS.remove(this.id);
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public String getId() {
        return this.id;
    }
}

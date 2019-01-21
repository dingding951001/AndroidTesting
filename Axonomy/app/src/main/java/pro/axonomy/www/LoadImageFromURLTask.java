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
import java.net.URL;

import pro.axonomy.www.project.ProjectFragment;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class LoadImageFromURLTask extends AsyncTask<String, Void, Bitmap> {

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
        Bitmap result = null;
        try {
            result = BitmapFactory.decodeStream((InputStream)new URL(params[0]).getContent());
            Log.i("LoadImageTask", "SUCCEED in loading image from URL: " + params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        fragment.removeFromUnfinishedAsyncTaskList(this.id);
    }
}

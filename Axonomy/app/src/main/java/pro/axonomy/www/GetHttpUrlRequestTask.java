package pro.axonomy.www;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class GetHttpUrlRequestTask extends AsyncTask<String, String, String> implements Callable {

    private Context context;
    public View parentView = null;
    public LayoutInflater inflater = null;
    public ProgressBar progressBar;

    public GetHttpUrlRequestTask(Context context, View view, LayoutInflater inflater) {
        this.context = context;
        this.parentView = view;
        this.inflater = inflater;
    }

    public GetHttpUrlRequestTask(Context context, View view) {
        this.context = context;
        this.parentView = view;
    }

    public GetHttpUrlRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... param) {
        StringBuilder sb = new StringBuilder();
        try {
            validateURL(param);

            final URL url = new URL(param[0]);
            Log.i("GetHttpUrlRequestTask", "Performing GET request to Url: " + url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (!TextUtils.isEmpty(UserInfo.getAuthorization(context))) {
                urlConnection.setRequestProperty("authorization", UserInfo.getAuthorization(context));
            }
            urlConnection.setRequestProperty("lang", "en");

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            bufferedReader.close();
            Log.i("GetHttpUrlRequestTask", "succeed in calling API with response: " + sb.toString());
        } catch (Exception e) {
            Log.i("GetHttpUrlRequestTask", "failed in calling API with response" + sb.toString());
        }

        return sb.toString();
    }

    private void validateURL(String... param) {
        if (param == null || param.length == 0) {
            Log.e("GetHttpUrlTask", "EMPTY url for request.");
            throw new RuntimeException("Empty url, cannot perform GET request.");
        }
    }

    @Override
    public Object call() {
        return this.execute();
    }
}

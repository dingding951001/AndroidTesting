package pro.axonomy.www;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetHttpUrlRequestTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... param) {
        StringBuilder sb = new StringBuilder();
        try {
            validateURL(param);

            final URL url = new URL(param[0]);
            Log.i("GetHttpUrlRequestTask", "Performing GET request to Url: " + url);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
}

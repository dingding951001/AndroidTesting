package pro.axonomy.www.vote;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class GetVoteTask extends AsyncTask<String, String, String> {

    private static final String VOTE_URL_LOGOUT = "https://wx.aceport.com/api/v1/integration/voting/rounds";

    private Context context;

    public GetVoteTask(Context context) {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... strings) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(VOTE_URL_LOGOUT);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            bufferedReader.close();
            Log.i("GetVoteTask", "succeed in retrieving vote page without login: " + sb.toString());
        } catch (Exception e) {
            Log.i("GetVoteTask", "failed in retrieving vote page without login" + sb.toString());
        }

        return null;
    }
}

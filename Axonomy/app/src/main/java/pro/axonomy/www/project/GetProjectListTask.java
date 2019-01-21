package pro.axonomy.www.project;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import pro.axonomy.www.PostHttpsRequestTask;
import pro.axonomy.www.login.LogInTask;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class GetProjectListTask extends AsyncTask<String, Void, String> {

    private static final String PROJECT_URL = "https://wx.aceport.com/public/project/items";
    private static final String MESSAGE = "MESSAGE";
    private static final String SUCCESS = "OK";

    @Override
    protected String doInBackground(String... params) {
        SSLContext sc;
        HttpsURLConnection connection = null;

        // set up SSLContext and HttpsURLConnection
        try {
            URL url = new URL(PROJECT_URL);

            connection = (HttpsURLConnection) url.openConnection();
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());

            // set Timeout and method
            connection.setReadTimeout(LogInTask.LOGIN_TIMEOUT);
            connection.setConnectTimeout(LogInTask.LOGIN_TIMEOUT);
            connection.setRequestMethod(LogInTask.POST);
            connection.setDoInput(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // send the request body to Https
        String requestBody = params[0];
        PostHttpsRequestTask.sendPostRequestBodyToHttpsConnection(connection, requestBody);

        // validate the username and password for login
        String result = null;
        JSONObject response = null;
        final int status;
        try {
            status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    result = LogInTask.extractResponse(connection);
                    Log.i("GetProjectListTask", result);
            }

            if (result != null) {
                response = new JSONObject(result);
            }
            if (response != null && (response.get(MESSAGE).equals(SUCCESS))) {
                Log.i("GetProjectListTask","SUCCEED in retrieving project lists with request: " + requestBody +
                        " and response: " + response.toString());
            } else if (!response.get(MESSAGE).equals(SUCCESS)) {
                Log.i("GetProjectListTask","FAILED in retrieving project lists with request: " + requestBody +
                        " and response: " + response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}

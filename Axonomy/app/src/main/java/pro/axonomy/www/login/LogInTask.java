package pro.axonomy.www.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import pro.axonomy.www.PostHttpsRequestTask;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class LogInTask extends AsyncTask<String, String, String> {

    public static final String LOGIN_URL = "https://wx.aceport.com/public/user/login";
    public static final int LOGIN_TIMEOUT = 7000;
    public static final String POST = "POST";
    public static final String LOGIN_SUCCEED = "登录成功";
    public static final String MESSAGE = "message";

    @Override
    protected String doInBackground(String... params) {

        SSLContext sc;
        HttpsURLConnection connection = null;

        // set up SSLContext and HttpsURLConnection
        try {
            URL url = new URL(LOGIN_URL);
            connection = (HttpsURLConnection) url.openConnection();
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());


            // set Timeout and method
            connection.setReadTimeout(LOGIN_TIMEOUT);
            connection.setConnectTimeout(LOGIN_TIMEOUT);
            connection.setRequestMethod(POST);
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
        final int status;
        try {
            status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    result = extractResponse(connection);
                    Log.i("response", result);
            }

            JSONObject response = null;
            if (result != null) {
                response = new JSONObject(result);
            }
            if (response != null && response.get(MESSAGE).equals(LOGIN_SUCCEED)) {
                Log.i("login","SUCCEED!!!!!");
            } else if (!response.get(MESSAGE).equals(LOGIN_SUCCEED)) {
                Log.i("loginError", "not correct pwd or username");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String extractResponse(HttpsURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        bufferedReader.close();
        //return received string
        return sb.toString();
    }
}

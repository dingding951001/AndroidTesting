package pro.axonomy.www;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import pro.axonomy.www.login.LogInTask;

import static pro.axonomy.www.login.LogInTask.LOGIN_TIMEOUT;
import static pro.axonomy.www.login.LogInTask.POST;

public class GeneralPostHttpRequestTask extends AsyncTask<String, String, String> {

    private Context context;
    public View parentView = null;
    public LayoutInflater inflater = null;

    public GeneralPostHttpRequestTask(Context context, View view, LayoutInflater inflater) {
        this.context = context;
        this.parentView = view;
        this.inflater = inflater;
    }

    public GeneralPostHttpRequestTask(Context context, View view) {
        this.context = context;
        this.parentView = view;
    }

    public GeneralPostHttpRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        SSLContext sc;
        HttpsURLConnection connection = null;

        try {
            validateURL(params);
            final URL url = new URL(params[0]);

            connection = (HttpsURLConnection) url.openConnection();
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());

            // set Timeout and method
            connection.setReadTimeout(LOGIN_TIMEOUT);
            connection.setConnectTimeout(LOGIN_TIMEOUT);
            connection.setRequestMethod(POST);
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
        String requestBody = null;
        if (params.length > 1) {
            requestBody = params[1];
        }
        PostHttpsRequestTask.sendPostRequestBodyToHttpsConnection(connection, requestBody, context);

        String result = null;
        final int status;
        try {
            status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    result = LogInTask.extractResponse(connection);
                    Log.i("response", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @SuppressLint("LongLogTag")
    private void validateURL(String... param) {
        if (param == null || param.length == 0) {
            Log.e("GeneralPostHttpRequestTask", "EMPTY url for request.");
            throw new RuntimeException("Empty url, cannot perform GET request.");
        }
    }
}

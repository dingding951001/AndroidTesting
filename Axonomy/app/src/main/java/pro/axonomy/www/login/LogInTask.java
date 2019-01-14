package pro.axonomy.www.login;

import android.content.Context;
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

import pro.axonomy.www.ButtomNavigationActivity;
import pro.axonomy.www.PostHttpsRequestTask;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class LogInTask extends AsyncTask<String, String, String> {

    public static final String LOGIN_TARGET = "target";
    public static final String LOGIN_PASSWORD = "password";
    public static final String VERIFICATION_CODE = "verification_code";
    public static final String FINGERPRINT = "fp";
    public static final String AREA_CODE = "area_code";
    public static final String REGISTRATION_FLAG = "registered";
    public static final String INVITATION_CODE = "invitation_code";
    public static final String LOGIN_SMS_TYPE = "type";

    public static final String LOGIN_URL = "https://wx.aceport.com/public/user/login";
    public static final String REGISTER_URL = "https://wx.aceport.com/public/user/newregister";
    public static final int LOGIN_TIMEOUT = 7000;
    public static final String POST = "POST";
    public static final String SMS_SUCCEED = "登陆成功";
    public static final String LOGIN_SUCCEED = "登录成功";
    public static final String MESSAGE = "message";

    private static final String REGISTER_FLAG = "1";
    private static final String LOGIN_FLAG = "0";

    private Context context;

    public LogInTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        SSLContext sc;
        HttpsURLConnection connection = null;

        // set up SSLContext and HttpsURLConnection
        try {
            URL url = null;
            if (params[1].equals(LOGIN_FLAG)) {
                url = new URL(LOGIN_URL);
            } else if (params[1].equals(REGISTER_FLAG)) {
                url = new URL(REGISTER_URL);
            }
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
            if (response != null && (response.get(MESSAGE).equals(SMS_SUCCEED) || response.get(MESSAGE).equals(LOGIN_SUCCEED))) {
                Log.i("loginActivity","SUCCEED in login with request: " + requestBody);
                startNavigationActivity();
            } else if (!response.get(MESSAGE).equals(LOGIN_SUCCEED)) {
                Log.i("loginActivity","FAILED in login with request: " + requestBody +
                " and response: " + response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void startNavigationActivity() {
        Intent navigationIntent = new Intent(context, ButtomNavigationActivity.class);
        context.startActivity(navigationIntent);
    }


    public static String extractResponse(HttpsURLConnection connection) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        bufferedReader.close();
        return sb.toString();
    }
}

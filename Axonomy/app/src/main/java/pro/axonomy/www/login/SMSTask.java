package pro.axonomy.www.login;

import android.os.AsyncTask;
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

public class SMSTask extends AsyncTask<String, String, String> {

    private EmailLoginActivity emailActivity = null;
    private MobileLoginActivity mobileActivity = null;

    private static final String SMS_URL = "https://wx.aceport.com/public/sms/sendcode2";
    private static final int SMS_TIMEOUT = 8000;
    private static final String POST = "POST";
    private static final String SEND_SUCCEED = "success";
    private static final String MESSAGE = "message";
    private static final String DATA = "data";

    public SMSTask(EmailLoginActivity activity) {
        this.emailActivity = activity;
    }

    public SMSTask(MobileLoginActivity activity) {
        this.mobileActivity = activity;
    }

    @Override
    protected String doInBackground(String... param) {
        SSLContext sc;
        HttpsURLConnection connection = null;

        // set up SSLContext and HttpsURLConnection
        try {
            URL url = new URL(SMS_URL);
            connection = (HttpsURLConnection) url.openConnection();
            sc = SSLContext.getInstance("TLS");
            sc.init(null, null, new java.security.SecureRandom());
            connection.setSSLSocketFactory(sc.getSocketFactory());


            // set Timeout and method
            connection.setReadTimeout(SMS_TIMEOUT);
            connection.setConnectTimeout(SMS_TIMEOUT);
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
        String requestBody = param[0];
        PostHttpsRequestTask.sendPostRequestBodyToHttpsConnection(connection, requestBody);

        // validate the username and password for login
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

            JSONObject response = null;
            if (result != null) {
                response = new JSONObject(result);
            }
            if (response != null && response.get(MESSAGE).equals(SEND_SUCCEED)) {
                String fingerprint = extractFingerPrint(response);
                Log.i("sendSMS", "Received the Fingerprint as: " + fingerprint);
                setFingerPrintBackToActivity(fingerprint);
                Log.i("sendSMS","SUCCEED sending with request: " + requestBody);
            } else if (!response.get(MESSAGE).equals(SEND_SUCCEED)) {
                Log.i("sendSMS","FAILED sending with request: " + requestBody);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String extractFingerPrint(JSONObject response) throws JSONException {
        if (response != null && response.get(DATA) != null) {
            return ((JSONObject)response.get(DATA)).get(LogInTask.FINGERPRINT).toString();
        }
        return null;
    }

    private void setFingerPrintBackToActivity(String fingerprint) {
        if (emailActivity != null) {
            emailActivity.setFp(fingerprint);
        } else if (mobileActivity != null ) {
            mobileActivity.setFp(fingerprint);
        }
    }
}

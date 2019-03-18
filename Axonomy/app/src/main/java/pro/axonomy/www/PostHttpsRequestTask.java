package pro.axonomy.www;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

public class PostHttpsRequestTask {

    public static void sendPostRequestBodyToHttpsConnection(HttpsURLConnection connection, String requestBody, Context context)  {

        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");
        if (!TextUtils.isEmpty(UserInfo.getAuthorization(context))) {
            connection.setRequestProperty("authorization", UserInfo.getAuthorization(context));
        }

        if (!TextUtils.isEmpty(requestBody)) {
            try {
                connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
                connection.setDoOutput(true);
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes("UTF-8"));
                outputStream.close();
                connection.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

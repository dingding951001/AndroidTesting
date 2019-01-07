package pro.axonomy.www;

import java.io.IOException;
import java.io.OutputStream;

import javax.net.ssl.HttpsURLConnection;

public class PostHttpsRequestTask {

    public static void sendPostRequestBodyToHttpsConnection(HttpsURLConnection connection, String requestBody)  {

        connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream outputStream= null;

        try {
            outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes("UTF-8"));
            outputStream.close();
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

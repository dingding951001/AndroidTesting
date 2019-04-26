package pro.axonomy.www.kyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import pro.axonomy.www.GeneralPostHttpRequestTask;
import pro.axonomy.www.R;
import pro.axonomy.www.WebViewActivity;

public class KYCSpeed extends AppCompatActivity {

    private static String GET_BIZ_TOKEN_URL = "https://wx.aceport.com/api/v1/faceid/get_biz_token";
    private static String OPENAPI_FACEID_PREFIX = "https://openapi.faceid.com/lite/v1/do/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kyc_speed_step1);
    }

    public void speedVerify2(View view) {
        final GeneralPostHttpRequestTask getBizTokenTask = new GeneralPostHttpRequestTask(this, view) {
            @Override
            public void onPostExecute(String result) {
                Log.i("getBizTokenTask", result);
                try {
                    JSONObject bizTokenInfo = new JSONObject(result);
                    String biz_token = bizTokenInfo.getJSONObject("data").getString("biz_token");
                    String url = OPENAPI_FACEID_PREFIX + biz_token;
                    Intent kycSpeedIntent = new Intent(getApplicationContext(), WebViewActivity.class);
                    kycSpeedIntent.putExtra(WebViewActivity.URL_PARAM, url);
                    startActivity(kycSpeedIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        getBizTokenTask.execute(GET_BIZ_TOKEN_URL);
    }

}

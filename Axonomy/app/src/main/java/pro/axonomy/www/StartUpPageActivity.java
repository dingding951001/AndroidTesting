package pro.axonomy.www;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class StartUpPageActivity extends Activity {

    static final String TRANSLATION_URL = "https://wx.aceport.xyz/api/tag/i18ngen";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        AsyncTask<String, String, String> TranslationTask = new GetHttpUrlRequestTask(getBaseContext()) {
            @Override
            protected void onPostExecute(String result) {
                UserInfo.setTranslation(getBaseContext(), result);
                Intent bottomNavigationActivity = new Intent(getBaseContext(), BottomNavigationActivity.class);
                startActivity(bottomNavigationActivity);
            }
        };
        TranslationTask.execute(TRANSLATION_URL);
    }

}

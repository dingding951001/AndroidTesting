package pro.axonomy.www.me.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pro.axonomy.www.R;
import pro.axonomy.www.UserInfo;
import pro.axonomy.www.login.MobileLoginActivity;

/**
 * Created by xingyuanding on 4/4/19.
 */

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
    }

    public void changeLanguage(View view) {
        Intent languageIntent = new Intent(getApplicationContext(), LanguageActivity.class);
        startActivity(languageIntent);
    }

    public void logout(View view) {
        UserInfo.setToken(getApplicationContext(), null);
        Intent loginActivity = new Intent(getApplicationContext(), MobileLoginActivity.class);
        startActivity(loginActivity);
    }

}

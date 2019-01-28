package pro.axonomy.www;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import pro.axonomy.www.login.MobileLoginActivity;

public class StartUpPageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Handler handler = new Handler();

        if (UserInfo.getUserName(this).length() == 0) {
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                    Intent mobileLoginActivity = new Intent(getBaseContext(), MobileLoginActivity.class);
                    startActivity(mobileLoginActivity);
                }
            }, 2000);
        } else {
            Log.i("StartUp", "Already logged in with username: " + UserInfo.getUserName(this));
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                    Intent mobileLoginActivity = new Intent(getBaseContext(), ButtomNavigationActivity.class);
                    startActivity(mobileLoginActivity);
                }
            }, 2000);
        }
    }

}

package pro.axonomy.www;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import pro.axonomy.www.login.MobileLoginActivity;

/**
 * Created by xingyuanding on 1/9/19.
 */
public class StartUpPageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
                Intent mobileLoginActivity = new Intent(getBaseContext(), ButtomNavigationActivity.class);
                startActivity(mobileLoginActivity);
            }
        }, 2000);
    }

}

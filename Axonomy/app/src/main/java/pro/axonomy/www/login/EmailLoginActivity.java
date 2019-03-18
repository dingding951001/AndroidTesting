package pro.axonomy.www.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import pro.axonomy.www.BottomNavigationActivity;
import pro.axonomy.www.GetHttpUrlRequestTask;
import pro.axonomy.www.R;

public class EmailLoginActivity extends Activity {

    private String fp;
    private int registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_login_email);

        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.email_sms);
        smsLayout.setVisibility(View.GONE);

        final TextView userAgreementTextView = findViewById(R.id.user_agreement_en_text_view);
        userAgreementTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void switchToMobileLogin(View view) {
        Intent mobilePasswordLoginIntent = new Intent(this, MobileLoginActivity.class);
        this.startActivity(mobilePasswordLoginIntent);
    }

    public void switchPasswordAndSMS(View view) {
        final TextView emailAuthorizationMethod = (TextView) findViewById(R.id.emailAuthorizationMethod);
        final EditText pwdText = (EditText) findViewById(R.id.emailPassword);
        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.email_sms);
        if (emailAuthorizationMethod.getText().toString().equals("SMS Code")) {
            pwdText.setVisibility(View.GONE);
            smsLayout.setVisibility(View.VISIBLE);
            emailAuthorizationMethod.setText("Password Login");
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)emailAuthorizationMethod.getLayoutParams();
            params.setMargins(410, 0, 0, 0);
            emailAuthorizationMethod.setLayoutParams(params);
        } else {
            pwdText.setVisibility(View.VISIBLE);
            smsLayout.setVisibility(View.GONE);
            emailAuthorizationMethod.setText("SMS Code");
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)emailAuthorizationMethod.getLayoutParams();
            params.setMargins(480, 0, 0, 0);
            emailAuthorizationMethod.setLayoutParams(params);
        }
    }

    public void sendSMSToEmail(View view) throws JSONException {
        final EditText sendCodeText = (EditText) findViewById(R.id.sendCodeToEmail);
        final EditText emailText = (EditText) findViewById(R.id.emailAddress);
        final String emailAddress = emailText.getText().toString();

        if (sendCodeText.getText().toString().equals("Send Code") && !TextUtils.isEmpty(emailAddress)) {
            final JSONObject requestBody = new JSONObject() {{
                put(LogInTask.LOGIN_SMS_TYPE, 1);
                put(LogInTask.LOGIN_TARGET, emailAddress);
            }};

            new SMSTask(this).execute(requestBody.toString());

            new CountDownTimer(180000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    sendCodeText.setText(millisUntilFinished / 1000 + " s");
                }

                @Override
                public void onFinish() {
                    sendCodeText.setText("Send Code");
                }
            }.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void login(View view) throws JSONException, ExecutionException, InterruptedException {
        final EditText emailText = (EditText) findViewById(R.id.emailAddress);
        final String emailAddress = emailText.getText().toString();

        final EditText pwdText = (EditText) findViewById(R.id.emailPassword);
        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.email_sms);

        if (pwdText.getVisibility() == View.VISIBLE) {
            final String pwd = pwdText.getText().toString();

            final JSONObject requestBody = new JSONObject() {{
                put(LogInTask.LOGIN_TARGET, emailAddress);
                put(LogInTask.LOGIN_PASSWORD, pwd);
            }};

            new LogInTask(this).execute(requestBody.toString(), "0");

        } else if (smsLayout.getVisibility() == View.VISIBLE) {
            final EditText verificationCodeText = (EditText) findViewById(R.id.email_verification);
            final String verificationCode = verificationCodeText.getText().toString();

            final JSONObject requestBody = new JSONObject() {{
                put(LogInTask.VERIFICATION_CODE, verificationCode);
                put(LogInTask.FINGERPRINT, fp);
                put(LogInTask.REGISTRATION_FLAG, registered);
            }};

            String response = new LogInTask(this).execute(requestBody.toString(), "1").get();

            if (response.equals(LogInTask.CALLBACK_SUCCEED)) {
                Intent bottomIntent = new Intent(this, BottomNavigationActivity.class);
                startActivity(bottomIntent);
            }
        }
    }

    public void setFpAndRegistered(String fingerprint, int registered) {
        this.fp = fingerprint;
        this.registered = registered;
    }
}

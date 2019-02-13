package pro.axonomy.www.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import pro.axonomy.www.BottomNavigationActivity;
import pro.axonomy.www.R;

public class MobileLoginActivity extends Activity {

    private String fp;
    private int registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_login_mobile);
        Spinner spinner = (Spinner) findViewById(R.id.country_code_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_code, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                ((TextView) view).setText(selectedItem.replaceAll("\\D+",""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.mobile_sms);
        smsLayout.setVisibility(View.GONE);

        final TextView userAgreementTextView = findViewById(R.id.user_agreement_en_text_view);
        userAgreementTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void switchToEmailLogin(View view) {
        Intent emailPasswordLoginIntent = new Intent(this, EmailLoginActivity.class);
        this.startActivity(emailPasswordLoginIntent);
    }

    public void switchPasswordAndSMS(View view) {
        final TextView mobileAuthorizationMethod = (TextView) findViewById(R.id.mobileAuthorizationMethod);
        final EditText pwdText = (EditText) findViewById(R.id.mobilePassword);
        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.mobile_sms);
        if (mobileAuthorizationMethod.getText().toString().equals("SMS Code")) {
            pwdText.setVisibility(View.GONE);
            smsLayout.setVisibility(View.VISIBLE);
            mobileAuthorizationMethod.setText("Password Login");
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mobileAuthorizationMethod.getLayoutParams();
            params.setMargins(410, 0, 0, 0);
            mobileAuthorizationMethod.setLayoutParams(params);
        } else {
            pwdText.setVisibility(View.VISIBLE);
            smsLayout.setVisibility(View.GONE);
            mobileAuthorizationMethod.setText("SMS Code");
            final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mobileAuthorizationMethod.getLayoutParams();
            params.setMargins(480, 0, 0, 0);
            mobileAuthorizationMethod.setLayoutParams(params);
        }
    }

    public void sendSMSToPhone(View view) throws JSONException {
        final EditText sendCodeText = (EditText) findViewById(R.id.sendCodeToPhone);
        final EditText mobileNumberText = (EditText) findViewById(R.id.mobileNumber);
        final String mobileNumber = mobileNumberText.getText().toString();
        if (sendCodeText.getText().toString().equals("Send Code") && !TextUtils.isEmpty(mobileNumber)) {
            final Spinner areaCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
            final String areaCode = areaCodeSpinner.getSelectedItem().toString().replaceAll("\\D+", "");

            final JSONObject requestBody = new JSONObject() {{
                put(LogInTask.LOGIN_SMS_TYPE, 0);
                put(LogInTask.LOGIN_TARGET, mobileNumber);
                put(LogInTask.AREA_CODE, areaCode);
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
        final EditText mobileNumberText = (EditText) findViewById(R.id.mobileNumber);
        final String mobileNumber = mobileNumberText.getText().toString();
        final Spinner areaCodeSpinner = (Spinner) findViewById(R.id.country_code_spinner);
        final String areadCode = areaCodeSpinner.getSelectedItem().toString().replaceAll("\\D+","");
        Log.i("MobileActivity" ,"Extracted Country Code: " + areadCode);

        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.mobile_sms);
        final EditText pwdText = (EditText) findViewById(R.id.mobilePassword);

        if (pwdText.getVisibility() == View.VISIBLE) {
            final String pwd = pwdText.getText().toString();

            final JSONObject requestBody = new JSONObject() {{
                put(LogInTask.LOGIN_TARGET, mobileNumber);
                put(LogInTask.LOGIN_PASSWORD, pwd);
            }};

            new LogInTask(this).execute(requestBody.toString(), "0");

        } else if (smsLayout.getVisibility() == View.VISIBLE) {
            final EditText verificationCodeText = (EditText) findViewById(R.id.phone_verification);
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

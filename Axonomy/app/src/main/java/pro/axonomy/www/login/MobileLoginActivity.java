package pro.axonomy.www.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import pro.axonomy.www.R;

public class MobileLoginActivity extends Activity {

    public static final String LOGIN_TARGET = "target";
    public static final String LOGIN_PASSWORD = "password";

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

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void login(View view) throws JSONException {
        final EditText mobileNumberText = (EditText) findViewById(R.id.mobileNumber);
        final String mobileNumber = mobileNumberText.getText().toString();
        final EditText pwdText = (EditText) findViewById(R.id.mobile_pwd);
        final String pwd = pwdText.getText().toString();

        final JSONObject requestBody = new JSONObject() {{
            put(LOGIN_TARGET, mobileNumber);
            put(LOGIN_PASSWORD, pwd);
        }};

        new LogInTask().execute(requestBody.toString());
    }

    public void emailLogin(View view) {
        Intent emailPasswordLoginIntent = new Intent(this, EmailLoginActivity.class);
        this.startActivity(emailPasswordLoginIntent);
    }

    public void switchPasswordAndSMS(View view) {
        final TextView mobileAuthorizationMethod = (TextView) findViewById(R.id.mobileAuthorizationMethod);
        final EditText pwdText = (EditText) findViewById(R.id.mobile_pwd);
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
}

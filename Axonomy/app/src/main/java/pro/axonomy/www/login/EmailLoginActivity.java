package pro.axonomy.www.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import pro.axonomy.www.R;

public class EmailLoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.layout_login_email);

        final LinearLayout smsLayout = (LinearLayout) findViewById(R.id.email_sms);
        smsLayout.setVisibility(View.GONE);

        final TextView userAgreementTextView = findViewById(R.id.user_agreement_en_text_view);
        userAgreementTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void mobileLogin(View view) {
        Intent mobilePasswordLoginIntent = new Intent(this, MobileLoginActivity.class);
        this.startActivity(mobilePasswordLoginIntent);
    }

    public void switchPasswordAndSMS(View view) {
        final TextView emailAuthorizationMethod = (TextView) findViewById(R.id.emailAuthorizationMethod);
        final EditText pwdText = (EditText) findViewById(R.id.email_pwd);
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
}

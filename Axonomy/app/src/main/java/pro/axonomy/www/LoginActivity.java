package pro.axonomy.www;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {

    public static final String LOGIN_TARGET = "target";
    public static final String LOGIN_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        Spinner spinner = (Spinner) findViewById(R.id.country_code_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_code, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final TextView userAgreementTextView = findViewById(R.id.user_agreement_en_text_view);
        userAgreementTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void login(View view) throws JSONException {
        final EditText mobileNumberText = (EditText) findViewById(R.id.mobileNumber);
        final String mobileNumber = mobileNumberText.getText().toString();
        final EditText pwdText = (EditText) findViewById(R.id.pwd);
        final String pwd = pwdText.getText().toString();

        final JSONObject requestBody = new JSONObject() {{
            put(LOGIN_TARGET, mobileNumber);
            put(LOGIN_PASSWORD, pwd);
        }};

        new LogInTask().execute(requestBody.toString());
    }
}

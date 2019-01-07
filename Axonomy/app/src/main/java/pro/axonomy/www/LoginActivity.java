package pro.axonomy.www;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

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

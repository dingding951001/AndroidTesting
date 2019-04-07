package pro.axonomy.www.me.setting;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import pro.axonomy.www.R;
import pro.axonomy.www.UserInfo;

/**
 * Created by xingyuanding on 4/7/19.
 */

public class LanguageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_language);

        String language = UserInfo.getLanguage(getApplicationContext());
        Log.i("LanguageActivity", "user language preference is " + language);

        if (language.equals("zh")) {
            ImageView selectEnglish = findViewById(R.id.select_english);
            selectEnglish.setVisibility(View.INVISIBLE);
        } else {
            ImageView selectChinese = findViewById(R.id.select_chinese);
            selectChinese.setVisibility(View.INVISIBLE);
        }
    }

    public void changeToChineseMode(View view) {
        ImageView selectChinese = findViewById(R.id.select_chinese);
        selectChinese.setVisibility(View.VISIBLE);
        ImageView selectEnglish = findViewById(R.id.select_english);
        selectEnglish.setVisibility(View.INVISIBLE);
        UserInfo.setLanguage(getApplicationContext(), "zh");
    }

    public void changeToEnglishMode(View view) {
        ImageView selectChinese = findViewById(R.id.select_chinese);
        selectChinese.setVisibility(View.INVISIBLE);
        ImageView selectEnglish = findViewById(R.id.select_english);
        selectEnglish.setVisibility(View.VISIBLE);
        UserInfo.setLanguage(getApplicationContext(), "en");
    }

}

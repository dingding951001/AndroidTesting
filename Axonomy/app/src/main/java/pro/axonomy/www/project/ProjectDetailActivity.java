package pro.axonomy.www.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pro.axonomy.www.R;

public class ProjectDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);

        String url = "http://google.com";
        WebView view = (WebView) this.findViewById(R.id.webview);
        view.setWebViewClient(new WebViewClient());
        view.loadUrl(url);
    }
}
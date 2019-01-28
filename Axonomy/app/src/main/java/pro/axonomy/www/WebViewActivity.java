package pro.axonomy.www;

import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    public static final String URL_PARAM = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);

        Bundle param = getIntent().getExtras();
        String url = null;
        if (param != null) {
            url = param.getString(URL_PARAM);
            Log.i("LoadWebView", "Creating Web View for URL: " + url);
        } else {
            Log.e("LoadWebView","Missing URL for WebViewActivity.");
            throw new RuntimeException("Missing url, cannot load the web view.");
        }

        WebView view = (WebView) this.findViewById(R.id.webview);

        // enable loading the https url
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);

        view.setWebViewClient(new WebViewClient());
        view.loadUrl(url);
    }
}
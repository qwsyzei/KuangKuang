package klsd.kuangkuang.main;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import klsd.kuangkuang.R;

public class Main2Activity extends BaseActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        webView = (WebView) findViewById(R.id.webview_certifiate123);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
            }
        });
//        webView.loadUrl("http://119.254.100.167:8080/uploads/1xxxx/index.html");
       webView.loadUrl("http://119.254.100.167:8080/uploads/xxxx2/index.html");
    }
}

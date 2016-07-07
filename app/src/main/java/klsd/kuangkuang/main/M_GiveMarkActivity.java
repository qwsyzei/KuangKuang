package klsd.kuangkuang.main;


import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 给我们评分
 */
import klsd.kuangkuang.R;

public class M_GiveMarkActivity extends BaseActivity {
private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__give_mark);
        initView();
    }

    private void initView() {
        webView= (WebView) findViewById(R.id.webView_givemark);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://tu.360.cn/2l5kh");
    }
}

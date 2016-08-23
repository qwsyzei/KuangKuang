package klsd.kuangkuang.main;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.Timer;
import java.util.TimerTask;

import klsd.kuangkuang.R;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;

/**
 * 证书
 */
public class T_CertificateActivity extends BaseActivity {
    private WebView webView;
    Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_certificate);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview_certifiate);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
            }
        });
        webView.loadUrl("http://www.eegem.com/gia/?no=5116040916");
        timer = new Timer();
        TimerTask timerTask = (new TimerTask() {
            @Override
            public void run() {
                getCertificate();
            }
        });
        timer.schedule(timerTask, 3000);//schedule这种用法中，有3个参数
        //第一个是TimerTask 类
        //第二个参数为延迟时间，0表示无延迟，否则比如是500毫秒，用户调用 schedule() 方法后，要等待500毫秒才可以第一次执行 run() 方法。
    }


    private void getCertificate() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("certificate", "5116040916");
//        params = KelaParams.generateSignParam("GET", Consts.certificateApi, params);
        new MyHTTP(this).baseRequest(Consts.certificateApi, JSONHandler.JTYPE_SMS_AUTH_CODE, HttpRequest.HttpMethod.GET, params, getHandler());
    }
}


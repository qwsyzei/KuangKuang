package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.Timer;
import java.util.TimerTask;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.Certificate;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 证书
 */
public class T_CertificateActivity extends BaseActivity implements View.OnClickListener {
    private WebView webView;
    Timer timer = null;
    private TextView tv_demand, tv_reset;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_certificate);
        setTitle(getString(R.string.gia_check_certificate));
        initView();
    }

    private void initView() {
        tv_demand = (TextView) findViewById(R.id.certificate_tv_demand);
        tv_reset = (TextView) findViewById(R.id.certificate_tv_reset);
        editText = (EditText) findViewById(R.id.certificate_edit_number);
        webView = (WebView) findViewById(R.id.webview_certifiate);
        editText.setCursorVisible(true);
        tv_demand.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
    }

    /**
     * 访问页面
     */
    private void gotoWebview() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 根据传入的参数再去加载新的网页
                return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
            }
        });
        webView.loadUrl("http://www.eegem.com/gia/?no=" + editText.getText().toString());
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.certificate_tv_demand:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
                if (editText.getText().toString().equals("")) {
                    ToastUtil.show(T_CertificateActivity.this, getString(R.string.input_certificate_number));
                } else if (editText.getText().length() != 10) {
                    ToastUtil.show(T_CertificateActivity.this, getString(R.string.wrong_certificate_number));
                } else {
                    UIutils.showDemanding(T_CertificateActivity.this);
                    gotoWebview();
                }
                break;
            case R.id.certificate_tv_reset:
                editText.setText("");
                break;
        }
    }

    private void getCertificate() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("certificate", editText.getText().toString());
//        params = KelaParams.generateSignParam("GET", Consts.certificateApi, params);
        new MyHTTP(this).baseRequest(Consts.certificateApi, JSONHandler.JTYPE_CERTIFICATE, HttpRequest.HttpMethod.GET, params, getHandler());
    }

    @Override
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_CERTIFICATE)) {

            Certificate certificate = (Certificate) handlerBundler.getSerializable("certificate");
            T_CertificateResultActivity.cer = certificate;
            UIutils.cancelLoading();
            Intent intent = new Intent(T_CertificateActivity.this, T_CertificateResultActivity.class);
            intent.putExtra("number", editText.getText().toString());
            startActivity(intent);
        }
    }
}


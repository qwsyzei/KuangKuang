package yksg.kuangkuang.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import yksg.kuangkuang.R;
import yksg.kuangkuang.views.ObservableScrollView;

public class Main2Activity extends Activity implements ObservableScrollView.ScrollViewListener {

    private RelativeLayout layoutHead;
    private ObservableScrollView scrollView;
    private LinearLayout layout_zhan;//占位用的布局
    private int height;

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);

        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview1);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        layoutHead = (RelativeLayout) findViewById(R.id.title_RelativeLayout);
        layout_zhan = (LinearLayout) findViewById(R.id.layout_zhanwei);

        //初始化webview
        //启用支持javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("http://weibo.com/u/2697099753");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        //获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = layout_zhan.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout_zhan.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = layout_zhan.getHeight();

                scrollView.setScrollViewListener(Main2Activity.this);
            }
        });

    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        //当向上滑动距离大于占位布局的高度值，就调整标题的背景
        if (y > height) {
            float alpha = (128);//0~255    完全透明~不透明

            //4个参数，第一个是透明度，后三个是红绿蓝三元色参数
            layoutHead.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
        } else {
            layoutHead.setBackgroundColor(Color.BLACK);
        }

    }
}
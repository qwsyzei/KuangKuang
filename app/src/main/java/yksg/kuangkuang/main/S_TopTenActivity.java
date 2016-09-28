package yksg.kuangkuang.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;

import yksg.kuangkuang.R;
import yksg.kuangkuang.adapters.S_TopAdapter;
import yksg.kuangkuang.models.Top;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.UIutils;
import yksg.kuangkuang.views.ObservableScrollView;
import yksg.kuangkuang.views.SelfListView;

/**
 * Top10
 */
public class S_TopTenActivity extends BaseActivity  implements ObservableScrollView.ScrollViewListener{
    private SelfListView listView;
    private ArrayList<Top> tlist;
    private S_TopAdapter sAdapter;
    private RelativeLayout layoutHead;
    private ObservableScrollView scrollView;
    private LinearLayout layout_zhan;//占位用的布局
    private int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s__top_ten);
        setTitle(getString(R.string.top10));
        initView();
    }

    private void initView() {
        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        layoutHead = (RelativeLayout) findViewById(R.id.title_RelativeLayout);
        layout_zhan = (LinearLayout) findViewById(R.id.layout_zhanwei);
        tlist = new ArrayList<>();
        listView = (SelfListView) findViewById(R.id.listview_top_ten);
        UIutils.showLoading(S_TopTenActivity.this);
        //获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = layout_zhan.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout_zhan.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = layout_zhan.getHeight();

                scrollView.setScrollViewListener(S_TopTenActivity.this);
            }
        });
        getTop10();
    }
    /**
     * 获取TOP10
     */
    public void getTop10() {
        RequestParams params = new RequestParams();
        new MyHTTP(this).baseRequest(Consts.articlesTopApi, JSONHandler.JTYPE_ARTICLES_TOP,
                HttpRequest.HttpMethod.GET, params, getHandler());
    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_TOP)) {
            UIutils.cancelLoading();
            tlist = (ArrayList<Top>) handlerBundler.getSerializable("top");
            if (tlist.size() == 0) {
                Log.d("Top10是没有数据的", "updateData() returned: " + "");
                return;
            }
            sAdapter = new S_TopAdapter(S_TopTenActivity.this, tlist,getHandler());
            listView.setAdapter(sAdapter);

        }

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

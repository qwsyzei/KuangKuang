package yksg.kuangkuang.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;

import yksg.kuangkuang.R;
import yksg.kuangkuang.adapters.M_FollowListAdapter;
import yksg.kuangkuang.models.Follows;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.utils.UIutils;
import yksg.kuangkuang.views.ObservableScrollView;
import yksg.kuangkuang.views.SelfListView;

/**
 * 关注列表
 */
public class M_FollowListActivity extends BaseActivity implements ObservableScrollView.ScrollViewListener{
    private SelfListView listView;
    private ArrayList<Follows> tlist;
    private M_FollowListAdapter fAdapter;
    private RelativeLayout layoutHead;
    private ObservableScrollView scrollView;
    private LinearLayout layout_zhan;//占位用的布局
    private int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__follow_list);
        setTitle(getString(R.string.my_follows));
        initView();
    }
    private void initView() {
        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        layoutHead = (RelativeLayout) findViewById(R.id.title_RelativeLayout);
        layout_zhan = (LinearLayout) findViewById(R.id.layout_zhanwei);
        //获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = layout_zhan.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout_zhan.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = layout_zhan.getHeight();

                scrollView.setScrollViewListener(M_FollowListActivity.this);
            }
        });
        tlist = new ArrayList<>();
        listView = (SelfListView) findViewById(R.id.listview_followlist);
        UIutils.showLoading(M_FollowListActivity.this);
        getfollowList();
    }
    /**
     * 获取关注列表
     */
    public void getfollowList() {
        new MyHTTP(this).baseRequest(Consts.followslistApi, JSONHandler.JTYPE_FOLLOW_LIST,
                HttpRequest.HttpMethod.GET, null, getHandler());
    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_FOLLOW_LIST)) {
            UIutils.cancelLoading();
            tlist = (ArrayList<Follows>) handlerBundler.getSerializable("follows");
            if (tlist.size() == 0) {
                ToastUtil.show(M_FollowListActivity.this,getString(R.string.no_data));
                return;
            }
            fAdapter = new M_FollowListAdapter(M_FollowListActivity.this, tlist,getHandler());
            listView.setAdapter(fAdapter);
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

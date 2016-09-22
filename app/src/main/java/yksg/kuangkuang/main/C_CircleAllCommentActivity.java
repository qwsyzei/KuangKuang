package yksg.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import yksg.kuangkuang.R;
import yksg.kuangkuang.adapters.C_CircleCommentAdapter;
import yksg.kuangkuang.models.CircleAllComment;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.DataCenter;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.KelaParams;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.utils.UIutils;
import yksg.kuangkuang.views.ContainsEmojiEditText;
import yksg.kuangkuang.views.ObservableScrollView;
import yksg.kuangkuang.views.PullToRefreshView;
import yksg.kuangkuang.views.SelfListView;

/**
 * 圈子的全部评论
 */
public class C_CircleAllCommentActivity extends BaseActivity implements View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, ObservableScrollView.ScrollViewListener {
    private TextView tv_send;
    private ContainsEmojiEditText edit_comment;
    public static String micropost_id;
    ArrayList<CircleAllComment> mylist;
    private C_CircleCommentAdapter allAdapter;
    private SelfListView listView;
    private int page = 1;
    MyHTTP http;
    // 自定义的listview的上下拉动刷新
    private PullToRefreshView mPullToRefreshView;
    private RelativeLayout layoutHead;
    private ObservableScrollView scrollView;
    private LinearLayout layout_zhan;//占位用的布局
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_circle_all_comment);
        setTitle(getString(R.string.all_comment));
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

                scrollView.setScrollViewListener(C_CircleAllCommentActivity.this);
            }
        });
        mylist = new ArrayList<>();
        Intent intent = getIntent();
        micropost_id = intent.getStringExtra("micropost_id");
        listView = (SelfListView) findViewById(R.id.listview_circle_allcomment);
        UIutils.showLoading(C_CircleAllCommentActivity.this);
        getCommentList();
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view_circle_allcomment);
        tv_send = (TextView) findViewById(R.id.all_comment_send_send);
        edit_comment = (ContainsEmojiEditText) findViewById(R.id.all_comment_send_edit);
        EditTListener(edit_comment);
        tv_send.setOnClickListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_comment_send_send:
                if (isSigned()) {
                    if (edit_comment.getText().toString().equals("")) {
                        ToastUtil.show(C_CircleAllCommentActivity.this, getString(R.string.please_input_content));
                    } else {
                        gotoComment();
                    }
                } else {
                    ToastUtil.show(C_CircleAllCommentActivity.this, getString(R.string.not_login));
                }
                break;
        }
    }

    /**
     * 给圈子评论
     */
    private void gotoComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("micropost_id", micropost_id);
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("content", edit_comment.getText().toString());
//        params.addQueryStringParameter("object", "");

        if (http == null) http = new MyHTTP(C_CircleAllCommentActivity.this);
        http.baseRequest(Consts.commentCircleApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    /**
     * 评论列表
     */
    private void getCommentList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("micropost_id", micropost_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", "15");
        params = KelaParams.generateSignParam("GET", Consts.circlecommentListApi, params);
        if (http == null) http = new MyHTTP(C_CircleAllCommentActivity.this);
        http.baseRequest(Consts.circlecommentListApi, JSONHandler.JTYPE_CIRCLE_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(C_CircleAllCommentActivity.this, getString(R.string.comment_success));
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_comment.getWindowToken(), 0); //强制隐藏键盘//清空数据并让它失去焦点
            Intent intent = new Intent(C_CircleAllCommentActivity.this, C_CircleAllCommentActivity.class);
            intent.putExtra("micropost_id", C_CircleAllCommentActivity.micropost_id);
            startActivity(intent);
            finish();
//            edit_comment.setText("");
//            edit_comment.setCursorVisible(false);
        } else if (jtype.equals(JSONHandler.JTYPE_CIRCLE_ALL_COMMENT)) {
            int curTradesSize = mylist.size();
            ArrayList<CircleAllComment> os = (ArrayList<CircleAllComment>) handlerBundler.getSerializable("circle_all_comment");
            Log.d("OS的长度", "handleMessage() returned: " + os.size());
            if (os.size() == 0) {
                UIutils.cancelLoading();
                ToastUtil.show(C_CircleAllCommentActivity.this, getString(R.string.no_more_data));
                return;
            }
            addTrades("bottom", os);//用于添加数据
            if (curTradesSize == 0) {
                mylist = os;
                allAdapter = new C_CircleCommentAdapter(C_CircleAllCommentActivity.this, mylist, getHandler());
                listView.setAdapter(allAdapter);
            } else {
                allAdapter.notifyDataSetChanged();
            }
            page += 1;
            UIutils.cancelLoading();
        }
    }

    public void addTrades(String from, List<CircleAllComment> ess) {
        List<String> ids = new ArrayList<String>();
        for (CircleAllComment o : mylist)
            ids.add(o.getId());

        for (CircleAllComment e : ess) {
            if (!ids.contains(e.getId())) {     //因为后台返回的会有的与前面的id重复，所以把不重复的添加了
                int i = from.equals("top") ? 0 : mylist.size();
                mylist.add(i, e);
            }
        }
        if (allAdapter != null) {
            allAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();

                getCommentList();
                ToastUtil.show(C_CircleAllCommentActivity.this, getString(R.string.load_more));
            }

        }, 2200);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPullToRefreshView.onHeaderRefreshComplete();
                mylist = new ArrayList<CircleAllComment>();
                page = 1;
                getCommentList();
                ToastUtil.show(C_CircleAllCommentActivity.this, getString(R.string.refresh_done));
            }

        }, 2200);
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

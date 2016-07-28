package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.C_CircleCommentAdapter;
import klsd.kuangkuang.models.CircleAllComment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;
import klsd.kuangkuang.views.ContainsEmojiEditText;
import klsd.kuangkuang.views.PullToRefreshView;
import klsd.kuangkuang.views.SelfListView;

/**
 * 圈子的全部评论
 */
public class C_CircleAllCommentActivity extends BaseActivity implements View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener {
    private TextView tv_send;
    private ContainsEmojiEditText edit_comment;
    String micropost_id;
    ArrayList<CircleAllComment> mylist;
    private C_CircleCommentAdapter allAdapter;
    private SelfListView listView;
    private int page = 1;
    private LinearLayout layout_send;
    MyHTTP http;
    // 自定义的listview的上下拉动刷新
    private PullToRefreshView mPullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_circle_all_comment);
        setTitle(getString(R.string.all_comment));
        initView();
    }
    private void initView() {
        layout_send = (LinearLayout) findViewById(R.id.all_comment_send);
        if (isSigned()) {
            layout_send.setVisibility(View.VISIBLE);
        } else {
            layout_send.setVisibility(View.GONE);
        }
        mylist = new ArrayList<>();
        Intent intent = getIntent();
        micropost_id = intent.getStringExtra("micropost_id");
        listView = (SelfListView) findViewById(R.id.listview_circle_allcomment);
        UIutils.showLoading(C_CircleAllCommentActivity.this);
        getCommentList();
        mPullToRefreshView= (PullToRefreshView) findViewById(R.id.pull_refresh_view_circle_allcomment);
        tv_send = (TextView) findViewById(R.id.all_comment_send_send);
        edit_comment = (ContainsEmojiEditText) findViewById(R.id.all_comment_send_edit);
        tv_send.setOnClickListener(this);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_comment_send_send:
                gotoComment();
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
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("limit", "10");
        params = KelaParams.generateSignParam("GET", Consts.circlecommentListApi, params);
        if (http == null) http = new MyHTTP(C_CircleAllCommentActivity.this);
        http.baseRequest(Consts.circlecommentListApi, JSONHandler.JTYPE_CIRCLE_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(C_CircleAllCommentActivity.this, "评论成功，刷新可见");
            edit_comment.setText("");
            edit_comment.setCursorVisible(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_comment.getWindowToken(), 0); //强制隐藏键盘//清空数据并让它失去焦点
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
                allAdapter = new C_CircleCommentAdapter(C_CircleAllCommentActivity.this, mylist);
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
                ToastUtil.show(C_CircleAllCommentActivity.this, "加载更多数据!");
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
                ToastUtil.show(C_CircleAllCommentActivity.this, "数据刷新完成!");
            }

        }, 2200);
    }
}

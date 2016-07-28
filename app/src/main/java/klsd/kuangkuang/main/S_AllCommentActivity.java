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
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_AllCommentAdapter;
import klsd.kuangkuang.models.AllComment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;
import klsd.kuangkuang.views.PullToRefreshView;
import klsd.kuangkuang.views.SelfListView;

/**
 * 全部评论
 */
public class S_AllCommentActivity extends BaseActivity implements View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener{
    private TextView tv_send;
    private EditText edit_comment;
    String article_id;
    ArrayList<AllComment> mylist;
    private S_AllCommentAdapter allAdapter;
    private SelfListView listView;
    String direction = "bottom";
    private int page = 1;
    private LinearLayout layout_send;
    // 自定义的listview的上下拉动刷新
    private PullToRefreshView mPullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_all_comment);
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
        article_id = intent.getStringExtra("a_id");
        listView = (SelfListView) findViewById(R.id.listview_allcomment);
        mPullToRefreshView= (PullToRefreshView) findViewById(R.id.pull_refresh_view_allcomment);
        getAllComment();
UIutils.showLoading(S_AllCommentActivity.this);
        tv_send = (TextView) findViewById(R.id.all_comment_send_send);
        edit_comment = (EditText) findViewById(R.id.all_comment_send_edit);
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
     * 评论
     */
    MyHTTP http;

    private void gotoComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("comment", edit_comment.getText().toString());
        params.addQueryStringParameter("commenter", DataCenter.getMember_id());
//        params.addQueryStringParameter("commenter", null);

        if (http == null) http = new MyHTTP(S_AllCommentActivity.this);
        http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    /**
     * 获取所有评论
     */
    private void getAllComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", "8");
        params = KelaParams.generateSignParam("GET", Consts.articlesCommentaryApi, params);
        if (http == null) http = new MyHTTP(S_AllCommentActivity.this);
        http.baseRequest(Consts.articlesCommentaryApi, JSONHandler.JTYPE_ARTICLES_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(S_AllCommentActivity.this, "评论成功，刷新可见");
            edit_comment.setText("");
            edit_comment.setCursorVisible(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edit_comment.getWindowToken(), 0); //强制隐藏键盘//清空数据并让它失去焦点
        } else if (jtype.equals(JSONHandler.JTYPE_ARTICLES_ALL_COMMENT)) {
            int curTradesSize = mylist.size();
            ArrayList<AllComment> os = (ArrayList<AllComment>) handlerBundler.getSerializable("all_comment");
            Log.d("OS的长度", "handleMessage() returned: " + os.size());
            if (os.size() == 0) {
                UIutils.cancelLoading();
                ToastUtil.show(S_AllCommentActivity.this, getString(R.string.no_more_data));
                return;
            }
            addTrades("bottom", os);//用于添加数据
            if (curTradesSize == 0) {
                mylist = os;
                allAdapter = new S_AllCommentAdapter(S_AllCommentActivity.this, mylist);
                listView.setAdapter(allAdapter);
            } else {

                allAdapter.notifyDataSetChanged();
            }
            page += 1;
            UIutils.cancelLoading();
        }
    }

    public void addTrades(String from, List<AllComment> ess) {
        List<String> ids = new ArrayList<String>();
        for (AllComment o : mylist)
            ids.add(o.getId());

        for (AllComment e : ess) {
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
                getAllComment();
                ToastUtil.show(S_AllCommentActivity.this, "加载更多数据!");
            }

        }, 2200);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.onHeaderRefreshComplete();
                mylist = new ArrayList<AllComment>();
                page = 1;
                getAllComment();
                ToastUtil.show(S_AllCommentActivity.this, "数据刷新完成!");
            }

        }, 2200);
    }
}

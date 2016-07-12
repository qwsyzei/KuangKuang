package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_AllCommentAdapter;
import klsd.kuangkuang.models.AllComment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 全部评论
 */
public class S_AllCommentActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener{
private TextView tv_send;
    private EditText edit_comment;
    String comment;
    String article_id;
    ArrayList<AllComment> mylist;
    private S_AllCommentAdapter allAdapter;
    private SwipeRefreshLayout swipeView;
    private ListView listView;
    String direction = "bottom";
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_all_comment);
        setTitle(getString(R.string.all_comment));
        initView();
    }

    private void initView() {
        mylist = new ArrayList<>();
        Intent intent=getIntent();
        article_id=intent.getStringExtra("a_id");
        listView= (ListView) findViewById(R.id.listview_allcomment);
        listView.setOnScrollListener(this);
        UIutils.showLoading(S_AllCommentActivity.this);
        swipt();
        gotoAllComment();

        tv_send= (TextView) findViewById(R.id.all_comment_send_send);
        edit_comment= (EditText) findViewById(R.id.all_comment_send_edit);
        comment=edit_comment.getText().toString();
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
        params.addQueryStringParameter("article_id",article_id);
        params.addQueryStringParameter("comment", comment);
        params.addQueryStringParameter("commenter", "48");

        if (http == null) http = new MyHTTP(S_AllCommentActivity.this);
        http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }
    /**
     * 获取所有评论
     */
    private void gotoAllComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id",article_id);
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("limit", "15");

        if (http == null) http = new MyHTTP(S_AllCommentActivity.this);
        http.baseRequest(Consts.articlesCommentaryApi, JSONHandler.JTYPE_ARTICLES_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(S_AllCommentActivity.this, "评论成功了");
        }else if(jtype.equals(JSONHandler.JTYPE_ARTICLES_ALL_COMMENT)){
            if (swipeView != null) swipeView.setRefreshing(false);//当获取到了就把下拉动画关了
            int curTradesSize = mylist.size();
            ArrayList<AllComment> os = (ArrayList<AllComment>) handlerBundler.getSerializable("all_comment");
            Log.d("OS的长度", "handleMessage() returned: " + os.size());
            if (os.size() == 0) {
                UIutils.cancelLoading();
                ToastUtil.show(S_AllCommentActivity.this, getString(R.string.no_more_data));
                return;
            }
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

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        int pos = absListView.getLastVisiblePosition();
        try {
            AllComment e = mylist.get(pos);
            if (e == mylist.get(mylist.size() - 1)) {
                loadDataFrom("bottom");

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
    private void swipt() {
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swip_s_all_comment);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {                 //此方法是刷新

                swipeView.setRefreshing(true);
                loadDataFrom("top");
            }
        });
    }
    //刷新数据的方法
    public void loadDataFrom(String from) {
        direction = from;
        if (direction.equals("bottom")) {
           gotoAllComment();

        }else {

            mylist = new ArrayList<AllComment>();
            page = 1;
           gotoAllComment();
        }
    }
}

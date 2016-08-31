package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_MywordAdapter;
import klsd.kuangkuang.models.Documents;
import klsd.kuangkuang.models.MyWord;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.CircleImageView;
import klsd.kuangkuang.views.PullToRefresh123View;
import klsd.kuangkuang.views.SelfListView;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 文章用户的主页
 */
public class S_AuthorActivity extends BaseActivity implements PullToRefresh123View.OnFooterRefreshListener,View.OnClickListener{
    private int limit = 10;
    private int page = 1;
    MyHTTP http;
    private SelfListView listView;
    private M_MywordAdapter mywordAdapter;
    private ArrayList<MyWord> sList;
    // 自定义的listview的上下拉动刷新
    private PullToRefresh123View mPullToRefreshView;
    String author_id;
    String picture_head, nickname, signature;
    String follow_state;
    TextView tv_name, tv_signature, tv_followed_number;
    private ImageView im_follow;
    private CircleImageView im_pic_head;
    private Documents documents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_author);
        Context context = getApplicationContext();
        initImageLoader(context);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        author_id = intent.getStringExtra("author_id");
        picture_head = intent.getStringExtra("picture_head");
        nickname = intent.getStringExtra("name");
        signature = intent.getStringExtra("signature");
        follow_state = intent.getStringExtra("follow_state");
        sList = new ArrayList<>();

        tv_name = (TextView) findViewById(R.id.author_name);
        tv_signature = (TextView) findViewById(R.id.author_signature);
        tv_followed_number = (TextView) findViewById(R.id.author_followed_number);
        im_follow = (ImageView) findViewById(R.id.im_s_author_follow);
        im_pic_head= (CircleImageView) findViewById(R.id.author_info_pic);
        tv_name.setText(nickname);
        if (signature.equals("null")) {
            tv_signature.setText(getString(R.string.not_too_lazy));
        } else {
            tv_signature.setText(signature);
        }
        if (follow_state.equals("0")) {
            im_follow.setImageResource(R.mipmap.follow_btn);
            im_follow.setOnClickListener(this);
        } else {
            im_follow.setImageResource(R.mipmap.followed_gray);
        }
        if (!picture_head.equals("null")) {
            ImageLoader.getInstance().displayImage(Consts.host + "/" + picture_head, im_pic_head);
        }
        listView = (SelfListView) findViewById(R.id.listview_author_words);
        listView.setFocusable(false);
        mPullToRefreshView = (PullToRefresh123View) findViewById(R.id.pull_refresh_view_author);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        getData();
        mywordAdapter = new M_MywordAdapter(S_AuthorActivity.this, sList, getHandler());
        listView.setAdapter(mywordAdapter);
    }
    /**
     * 获取个人资料
     */
    private void getData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", author_id);
        if (http == null) http = new MyHTTP(S_AuthorActivity.this);
        http.baseRequest(Consts.memberDocumentsApi, JSONHandler.JTYPE_MEMBER_DOCUMENTS, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }
    /**
     * 获取说说列表
     */
    private void getWordList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", author_id);
        params.addQueryStringParameter("limit", limit + "");
        params.addQueryStringParameter("page", page + "");
        if (http == null) http = new MyHTTP(S_AuthorActivity.this);
        http.baseRequest(Consts.micropostsMemberListApi, JSONHandler.JTYPE_MYWORD_LIST, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    @Override
    public void onFooterRefresh(PullToRefresh123View view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
                loadDataFrom();
                ToastUtil.show(S_AuthorActivity.this, getString(R.string.load_more));
            }
        }, 3000);
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_MYWORD_LIST)) {
            int curTradesSize = sList.size();
            ArrayList<MyWord> os = (ArrayList<MyWord>) handlerBundler.getSerializable("myword_list");

            Log.d("OS的长度", "handleMessage() returned: " + os.size());
            if (os.size() == 0) {
                ToastUtil.show(S_AuthorActivity.this, getString(R.string.no_more_data));
                return;
            }
            addTrades("bottom", os);
            if (curTradesSize == 0) {
                sList = os;
                mywordAdapter = new M_MywordAdapter(S_AuthorActivity.this, sList, getHandler());
                listView.setAdapter(mywordAdapter);
            } else {
                mywordAdapter.notifyDataSetChanged();
            }
            page += 1;
        }else if (jtype.equals(JSONHandler.JTYPE_ADD_FOLLOW)) {
            ToastUtil.show(S_AuthorActivity.this, getString(R.string.add_follows_success));
            im_follow.setImageResource(R.mipmap.followed_gray);
            follow_state="1";
        }else if (jtype.equals(JSONHandler.JTYPE_MEMBER_DOCUMENTS)) {
            documents = (Documents)handlerBundler.getSerializable("documents");
            if (documents.getFollowed_number().contains(".0")){
                tv_followed_number.setText("已有"+documents.getFollowed_number().replace(".0","")+"人关注");
            }else{
                tv_followed_number.setText("已有"+documents.getFollowed_number()+"人关注");
            }
            Log.d("粉丝数", "handleMessage() returned: " + documents.getFollowed_number());

        }
    }

    //刷新数据的方法
    public void loadDataFrom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getWordList();
            }
        }).start();

    }

    public void addTrades(String from, List<MyWord> ess) {
        List<String> ids = new ArrayList<String>();
        for (MyWord o : sList)
            ids.add(o.getId());

        for (MyWord e : ess) {
            if (!ids.contains(e.getId())) {
                int i = from.equals("top") ? 0 : sList.size();
                sList.add(i, e);
            }
        }
        if (mywordAdapter != null) {
            mywordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_s_author_follow:
                //添加关注
                if (isSigned()) {
                    if (author_id.equals(DataCenter.getMember_id())) {
                        ToastUtil.show(S_AuthorActivity.this, getString(R.string.cannot_add_follow_self));
                    } else {
                        gotoFollow();
                    }
                } else {
                    ToastUtil.show(S_AuthorActivity.this, getString(R.string.not_login));
                }
                break;
        }
    }
    /**
     * 关注
     */
    private void gotoFollow() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("object", author_id);
        if (http == null) http = new MyHTTP(S_AuthorActivity.this);
        http.baseRequest(Consts.addfollowsApi, JSONHandler.JTYPE_ADD_FOLLOW, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }
}

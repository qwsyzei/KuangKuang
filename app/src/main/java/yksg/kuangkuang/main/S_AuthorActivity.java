package yksg.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import yksg.kuangkuang.R;
import yksg.kuangkuang.adapters.M_MywordAdapter;
import yksg.kuangkuang.models.Documents;
import yksg.kuangkuang.models.MyWord;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.DataCenter;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.CircleImageView;
import yksg.kuangkuang.views.ObservableScrollView;
import yksg.kuangkuang.views.PullToRefresh123View;
import yksg.kuangkuang.views.SelfListView;

import static yksg.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 文章用户的主页
 */
public class S_AuthorActivity extends BaseActivity implements PullToRefresh123View.OnFooterRefreshListener,View.OnClickListener,ObservableScrollView.ScrollViewListener{
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
    private Picasso picasso;
    private RelativeLayout layoutHead;
    private ObservableScrollView scrollView;
    private LinearLayout layout_zhan;//占位用的布局
    private int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_author);
        Context context = getApplicationContext();
        initImageLoader(context);
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

                scrollView.setScrollViewListener(S_AuthorActivity.this);
            }
        });
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
//            ImageLoader.getInstance().displayImage(Consts.host + "/" + picture_head, im_pic_head);
            picasso.with(S_AuthorActivity.this).load(Consts.host + "/" + picture_head).into(im_pic_head);
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

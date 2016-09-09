package yksg.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import yksg.kuangkuang.R;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyDate;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.CircleImageView;

import static yksg.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 专题文章
 */
public class S_ArticleActivity extends BaseActivity implements View.OnClickListener {
    String testString, article_id, title, tag, views, like_number, comment_number, created_at, author_member_id;
    private String follow_state;
    private String is_like,is_collect;
    private String nickname, picture_head, author_signature;
    private TextView tv_content;
    private LinearLayout layout_like, layout_comment;
    private ImageView im_collect, im_share;
    private ImageView im_like;
    private PopupWindow sharePopwindow;
    private TextView tv_title, tv_tag;
    private TextView tv_views, tv_like, tv_comment;
    private TextView tv_time;//文章发表时间
    private TextView tv_author_name, tv_author_signature;
    private CircleImageView im_author_head;
    String common_time;//类似1天前的写法
    private ImageView im_add_follow;
    MyHTTP http;
    private WebView webView;
    private Picasso picasso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.s__article);
//        Context context = getApplicationContext();
//        initImageLoader(context);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        testString = intent.getStringExtra("content_html");
        article_id = intent.getStringExtra("article_id");
        title = intent.getStringExtra("title");
        tag = intent.getStringExtra("tag");
        views = intent.getStringExtra("views");
        like_number = intent.getStringExtra("like");
        comment_number = intent.getStringExtra("comment");
        created_at = intent.getStringExtra("created_at");
        nickname = intent.getStringExtra("nickname");
        picture_head = intent.getStringExtra("picture_son");
        author_signature = intent.getStringExtra("signature");
        author_member_id = intent.getStringExtra("author_member_id");
        follow_state = intent.getStringExtra("follow_state");
        is_like = intent.getStringExtra("is_like");
        is_collect=intent.getStringExtra("is_collect");
        common_time = MyDate.timeLogic(created_at.substring(0, 19).replace("T", " "));
        webView = (WebView) findViewById(R.id.webview_article);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(testString);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        im_add_follow = (ImageView) findViewById(R.id.im_s_artile_follow);
        tv_author_name = (TextView) findViewById(R.id.article_author_name);
        tv_author_signature = (TextView) findViewById(R.id.article_author_tag);
        im_author_head = (CircleImageView) findViewById(R.id.article_author_pic);
        im_like = (ImageView) findViewById(R.id.article_im_like);
//        im_author_head.setOnClickListener(this);
        tv_author_name.setText(nickname);

        layout_like = (LinearLayout) findViewById(R.id.layout_s_artile_like);
        layout_comment = (LinearLayout) findViewById(R.id.layout_s_artile_comment);
        im_collect = (ImageView) findViewById(R.id.im_s_artile_collect);
        tv_views = (TextView) findViewById(R.id.article_views_number);
        tv_like = (TextView) findViewById(R.id.article_like_number);
        tv_comment = (TextView) findViewById(R.id.article_comment_number);
        im_share = (ImageView) findViewById(R.id.im_article_title_share);
        tv_time = (TextView) findViewById(R.id.article_author_time);
        if (author_signature.equals("null")) {
            tv_author_signature.setText(getString(R.string.not_too_lazy));
        } else {
            tv_author_signature.setText(author_signature);
        }
        if (is_like != null && is_like.equals("1")) {
            im_like.setImageResource(R.mipmap.like01);
        } else {
            im_like.setImageResource(R.mipmap.like);
            layout_like.setOnClickListener(this);
        }
        if (!picture_head.equals("null")) {
//            ImageLoader.getInstance().displayImage(Consts.host + "/" + picture_head, im_author_head);
            picasso.with(S_ArticleActivity.this).load(Consts.host + "/" + picture_head).into(im_author_head);
        }
        if (follow_state.equals("0")) {
            im_add_follow.setImageResource(R.mipmap.follow_btn);
            im_add_follow.setOnClickListener(this);
        } else {
            im_add_follow.setImageResource(R.mipmap.followed_gray);
        }
        if (is_collect.equals("0")) {
            im_collect.setImageResource(R.mipmap.collect);
            im_collect.setOnClickListener(this);
        } else {
            im_collect.setImageResource(R.mipmap.collect_gray);
        }
        tv_time.setText(common_time);
        im_share.setOnClickListener(this);

        if (views.equals("null")) {
            tv_views.setText("0");
        } else if (views.contains(".0")) {
            tv_views.setText(views.replace(".0", ""));
        } else {
            tv_views.setText(views);
        }
        if (like_number.equals("null")) {
            tv_like.setText("0");
        } else if (like_number.contains(".0")) {
            tv_like.setText(like_number.replace(".0", ""));
        } else {
            tv_like.setText(like_number);
        }
        if (comment_number.equals("null")) {
            tv_comment.setText("0");
        } else if (comment_number.contains(".0")) {
            tv_comment.setText(comment_number.replace(".0", ""));
        } else {
            tv_comment.setText(comment_number);
        }

        tv_title = (TextView) findViewById(R.id.article_author_title);
        tv_title.setText(title);
        tv_tag = (TextView) findViewById(R.id.article_author_title_tag);
        tv_tag.setText("[" + tag + "]");

        layout_comment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_s_artile_like:
                if (isSigned()) {
                    gotoLike();
                } else {
                    ToastUtil.show(S_ArticleActivity.this, getString(R.string.not_login));
                }
                break;
            case R.id.layout_s_artile_comment:
                Intent intent = new Intent(S_ArticleActivity.this, S_AllCommentActivity.class);
                intent.putExtra("a_id", article_id);
                startActivity(intent);
                break;
            case R.id.im_article_title_share:
                Share_Dialog(view);
                break;
            case R.id.im_s_artile_collect:
                if (isSigned()) {
                    gotoCollect();
                } else {
                    ToastUtil.show(S_ArticleActivity.this, getString(R.string.not_login));
                }
                break;
            case R.id.im_s_artile_follow:
                //添加关注
                if (isSigned()) {
                        gotoFollow();
                } else {
                    ToastUtil.show(S_ArticleActivity.this, getString(R.string.not_login));
                }

                break;
//            case R.id.article_author_pic:
//                Intent intent1 = new Intent(S_ArticleActivity.this, S_AuthorActivity.class);
//                intent1.putExtra("author_id", author_member_id);
//                intent1.putExtra("picture_head", picture_head);
//                intent1.putExtra("name", nickname);
//                intent1.putExtra("signature", author_signature);
//                intent1.putExtra("follow_state", follow_state);
//                startActivity(intent1);
//                break;
        }
    }

    /**
     * 赞
     */
    private void gotoLike() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("object_id", article_id);
        params.addQueryStringParameter("species", "article");
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.addLikeApi, JSONHandler.JTYPE_ARTICLES_LIKE, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 收藏
     */
    private void gotoCollect() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCollectArticleApi, JSONHandler.JTYPE_COLLECT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    /**
     * 关注
     */
    private void gotoFollow() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("object", author_member_id);
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.addfollowsApi, JSONHandler.JTYPE_ADD_FOLLOW, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIKE)) {
            ToastUtil.show(S_ArticleActivity.this, getString(R.string.praise_done));
            im_like.setImageResource(R.mipmap.like01);
            tv_like.setText((Integer.parseInt(tv_like.getText().toString()) + 1) + "");
        } else if (jtype.equals(JSONHandler.JTYPE_COLLECT)) {
            ToastUtil.show(S_ArticleActivity.this, getString(R.string.collect_done));
            im_collect.setImageResource(R.mipmap.collect_gray);
            im_collect.setClickable(false);
        } else if (jtype.equals(JSONHandler.JTYPE_ADD_FOLLOW)) {
            ToastUtil.show(S_ArticleActivity.this, getString(R.string.add_follows_success));
            im_add_follow.setImageResource(R.mipmap.followed_gray);
            follow_state = "1";
        }

    }

    //分享窗口
    private void Share_Dialog(View v) {
        View pop_view = getLayoutInflater().inflate(R.layout.dialog_share, null, false);
        sharePopwindow = new PopupWindow(pop_view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 设置动画效果
//        sharePopwindow.setAnimationStyle(R.style.share_style);
        sharePopwindow.showAtLocation(v, Gravity.RIGHT, 0, 0);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb01b1b1b);
        pop_view.setBackgroundDrawable(dw);
        pop_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                sharePopwindow.dismiss();
                return true;
            }
        });
    }

}


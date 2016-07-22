package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_AllCommentAdapter;
import klsd.kuangkuang.models.AllComment;
import klsd.kuangkuang.richtext.RichText;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.CircleImageView;


/**
 * 专题文章
 */
public class S_ArticleActivity extends BaseActivity implements View.OnClickListener {
    String testString, article_id, title, tag, views, like_number, comment_number, created_at;
    private String nickname,picture_head,author_signature;
    private TextView tv_content;
    private LinearLayout layout_like, layout_comment;
    private TextView tv_allcomment;
    private S_AllCommentAdapter allAdapter;
    public ArrayList<AllComment> os;
    private int page = 1;
    private ListView listView;
    private TextView tv_dialog_send;
    private EditText edit_dialog_comment;
    private ImageView im_collect, im_share;
    private PopupWindow cPopwindow;
    private PopupWindow sharePopwindow;
    private TextView tv_title, tv_tag;
    private TextView tv_views, tv_like, tv_comment;
    private TextView tv_time;//文章发表时间
    private TextView tv_author_name,tv_author_signature;
    private CircleImageView im_author_head;
    String common_time;//类似1天前的写法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.s__article);
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
        nickname=intent.getStringExtra("nickname");
        picture_head=intent.getStringExtra("picture_son");
        author_signature=intent.getStringExtra("signature");
//        common_time = MyDate.timeLogic("2014-01-18 12:22:10");
        Log.d("我算的时间是", "initView() returned: " +created_at.substring(0, 19).replace("T", " "));
        common_time = MyDate.timeLogic(created_at.substring(0, 19).replace("T", " "));
        Log.d("VIEW是", "initView() returned: " + views);
        Log.d("LIKE是", "initView() returned: " + like_number);
        Log.d("COMMENT是", "initView() returned: " + comment_number);

        tv_content = (TextView) findViewById(R.id.tv_article_content);
        RichText.from(testString).into(tv_content);

        tv_author_name= (TextView) findViewById(R.id.article_author_name);
        tv_author_signature= (TextView) findViewById(R.id.article_author_tag);
        im_author_head= (CircleImageView) findViewById(R.id.article_author_pic);
        tv_author_name.setText(nickname);
        tv_author_signature.setText(author_signature);
        if (!picture_head.equals("null")){
            BitmapUtils bitmapUtils=new BitmapUtils(S_ArticleActivity.this);
            bitmapUtils.display(im_author_head,Consts.host+"/"+picture_head);
        }


        listView = (ListView) findViewById(R.id.listview_article_comment3);
        listView.setFocusable(false);//因为还有个scrollview，会影响显示位置
        layout_like = (LinearLayout) findViewById(R.id.layout_s_artile_like);
        layout_comment = (LinearLayout) findViewById(R.id.layout_s_artile_comment);
        im_collect = (ImageView) findViewById(R.id.im_s_artile_collect);
        tv_allcomment = (TextView) findViewById(R.id.article_look_all_comment);
        tv_views = (TextView) findViewById(R.id.article_views_number);
        tv_like = (TextView) findViewById(R.id.article_like_number);
        tv_comment = (TextView) findViewById(R.id.article_comment_number);
        im_share = (ImageView) findViewById(R.id.im_article_title_share);
        tv_time = (TextView) findViewById(R.id.article_author_time);
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


        Log.d("我能得到的ID是", "initView() returned: " + DataCenter.getMember_id());
        tv_title = (TextView) findViewById(R.id.article_author_title);
        tv_title.setText(title);
        tv_tag = (TextView) findViewById(R.id.article_author_title_tag);
        tv_tag.setText("[" + tag + "]");

        tv_allcomment.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        layout_comment.setOnClickListener(this);
        im_collect.setOnClickListener(this);
        gotoAllComment();
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
                if (isSigned()) {
                    Comment_Dialog(view);
                } else {
                    ToastUtil.show(S_ArticleActivity.this, getString(R.string.not_login));
                }
                break;
            case R.id.im_article_title_share:
                Share_Dialog(view);
                break;
            case R.id.im_s_artile_collect:
                if (isSigned()) {
                gotoCollect();
                } else {
                    ToastUtil.show(S_ArticleActivity.this,getString(R.string.not_login));
                }
                break;
            case R.id.article_look_all_comment:

                    Intent intent = new Intent(S_ArticleActivity.this, S_AllCommentActivity.class);
                    intent.putExtra("a_id", article_id);
                    startActivity(intent);

                break;
            case R.id.dialog_comment_send_send:
                gotoComment();

                break;
        }
    }

    /**
     * 赞
     */
    MyHTTP http;

    private void gotoLike() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("side", "like");
        params = KelaParams.generateSignParam("GET", Consts.articlesLikeApi, params);
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesLikeApi, JSONHandler.JTYPE_ARTICLES_LIKE, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 收藏
     */
    private void gotoCollect() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCollectArticleApi, JSONHandler.JTYPE_COLLECT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 评论
     */
    private void gotoComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("comment", edit_dialog_comment.getText().toString());
        params.addQueryStringParameter("commenter", DataCenter.getMember_id());
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 获取所有评论
     */
    private void gotoAllComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("limit", "3");
        params = KelaParams.generateSignParam("GET", Consts.articlesCommentaryApi, params);
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCommentaryApi, JSONHandler.JTYPE_ARTICLES_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIKE)) {
            ToastUtil.show(S_ArticleActivity.this, "已赞");
        } else if (jtype.equals(JSONHandler.JTYPE_ARTICLES_ALL_COMMENT)) {

            os = (ArrayList<AllComment>) handlerBundler.getSerializable("all_comment");
            if (os.size() == 0) {
                Log.d("评论是没有数据的", "updateData() returned: " + "");
                return;
            }
            allAdapter = new S_AllCommentAdapter(S_ArticleActivity.this, os);
            listView.setAdapter(allAdapter);

        } else if (jtype.equals(JSONHandler.JTYPE_COLLECT)) {
            ToastUtil.show(S_ArticleActivity.this, "已收藏");
        } else if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            cPopwindow.dismiss();
            ToastUtil.show(S_ArticleActivity.this, "评论成功");
        }

    }


    //评论窗口
    private void Comment_Dialog(View v) {
        View pop_view = getLayoutInflater().inflate(R.layout.dialog_comment, null, false);
        cPopwindow = new PopupWindow(pop_view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        // 设置动画效果
        cPopwindow.setAnimationStyle(R.style.mystyle);
        cPopwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);//可解决被软键盘遮住的问题
        cPopwindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb01b1b1b);
        pop_view.setBackgroundDrawable(dw);
        pop_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cPopwindow.dismiss();
                return true;
            }
        });

        edit_dialog_comment = (EditText) pop_view.findViewById(R.id.dialog_comment_edit);
        tv_dialog_send = (TextView) pop_view.findViewById(R.id.dialog_comment_send_send);
        tv_dialog_send.setOnClickListener(this);
        edit_dialog_comment.requestFocus();
        //调用系统输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

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


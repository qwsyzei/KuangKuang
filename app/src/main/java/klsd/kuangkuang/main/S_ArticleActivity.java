package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_AllCommentAdapter;
import klsd.kuangkuang.models.AllComment;
import klsd.kuangkuang.richtext.RichText;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;


/**
 * 专题文章
 */
public class S_ArticleActivity extends BaseActivity implements View.OnClickListener {
    String testString, article_id;
    private TextView tv_content;
    private LinearLayout layout_like, layout_comment, layout_collect;
    private TextView tv_allcomment;
    private S_AllCommentAdapter allAdapter;
    public ArrayList<AllComment> os;
    private int page = 1;
    private ListView listView;
//    private CommentDialog cDialog;
    private TextView tv_dialog_send;
    private EditText edit_dialog_comment;
    String str_comment;
private PopupWindow cPopwindow;
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
        tv_content = (TextView) findViewById(R.id.tv_article_content);
        RichText.from(testString).into(tv_content);
        listView = (ListView) findViewById(R.id.listview_article_comment3);
        layout_like = (LinearLayout) findViewById(R.id.layout_s_artile_like);
        layout_comment = (LinearLayout) findViewById(R.id.layout_s_artile_comment);
        layout_collect = (LinearLayout) findViewById(R.id.layout_s_artile_collect);
        tv_allcomment = (TextView) findViewById(R.id.article_look_all_comment);
        tv_allcomment.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        layout_comment.setOnClickListener(this);
        layout_collect.setOnClickListener(this);
        gotoAllComment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_s_artile_like:
                gotoLike();
                break;
            case R.id.layout_s_artile_comment:
                Comment_Dialog(view);
                break;
            case R.id.layout_s_artile_collect:
                gotoCollect();
//                gotoCollectCancel();
                break;
            case R.id.article_look_all_comment:
                Intent intent = new Intent(S_ArticleActivity.this, S_AllCommentActivity.class);
                intent.putExtra("a_id", article_id);
                startActivity(intent);
                break;
            case R.id.dialog_comment_send_send:
//                gotoComment();
                ToastUtil.show(S_ArticleActivity.this,edit_dialog_comment.getText().toString());
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
        params.addQueryStringParameter("member_id", "48");
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCollectArticleApi, JSONHandler.JTYPE_COLLECT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 取消收藏
     */
    private void gotoCollectCancel() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("member_id", "48");
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCollectDestroyApi, JSONHandler.JTYPE_COLLECT_DESTROY, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 评论
     */
    private void gotoComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("comment", edit_dialog_comment.getText().toString());
        params.addQueryStringParameter("commenter", "48");
        Log.d("写的评论是", "gotoComment() returned: " + edit_dialog_comment.getText().toString());
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
        } else if (jtype.equals(JSONHandler.JTYPE_COLLECT_DESTROY)) {
            ToastUtil.show(S_ArticleActivity.this, "已取消收藏");
        }

    }


    //评论窗口
    private void Comment_Dialog(View v) {
        View pop_view=getLayoutInflater().inflate(R.layout.dialog_comment,null,false);
        cPopwindow=new PopupWindow(pop_view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,true);
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
}


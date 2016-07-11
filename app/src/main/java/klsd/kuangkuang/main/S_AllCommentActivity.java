package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
public class S_AllCommentActivity extends BaseActivity implements View.OnClickListener{
private TextView tv_send;
    private EditText edit_comment;
    String comment;
    String article_id;
    ArrayList<AllComment> mylist=new ArrayList<>();
    private S_AllCommentAdapter allAdapter;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_all_comment);
        setTitle(getString(R.string.all_comment));
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        article_id=intent.getStringExtra("article_id");
        for (int i=0;i<mylist.size();i++){
            Log.d("评论列表是", "initView() returned: " + mylist.get(i).getBody());
        }

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
//        params.addQueryStringParameter("commenter", commenter);

        if (http == null) http = new MyHTTP(S_AllCommentActivity.this);
        http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }


    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(S_AllCommentActivity.this, "评论成功了");
        }
    }
}

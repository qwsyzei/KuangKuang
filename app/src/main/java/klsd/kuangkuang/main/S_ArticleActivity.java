package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.Serializable;
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
public class S_ArticleActivity extends BaseActivity implements View.OnClickListener{
     String testString,article_id;
    private TextView tv_content;
    private LinearLayout layout_like,layout_comment,layout_collect;
    private TextView tv_allcomment;
    private S_AllCommentAdapter allAdapter;
    private   ArrayList<AllComment> aList;
    public  ArrayList<AllComment> os;
    private int page = 1;
    private ListView listView;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s__article);
        initView();
    }
    private void initView() {
        Intent intent=getIntent();
        testString=intent.getStringExtra("content_html");
        article_id=intent.getStringExtra("article_id");
        tv_content= (TextView) findViewById(R.id.tv_article_content);
        RichText.from(testString).into(tv_content);
        aList=new ArrayList<>();
        listView= (ListView) findViewById(R.id.listview_article_comment3);
        layout_like= (LinearLayout) findViewById(R.id.layout_s_artile_like);
        layout_comment= (LinearLayout) findViewById(R.id.layout_s_artile_comment);
        layout_collect= (LinearLayout) findViewById(R.id.layout_s_artile_collect);
        tv_allcomment= (TextView) findViewById(R.id.article_look_all_comment);
        tv_allcomment.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        layout_comment.setOnClickListener(this);
        layout_collect.setOnClickListener(this);
        gotoAllComment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_s_artile_like:
                gotoLike();
                break;
            case R.id.layout_s_artile_comment:
                break;
            case R.id.layout_s_artile_collect:
                break;
            case R.id.article_look_all_comment:
                Intent intent=new Intent(S_ArticleActivity.this,S_AllCommentActivity.class);
                intent.putExtra("article_id", article_id);
                startActivity(intent);
                break;
        }
    }

    /**
     * 赞
     */
    MyHTTP http;
    private void gotoLike() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id",article_id);
        params.addQueryStringParameter("side", "like");
        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesLikeApi, JSONHandler.JTYPE_ARTICLES_LIKE, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    /**
     * 获取所有评论
     */
    private void gotoAllComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id",article_id);
        params.addQueryStringParameter("page", "1");
        params.addQueryStringParameter("limit", "15");
//        params.addQueryStringParameter("commenter", commenter);

        if (http == null) http = new MyHTTP(S_ArticleActivity.this);
        http.baseRequest(Consts.articlesCommentaryApi, JSONHandler.JTYPE_ARTICLES_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIKE)) {
            ToastUtil.show(S_ArticleActivity.this,"赞成功了");
        }else if(jtype.equals(JSONHandler.JTYPE_ARTICLES_ALL_COMMENT)){
            ToastUtil.show(S_ArticleActivity.this, "全部评论获取了");
                int curTradesSize = aList.size();
                os = (ArrayList<AllComment>) handlerBundler.getSerializable("all_comment");
                if (curTradesSize == 0) {
                    if (os.size()>3){
                        for (int i=0;i<3;i++){
                            aList.add(os.get(i));//将前3个筛选出来
                        }
                        Log.d("进入筛选界面了", "Alist的长度是" + aList.size());
                    }else{
                        aList=os;
                        Log.d("直接等于就行了", "updateData() returned: " + "");
                    }
                    allAdapter = new S_AllCommentAdapter(S_ArticleActivity.this,aList);
                    listView.setAdapter(allAdapter);

                } else {

                    allAdapter.notifyDataSetChanged();
                }

            }

    }
}

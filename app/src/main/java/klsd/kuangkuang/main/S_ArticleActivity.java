package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.richtext.RichText;

/**
 * 专题文章
 */
public class S_ArticleActivity extends BaseActivity {
     String testString;
    private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s__article);
        initView();
    }
    private void initView() {
        Intent intent=getIntent();
        testString=intent.getStringExtra("content_html");
        tv_content= (TextView) findViewById(R.id.tv_article_content);
        RichText.from(testString).into(tv_content);
    }
}

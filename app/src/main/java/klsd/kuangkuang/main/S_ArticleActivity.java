package klsd.kuangkuang.main;

import android.os.Bundle;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.richtext.RichText;

/**
 * 专题文章
 */
public class S_ArticleActivity extends BaseActivity {
     String testString="<html>\n" +
             "  <head>\n" +
             "    <title>Head First Lounge Elixirs</title>\n" +
             "  </head>\n" +
             "  <body>\n" +
             "    <h1>Our Elixirs</h1>\n" +
             "\n" +
             "    <h2>Green Tea Cooler</h2>\n" +
             "    <p>\n" +
             "      <img src=\"http://cdn.duitang.com/uploads/item/201512/06/20151206223623_w4Eie.jpeg\">\n" +
             "      Chock full of vitamins and minerals, this elixir\n" +
             "      combines the healthful benefits of green tea with\n" +
             "      a twist of chamomile blossoms and ginger root.\n" +
             "    </p>\n" +
             "    <h2>Raspberry Ice Concentration</h2>\n" +
             "    <p>\n" +
             "      <img src=\"http://cdn.duitang.com/uploads/item/201512/06/20151206223623_w4Eie.jpeg\">\n" +
             "      Combining raspberry juice with lemon grass,\n" +
             "      citrus peel and rosehips, this icy drink\n" +
             "      will make your mind feel clear and crisp.\n" +
             "    </p>\n" +
             "    <h2>Blueberry Bliss Elixir</h2>\n" +
             "    <p>\n" +
             "      <img src=\"http://cdn.duitang.com/uploads/item/201512/06/20151206223623_w4Eie.jpeg\">\n" +
             "      Blueberries and cherry essence mixed into a base\n" +
             "      of elderflower herb tea will put you in a relaxed\n" +
             "      state of bliss in no time.\n" +
             "    </p>\n" +
             "    <h2>Cranberry Antioxidant Blast</h2>\n" +
             "    <p>\n" +
             "      <img src=\"http://cdn.duitang.com/uploads/item/201512/06/20151206223623_w4Eie.jpeg\">\n" +
             "      Wake up to the flavors of cranberry and hibiscus\n" +
             "      in this vitamin C rich elixir.\n" +
             "    </p>\n" +
             "  </body>\n" +
             "</html>";
    private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s__article);
        initView();
    }

    private void initView() {
        tv_content= (TextView) findViewById(R.id.tv_article_content);
        RichText.from(testString).into(tv_content);
    }
}

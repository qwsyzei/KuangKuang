package klsd.kuangkuang.main;


import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_TopAdapter;
import klsd.kuangkuang.models.Top;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;

/**
 * Top10
 */
public class S_TopTenActivity extends BaseActivity {
    private ListView listView;
    private ArrayList<Top> tlist;
    private S_TopAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s__top_ten);
        setTitle(getString(R.string.top10));
        initView();
    }

    private void initView() {
        tlist = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview_top_ten);
        getTop10();
    }
    /**
     * 获取TOP10
     */
    public void getTop10() {
        RequestParams params = new RequestParams();
        params = KelaParams.generateSignParam("GET", Consts.articlesTopApi, params);
        new MyHTTP(this).baseRequest(Consts.articlesTopApi, JSONHandler.JTYPE_ARTICLES_TOP,
                HttpRequest.HttpMethod.GET, params, getHandler());
    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_TOP)) {

            tlist = (ArrayList<Top>) handlerBundler.getSerializable("top");
            if (tlist.size() == 0) {
                Log.d("Top10是没有数据的", "updateData() returned: " + "");
                return;
            }
            Log.d("这个评论是什么呢", "updateData() returned: " + tlist.get(0).getCreated_at());
            sAdapter = new S_TopAdapter(S_TopTenActivity.this, tlist,getHandler());
            listView.setAdapter(sAdapter);

        }

    }
}

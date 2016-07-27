package klsd.kuangkuang.main;


import android.os.Bundle;
import android.util.Log;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_MyCollectAdapter;
import klsd.kuangkuang.models.MyCollect;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.PullToRefreshView;
import klsd.kuangkuang.views.SelfListView;

/**
 * 我的收藏
 */
public class M_MyCollectActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener {
M_MyCollectAdapter myCollectAdapter;
    ArrayList<MyCollect> myList;
    private SelfListView listView;
    private int page = 1;
    // 自定义的listview的上下拉动刷新
    private PullToRefreshView mPullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__my_collect);
        setTitle(getString(R.string.my_collect));
        initView();
    }

    private void initView() {
        myList=new ArrayList<>();
        listView= (SelfListView) findViewById(R.id.listview_my_collect);
        mPullToRefreshView= (PullToRefreshView) findViewById(R.id.pull_refresh_view_my_collect);
        getCollectShowList();
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);
    }
    MyHTTP http;
    /**
     * 获取收藏列表
     */
    private void getCollectShowList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", "6");

        if (http == null) http = new MyHTTP(M_MyCollectActivity.this);
        http.baseRequest(Consts.articlesCollectShowApi, JSONHandler.JTYPE_COLLECT_SHOW, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }

    public void updateData() {
        super.updateData();
         if(jtype.equals(JSONHandler.JTYPE_COLLECT_SHOW)){
            int curTradesSize = myList.size();
            ArrayList<MyCollect> os = (ArrayList<MyCollect>) handlerBundler.getSerializable("collect_show");
            Log.d("OS的长度", "handleMessage() returned: " + os.size());
            if (os.size() == 0) {
                ToastUtil.show(M_MyCollectActivity.this, getString(R.string.no_more_data));
                return;
            }
             addTrades("bottom",os);
            if (curTradesSize == 0) {
                myList = os;
                myCollectAdapter = new M_MyCollectAdapter(M_MyCollectActivity.this, myList,getHandler());
                listView.setAdapter(myCollectAdapter);

            } else {

                myCollectAdapter.notifyDataSetChanged();
            }
            page += 1;
        }
    }
    public void addTrades(String from, List<MyCollect> ess) {
        List<String> ids = new ArrayList<String>();
        for (MyCollect o : myList)
            ids.add(o.getId());

        for (MyCollect e : ess) {
            if (!ids.contains(e.getId())) {     //因为后台返回的会有的与前面的id重复，所以把不重复的添加了
                int i = from.equals("top") ? 0 : myList.size();
                myList.add(i, e);
            }
        }
        if (myCollectAdapter != null) {
            myCollectAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();

                getCollectShowList();
                ToastUtil.show(M_MyCollectActivity.this, "加载更多数据!");
            }

        }, 2200);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {

                mPullToRefreshView.onHeaderRefreshComplete();

                myList = new ArrayList<MyCollect>();
                page = 1;
                getCollectShowList();
                ToastUtil.show(M_MyCollectActivity.this, "数据刷新完成!");
            }

        }, 2200);
    }
}

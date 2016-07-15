package klsd.kuangkuang.main;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

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
import klsd.kuangkuang.utils.UIutils;

/**
 * 我的收藏
 */
public class M_MyCollectActivity extends BaseActivity implements AbsListView.OnScrollListener {
M_MyCollectAdapter myCollectAdapter;
    ArrayList<MyCollect> myList;
//    private TextView tv_top;
    private SwipeRefreshLayout swipeView;
    private ListView listView;
    String direction = "bottom";
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__my_collect);
        setTitle(getString(R.string.my_collect));
        initView();
    }

    private void initView() {
        myList=new ArrayList<>();
//        tv_top= (TextView) findViewById(R.id.my_collect_tv_top);

        listView= (ListView) findViewById(R.id.listview_my_collect);
        listView.setOnScrollListener(this);
        UIutils.showLoading(M_MyCollectActivity.this);
        swipt();
        getCollectShow();
    }
    MyHTTP http;
    /**
     * 获取收藏列表
     */
    private void getCollectShow() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", "10");

        if (http == null) http = new MyHTTP(M_MyCollectActivity.this);
        http.baseRequest(Consts.articlesCollectShowApi, JSONHandler.JTYPE_COLLECT_SHOW, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }
    /**
     * 取消收藏
     */
    private void gotoCollectCancel() {
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("member_id", "48");
        if (http == null) http = new MyHTTP(M_MyCollectActivity.this);
        http.baseRequest(Consts.articlesCollectDestroyApi, JSONHandler.JTYPE_COLLECT_DESTROY, HttpRequest.HttpMethod.GET,
                params, getHandler());

    }
    public void updateData() {
        super.updateData();
         if(jtype.equals(JSONHandler.JTYPE_COLLECT_SHOW)){
            if (swipeView != null) swipeView.setRefreshing(false);//当获取到了就把下拉动画关了
            int curTradesSize = myList.size();
            ArrayList<MyCollect> os = (ArrayList<MyCollect>) handlerBundler.getSerializable("collect_show");
            Log.d("OS的长度", "handleMessage() returned: " + os.size());
            if (os.size() == 0) {
                UIutils.cancelLoading();
                ToastUtil.show(M_MyCollectActivity.this, getString(R.string.no_more_data));
                return;
            }
             addTrades("bottom",os);
            if (curTradesSize == 0) {
                myList = os;
//                tv_top.setText(getString(R.string.my_collect)+"："+myList.size());//收藏数，先不用
                myCollectAdapter = new M_MyCollectAdapter(M_MyCollectActivity.this, myList);
                listView.setAdapter(myCollectAdapter);

            } else {

                myCollectAdapter.notifyDataSetChanged();
            }
            page += 1;
            UIutils.cancelLoading();
        }else if (jtype.equals(JSONHandler.JTYPE_COLLECT_DESTROY)) {
             ToastUtil.show(M_MyCollectActivity.this, "已取消收藏");
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
    public void onScrollStateChanged(AbsListView absListView, int i) {
        int pos = absListView.getLastVisiblePosition();
        try {
            MyCollect e = myList.get(pos);
            if (e == myList.get(myList.size() - 1)) {
                loadDataFrom("bottom");

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
    private void swipt() {
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swip_my_collect);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {                 //此方法是刷新

                swipeView.setRefreshing(true);
                loadDataFrom("top");
            }
        });
    }
    //刷新数据的方法
    public void loadDataFrom(String from) {
        direction = from;
        if (direction.equals("bottom")) {
            getCollectShow();

        }else {

            myList = new ArrayList<MyCollect>();
            page = 1;
            getCollectShow();
        }
    }
}

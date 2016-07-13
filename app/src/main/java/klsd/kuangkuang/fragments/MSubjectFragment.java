package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_SubjectAdapter;
import klsd.kuangkuang.main.BaseActivity;
import klsd.kuangkuang.main.LoginActivity;
import klsd.kuangkuang.main.S_TopTenActivity;
import klsd.kuangkuang.models.Subject;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 专题
 */
public class MSubjectFragment extends MyBaseFragment implements AbsListView.OnScrollListener,View.OnClickListener {
    View view;
    private S_SubjectAdapter sAdapter;
    private ListView listView;
    private ArrayList<Subject> sList;
    private static Activity a;
    private SwipeRefreshLayout swipeView;
   private int limit = 15;
    private int page = 1;
    String direction = "bottom";
    private TextView tv_top;
    public MSubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        a = this.getActivity();
        view = inflater.inflate(R.layout.fragment_msubject, container, false);

        setTitle(getString(R.string.main_subject));
        initView();
        return view;
    }

    private void initView() {
        sList = new ArrayList<Subject>();
        Log.d("去获取了", "initView() returned: " + "");
        tv_top= (TextView) view.findViewById(R.id.tv_title_right);
        tv_top.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.listview_msubject);
        listView.setOnScrollListener(this);
//        getSubjectList();
        UIutils.showLoading(a);
        swipt();
        getArticlesList();

//        listView.setAdapter(sAdapter);
    }

    public void setTitle(String title) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }

    public void setTitleRight(String tv_right) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title_right);
        if (textView != null) textView.setText(tv_right);
    }
    MyHTTP http;
    private void getArticlesList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("limit","10");
        params.addQueryStringParameter("page", page+"");
        if (http == null) http = new MyHTTP(a);
        http.baseRequest(Consts.articlesListApi, JSONHandler.JTYPE_ARTICLES_LIST, HttpRequest.HttpMethod.GET,
                params, handler);
    }



    private Handler handler = new BaseActivity.KelaHandler(a){
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            String res = bundle.getString("result");
            String jtype = bundle.getString("jtype");
            if (res == null) {
//				ToastUtil.show(a, "交易完成数据网络请求失败");
                a.startActivity(new Intent(a, LoginActivity.class));
                a.finish();
            } else if (res.equals("OK")) {
                if (swipeView != null) swipeView.setRefreshing(false);
                if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIST)) {
                    int curTradesSize = sList.size();
                    ArrayList<Subject> os = (ArrayList<Subject>) bundle.getSerializable("subject_article");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        UIutils.cancelLoading();
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    if (curTradesSize == 0) {
                        sList = os;
                        sAdapter = new S_SubjectAdapter(a, sList,handler);
                        listView.setAdapter(sAdapter);

                    } else {

                        sAdapter.notifyDataSetChanged();
                    }
                    page += 1;
                    UIutils.cancelLoading();
                }
            } else {
                ToastUtil.show(a, res);
            }
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        int pos = absListView.getLastVisiblePosition();
        try {
            Subject e = sList.get(pos);
            if (e == sList.get(sList.size() - 1)) {
                loadDataFrom("bottom");

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
    public void swipt() {
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swip_msubject);
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
        if (from.equals("bottom")) {
            getArticlesList();

        }else {

            sList = new ArrayList<Subject>();
            page = 1;
            getArticlesList();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_title_right:
                myStartActivity(new Intent(a, S_TopTenActivity.class));
                break;
        }
    }
}

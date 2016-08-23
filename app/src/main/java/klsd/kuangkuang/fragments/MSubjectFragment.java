package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_SubjectAdapter;
import klsd.kuangkuang.main.BaseActivity;
import klsd.kuangkuang.main.S_TopTenActivity;
import klsd.kuangkuang.models.Subject;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;
import klsd.kuangkuang.views.PullToRefreshView;
import klsd.kuangkuang.views.SelfListView;

/**
 * 专题
 */
public class MSubjectFragment extends MyBaseFragment implements View.OnClickListener,PullToRefreshView.OnHeaderRefreshListener,PullToRefreshView.OnFooterRefreshListener {
    View view;
    private S_SubjectAdapter sAdapter;
    private SelfListView listView;
    private ArrayList<Subject> sList;
    private static Activity a;
   private int limit = 8;
    private int page = 1;
    private TextView tv_top;
    // 自定义的listview的上下拉动刷新
    private PullToRefreshView mPullToRefreshView;
    private String tagtag="0";
    public MSubjectFragment(String tag) {
        this.tagtag=tag;
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
        tv_top= (TextView) view.findViewById(R.id.tv_title_right);
        tv_top.setOnClickListener(this);
        listView = (SelfListView) view.findViewById(R.id.listview_msubject);
        mPullToRefreshView= (PullToRefreshView)view.findViewById(R.id.pull_refresh_view_subject);
        UIutils.showLoading(a);
        getArticlesList();
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

    }
    @Override
    public void onResume() {
        super.onResume();
//        ToastUtil.show(a, "我是专题");
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
        params.addQueryStringParameter("limit", limit+"");
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("tag", tagtag);
//        params = KelaParams.generateSignParam("GET", Consts.articlesListApi, params);
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
				ToastUtil.show(a, getString(R.string.network_problem));
            } else if (res.equals("OK")) {
                if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIST)) {
                    int curTradesSize = sList.size();
                    ArrayList<Subject> os = (ArrayList<Subject>) bundle.getSerializable("subject_article");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        UIutils.cancelLoading();
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    addTrades("bottom",os);
                    if (curTradesSize == 0) {
                        sList = os;
                        sAdapter = new S_SubjectAdapter(a, sList,handler);
                        listView.setAdapter(sAdapter);

                    } else {

                        sAdapter.notifyDataSetChanged();
                    }
                    UIutils.cancelLoading();
                    page += 1;
                }
            } else {
                ToastUtil.show(a, res);
            }
        }
    };
    public void addTrades(String from, List<Subject> ess) {
        List<String> ids = new ArrayList<String>();
        for (Subject o : sList)
            ids.add(o.getId());

        for (Subject e : ess) {
            if (!ids.contains(e.getId())) {
                int i = from.equals("top") ? 0 : sList.size();
                sList.add(i, e);
            }
        }
        if (sAdapter != null) {
            sAdapter.notifyDataSetChanged();
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

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
                getArticlesList();
                ToastUtil.show(a, getString(R.string.load_more));
            }
        }, 2400);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.onHeaderRefreshComplete();
                sList = new ArrayList<Subject>();
                page = 1;
                getArticlesList();
                ToastUtil.show(a, getString(R.string.refresh_done));
            }
        }, 2400);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("专题专题被关闭了", "onDestroy() returned: " + "");
    }
}

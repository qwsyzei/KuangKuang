package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.C_CircleAdapter;
import klsd.kuangkuang.main.BaseActivity;
import klsd.kuangkuang.main.C_ReleaseWordActivity;
import klsd.kuangkuang.main.LoginActivity;
import klsd.kuangkuang.models.Circles;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;
import klsd.kuangkuang.views.PullToRefreshView;

/**
 * 圈子
 */
public class MCircleFragment extends MyBaseFragment implements View.OnClickListener, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {
    View view;
    private static Activity a;
    private List<Circles> cirList;
    private C_CircleAdapter cAdapter;
    private ListView listView;
    private TextView tv_release;
    private int limit = 7;
    private int page = 1;
    // 自定义的listview的上下拉动刷新
    private PullToRefreshView mPullToRefreshView;

    public MCircleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        a = this.getActivity();
        view = inflater.inflate(R.layout.fragment_mcircle, container, false);
        setTitle(getString(R.string.main_circle));
        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        ToastUtil.show(a,"我是圈子");
    }

    /**
     * 数据初始化
     */
    private void initView() {
        cirList = new ArrayList<>();
        tv_release = (TextView) view.findViewById(R.id.tv_title_circle_right);
        tv_release.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.listview_circle);
        getcircleList();
        UIutils.showLoading(a);
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_refresh_view_circle);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

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

    private void getcircleList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getcircleList123();
            }
        }).start();
    }

    /**
     * 得到朋友圈列表
     */
    private void getcircleList123() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", limit + "");
            if (http == null) http = new MyHTTP(a);
            http.baseRequest(Consts.micropostsListApi, JSONHandler.JTYPE_CIRCLE_LIST, HttpRequest.HttpMethod.GET,
                    params, handler);
    }

    private Handler handler = new BaseActivity.KelaHandler(a) {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            String res = bundle.getString("result");
            String jtype = bundle.getString("jtype");
            if (res == null) {
                ToastUtil.show(a, getString(R.string.network_problem));
            } else if (res.equals("OK")) {
                if (jtype.equals(JSONHandler.JTYPE_CIRCLE_LIST)) {
                    int curTradesSize = cirList.size();
                    ArrayList<Circles> os = (ArrayList<Circles>) bundle.getSerializable("circle_list");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        UIutils.cancelLoading();
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    addTrades("bottom", os);
                    if (curTradesSize == 0) {
                        cirList = os;
                        cAdapter = new C_CircleAdapter(a, cirList, handler);
                        listView.setAdapter(cAdapter);
                    } else {

                        cAdapter.notifyDataSetChanged();
                    }
                    page += 1;
                    UIutils.cancelLoading();
                }
            } else {
                ToastUtil.show(a, res);
            }
        }
    };

    public void addTrades(String from, List<Circles> ess) {
        List<String> ids = new ArrayList<String>();
        for (Circles o : cirList)
            ids.add(o.getId());

        for (Circles e : ess) {
            if (!ids.contains(e.getId())) {
                int i = from.equals("top") ? 0 : cirList.size();
                cirList.add(i, e);
            }
        }
        if (cAdapter != null) {
            cAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 假数据
     */
    private Bitmap bit_head;

    private List<Circles> getSubjectList() {
        cirList = new ArrayList<Circles>();
        Circles sub = new Circles(a);
        sub.setContent_son("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……");
        sub.setNickname("当年明月");
        sub.setLike("15");
        sub.setComment("3");
        sub.setCreated_at("发布于1小时前");
        sub.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang01));
        cirList.add(sub);

        return cirList;
    }

    public boolean isSigned() {
        return DataCenter.isSigned();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_circle_right:
                if (isSigned()) {
                    myStartActivity(new Intent(getActivity(), C_ReleaseWordActivity.class));
                } else {
                    ToastUtil.show(a, getString(R.string.not_login));
                }
                break;
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
                getcircleList();
                ToastUtil.show(a, getString(R.string.load_more));
            }
        }, 3000);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.onHeaderRefreshComplete();
                cirList = new ArrayList<Circles>();
                page = 1;
                getcircleList();
                ToastUtil.show(a, getString(R.string.refresh_done));
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("圈子被关闭了", "onDestroy() returned: " + "");
    }
}

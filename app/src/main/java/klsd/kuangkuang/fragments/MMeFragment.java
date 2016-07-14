package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_MywordAdapter;
import klsd.kuangkuang.main.BaseActivity;
import klsd.kuangkuang.main.C_ReleaseWordActivity;
import klsd.kuangkuang.main.LoginActivity;
import klsd.kuangkuang.main.M_MyCollectActivity;
import klsd.kuangkuang.main.M_SetActivity;
import klsd.kuangkuang.models.MyWord;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 我
 */
public class MMeFragment extends MyBaseFragment implements View.OnClickListener, AbsListView.OnScrollListener {
    View view;
    private M_MywordAdapter mywordAdapter;
    private ImageView im_set;
    private RelativeLayout layout_collect;
    private ArrayList<MyWord> sList;
    private ListView listView;
    private static Activity a;
    private int limit = 10;
    private int page = 1;

    private LinearLayout layout_release;

    public MMeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        a = this.getActivity();
        view = inflater.inflate(R.layout.fragment_me, container, false);
        setTitle(getString(R.string.main_me));
        initView();
        return view;
    }

    private void initView() {
        sList = new ArrayList<>();
        im_set = (ImageView) view.findViewById(R.id.im_title_set);
        layout_collect = (RelativeLayout) view.findViewById(R.id.me_collect_layout);
        layout_release = (LinearLayout) view.findViewById(R.id.layout_me_release_word_now);
        listView = (ListView) view.findViewById(R.id.listview_me_myword);
        listView.setOnScrollListener(this);
        layout_release.setOnClickListener(this);
        layout_collect.setOnClickListener(this);
        im_set.setOnClickListener(this);
        getMywordList();
    }

    MyHTTP http;

    private void getMywordList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", "48");
        params.addQueryStringParameter("limit", limit + "");
        params.addQueryStringParameter("page", page + "");
        if (http == null) http = new MyHTTP(a);
        http.baseRequest(Consts.micropostsMemberListApi, JSONHandler.JTYPE_MYWORD_LIST, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    private Handler handler = new BaseActivity.KelaHandler(a) {
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
                if (jtype.equals(JSONHandler.JTYPE_MYWORD_LIST)) {
                    int curTradesSize = sList.size();
                    ArrayList<MyWord> os = (ArrayList<MyWord>) bundle.getSerializable("myword_list");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    if (curTradesSize == 0) {
                        sList = os;
                        mywordAdapter = new M_MywordAdapter(a, sList);
                        listView.setAdapter(mywordAdapter);

                    } else {

                        mywordAdapter.notifyDataSetChanged();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_title_set:
                myStartActivity(new Intent(getActivity(), M_SetActivity.class));
                break;
            case R.id.me_collect_layout:
                myStartActivity(new Intent(getActivity(), M_MyCollectActivity.class));
                break;
            case R.id.layout_me_release_word_now:
                myStartActivity(new Intent(getActivity(), C_ReleaseWordActivity.class));
                break;
        }
    }

    public void setTitle(String title) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }

    public void setTitleRight(String tv_right) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title_right);
        if (textView != null) textView.setText(tv_right);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        int pos = absListView.getLastVisiblePosition();
        try {
            MyWord e = sList.get(pos);
            if (e == sList.get(sList.size() - 1)) {
                loadDataFrom();

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    //刷新数据的方法
    public void loadDataFrom() {
        getMywordList();
    }
}

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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_MywordAdapter;
import klsd.kuangkuang.main.BaseActivity;
import klsd.kuangkuang.main.C_ReleaseWordActivity;
import klsd.kuangkuang.main.LoginActivity;
import klsd.kuangkuang.main.M_MyCollectActivity;
import klsd.kuangkuang.main.M_SetActivity;
import klsd.kuangkuang.models.MyWord;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 我
 */
public class MMeFragment extends MyBaseFragment implements View.OnClickListener, AbsListView.OnScrollListener {
    View view;
    private List<MyWord> cirList;
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataCenter.isSigned()){
            initView();
        }else {
            myStartActivity(new Intent(a,LoginActivity.class));
        }
    }

    /**
     * 假数据
     *
     * @return
     */
    private List<MyWord> getSubjectList() {
        cirList = new ArrayList<MyWord>();
//        for (int i = 0; i < 5; i++) {
        MyWord sub = new MyWord(a);
        sub.setDay("10");
        sub.setContent("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……");
        sub.setMonth("7");
        sub.setBitmip(BitmapFactory.decodeResource(a.getResources(), R.mipmap.m31));
        cirList.add(sub);

        MyWord sub2 = new MyWord(a);
        sub2.setDay("12");
        sub2.setContent("    昨天老公给买了个大大的钻戒，但他不让我晒");
        sub2.setMonth("6");
        sub2.setBitmip(BitmapFactory.decodeResource(a.getResources(), R.mipmap.m53));
        cirList.add(sub2);
        MyWord sub3 = new MyWord(a);
        sub3.setDay("01");
        sub3.setContent("   谁说我自己不会鉴赏珠宝，我今天就要让你们都看看");
        sub3.setMonth("6");
        sub3.setBitmip(BitmapFactory.decodeResource(a.getResources(), R.mipmap.m32));
        cirList.add(sub3);
        MyWord sub4 = new MyWord(a);
        sub4.setDay("26");
        sub4.setContent(" 今天过生日，让我最想不到的是，我的好弟弟给我买了个金项链");
        sub4.setMonth("5");
        sub4.setBitmip(BitmapFactory.decodeResource(a.getResources(), R.mipmap.m51));
        cirList.add(sub4);
        MyWord sub5 = new MyWord(a);
        sub5.setDay("30");
        sub5.setContent(" 好吧，我觉得我还是得相信我自己的感觉，毕竟这是自己的爱好");
        sub5.setMonth("4");
        sub5.setBitmip(BitmapFactory.decodeResource(a.getResources(), R.mipmap.m21));
        cirList.add(sub5);
//        }
        return cirList;
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
//        getMyWordList();
        getSubjectList();
        mywordAdapter = new M_MywordAdapter(a, cirList);
        listView.setAdapter(mywordAdapter);
    }

    MyHTTP http;

    private void getMyWordList() {
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
                    ArrayList<MyWord> os = (ArrayList<MyWord>) bundle.getSerializable("MyWord_list");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    addTrades("bottom",os);
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
    public void addTrades(String from, List<MyWord> ess) {
        List<String> ids = new ArrayList<String>();
        for (MyWord o : sList)
            ids.add(o.getId());

        for (MyWord e : ess) {
            if (!ids.contains(e.getId())) {
                int i = from.equals("top") ? 0 : sList.size();
                sList.add(i, e);
            }
        }
        if (mywordAdapter != null) {
            mywordAdapter.notifyDataSetChanged();
        }
    }
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
        getMyWordList();
    }
}

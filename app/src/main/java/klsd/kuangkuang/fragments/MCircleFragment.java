package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import static klsd.kuangkuang.R.mipmap.touxiang01;

/**
 * 圈子
 */
public class MCircleFragment extends MyBaseFragment implements View.OnClickListener,AbsListView.OnScrollListener {
    View view;
    private static Activity a;
    private List<Circles> cirList;
    private C_CircleAdapter cAdapter;

    private ListView listView;
    private TextView tv_release;
    private SwipeRefreshLayout swipeView;
    private int limit = 5;
    private int page = 1;
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
    /**
     * 数据初始化
     */
    private void initView() {
        cirList=new ArrayList<>();
        tv_release = (TextView) view.findViewById(R.id.tv_title_circle_right);
        tv_release.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.listview_circle);
        listView.setOnScrollListener(this);
//        getSubjectList();
        UIutils.showLoading(a);
        swipt();
        getcircleList();
//        cAdapter = new C_CircleAdapter(a, cirList);
//        listView.setAdapter(cAdapter);

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

    /**
     * 得到朋友圈列表
     */
    private void getcircleList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", page+"");
        params.addQueryStringParameter("limit", limit+"");
        params = KelaParams.generateSignParam("GET", Consts.micropostsListApi, params);
        if (http == null) http = new MyHTTP(a);
        http.baseRequest(Consts.micropostsListApi, JSONHandler.JTYPE_CIRCLE_LIST, HttpRequest.HttpMethod.GET,
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
                if (jtype.equals(JSONHandler.JTYPE_CIRCLE_LIST)) {
                    int curTradesSize = cirList.size();
                    ArrayList<Circles> os = (ArrayList<Circles>) bundle.getSerializable("circle_list");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        UIutils.cancelLoading();
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    addTrades("bottom",os);
                    if (curTradesSize == 0) {
                        cirList = os;
                        cAdapter = new C_CircleAdapter(a, cirList,handler);
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
    public void swipt() {
        swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swip_circle);
        swipeView.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {                 //此方法是刷新

                swipeView.setRefreshing(true);
                loadDataFrom("top");
            }
        });
    }
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

    //刷新数据的方法
    public void loadDataFrom(String from) {
        if (from.equals("bottom")) {
            getcircleList();
        }else {
            cirList = new ArrayList<Circles>();
            page = 1;
            getcircleList();
        }
    }
    /**
     * 假数据
     *
     * @return
     */
    private Bitmap bit_head;

    private List<Circles> getSubjectList() {
        cirList = new ArrayList<Circles>();
//        for (int i = 0; i < 5; i++) {
        Circles sub = new Circles(a);
        sub.setContent_son("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……");
        sub.setNickname("当年明月");
        sub.setLike("15");
        sub.setComment("3");
        sub.setCreated_at("发布于1小时前");
        sub.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang01));
        cirList.add(sub);

        Circles sub2 = new Circles(a);
        sub2.setContent_son("   连柱子都要镀金，连椅子都要镶钻!那么问题来了，究竟是怎么样的女子才能嫁给如此富得流油的文莱王子……");
        sub2.setNickname("刘备");
        sub2.setLike("20");
        sub2.setComment("10");
        sub2.setCreated_at("发布于8小时前");
//        sub2.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub2.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang02));
        cirList.add(sub2);
        Circles sub3 = new Circles(a);
        sub3.setContent_son("    那还是在旅游旺季。她试了各种办法，比如把它们移到中间的展示区……");
        sub3.setNickname("丽莎");
        sub3.setLike("15");
        sub3.setComment("6");
        sub3.setCreated_at("发布于1天前");
//        sub3.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub3.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang03));
        cirList.add(sub3);
        Circles sub4 = new Circles(a);
        sub4.setContent_son("    很多电影中都会有偷盗珠宝的情节，那些动辄上亿的珠宝引无数大盗竞折腰，上天入地无所不能，荧屏外的观众也看的够爽……");
        sub4.setNickname("徐小明");
        sub4.setLike("5");
        sub4.setComment("1");
        sub4.setCreated_at("发布于两天前");
//        sub4.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub4.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang04));
        cirList.add(sub4);
        Circles sub5 = new Circles(a);
        sub5.setContent_son("    有一个把商品价格提高反而增加销量的故事：一批绿松石珠宝，眼看就要砸在一位女店主手里了……");
        sub5.setNickname("一阵风");
        sub5.setLike("13");
        sub5.setComment("9");
        sub5.setCreated_at("发布于4天前");
//        sub5.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub5.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang05));
        cirList.add(sub5);
//        }
        return cirList;
    }

    public boolean isSigned() {
        return DataCenter.isSigned();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_circle_right:
                if(isSigned()){
                    myStartActivity(new Intent(getActivity(), C_ReleaseWordActivity.class));
                }else{
                    ToastUtil.show(a,"您未登录");
                }

                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        int pos = absListView.getLastVisiblePosition();
        try {
            Circles e = cirList.get(pos);
            if (e == cirList.get(cirList.size() - 1)) {
                loadDataFrom("bottom");

            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}

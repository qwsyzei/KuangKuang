package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.C_CircleAdapter;
import klsd.kuangkuang.main.C_ReleaseWordActivity;
import klsd.kuangkuang.models.Circles;
import klsd.kuangkuang.utils.DataCenter;

import static klsd.kuangkuang.R.mipmap.touxiang01;

/**
 * 圈子
 */
public class MCircleFragment extends MyBaseFragment implements View.OnClickListener {
    View view;
    private static Activity a;
    private List<Circles> cirList;
    private C_CircleAdapter cAdapter;

    private ListView listView;
    private TextView tv_release;

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
//        if (isSigned()) {
        initView();
//        } else {
//          myStartActivity(new Intent(getActivity(), LoginActivity.class));
//        }

        return view;
    }

    public void setTitle(String title) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }

    public void setTitleRight(String tv_right) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title_right);
        if (textView != null) textView.setText(tv_right);
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
        Circles sub = new Circles(getContext());
        sub.setTitle("钻石趣闻");
        sub.setDescribe("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……");
        sub.setAuthor("当年明月");
        sub.setViews("22");
        sub.setLike("15");
        sub.setComment("3");
        sub.setCreated_at("发布于1小时前");
        sub.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang01));
        cirList.add(sub);

        Circles sub2 = new Circles(getContext());
        sub2.setTitle("男士珠宝");
        sub2.setDescribe("   连柱子都要镀金，连椅子都要镶钻!那么问题来了，究竟是怎么样的女子才能嫁给如此富得流油的文莱王子……");
        sub2.setAuthor("刘备");
        sub2.setViews("82");
        sub2.setLike("20");
        sub2.setComment("10");
        sub2.setCreated_at("发布于8小时前");
//        sub2.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub2.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang02));
        cirList.add(sub2);
        Circles sub3 = new Circles(getContext());
        sub3.setTitle("羊年限量");
        sub3.setDescribe("    那还是在旅游旺季。她试了各种办法，比如把它们移到中间的展示区……");
        sub3.setAuthor("丽莎");
        sub3.setViews("30");
        sub3.setLike("15");
        sub3.setComment("6");
        sub3.setCreated_at("发布于1天前");
//        sub3.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub3.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang03));
        cirList.add(sub3);
        Circles sub4 = new Circles(getContext());
        sub4.setTitle("如何搭配");
        sub4.setDescribe("    很多电影中都会有偷盗珠宝的情节，那些动辄上亿的珠宝引无数大盗竞折腰，上天入地无所不能，荧屏外的观众也看的够爽……");
        sub4.setAuthor("徐小明");
        sub4.setViews("10");
        sub4.setLike("5");
        sub4.setComment("1");
        sub4.setCreated_at("发布于两天前");
//        sub4.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        sub4.setHead(BitmapFactory.decodeResource(a.getResources(), R.mipmap.touxiang04));
        cirList.add(sub4);
        Circles sub5 = new Circles(getContext());
        sub5.setTitle("最大祖母绿");
        sub5.setDescribe("    有一个把商品价格提高反而增加销量的故事：一批绿松石珠宝，眼看就要砸在一位女店主手里了……");
        sub5.setAuthor("一阵风");
        sub5.setViews("33");
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

    /**
     * 数据初始化
     */
    private void initView() {
        tv_release = (TextView) view.findViewById(R.id.tv_title_circle_right);
        tv_release.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.listview_circle);
        getSubjectList();
        cAdapter = new C_CircleAdapter(getContext(), cirList);

        listView.setAdapter(cAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_circle_right:
                myStartActivity(new Intent(getActivity(), C_ReleaseWordActivity.class));
                break;
        }
    }
}

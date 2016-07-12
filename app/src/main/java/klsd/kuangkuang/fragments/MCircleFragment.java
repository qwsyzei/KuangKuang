package klsd.kuangkuang.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.C_CircleAdapter;
import klsd.kuangkuang.adapters.C_CircleGridAdapter;
import klsd.kuangkuang.main.LoginActivity;
import klsd.kuangkuang.models.Circle;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.views.SelfGridView;

/**
 * 圈子
 */
public class MCircleFragment extends MyBaseFragment {
    View view;

    private List<Circle> cirList;
    private C_CircleAdapter cAdapter;

    private ListView listView;


    public MCircleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    private List<Circle> getSubjectList() {
        cirList = new ArrayList<Circle>();
        for (int i = 0; i < 5; i++) {
            Circle sub = new Circle(getContext());
            sub.setTitle("钻石趣闻" + i);
            sub.setDescribe("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……" + i);
            sub.setAuthor("刘备" + i);
            sub.setViews("22");
            sub.setLike("15");
            sub.setComment("3");
            sub.setHead_pic_url("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
            cirList.add(sub);

        }
        return cirList;
    }
    public boolean isSigned() {
        return DataCenter.isSigned();
    }
    /**
     * 数据初始化
     */
    private void initView() {

        listView = (ListView) view.findViewById(R.id.listview_circle);
        getSubjectList();
        cAdapter = new C_CircleAdapter(getContext(), cirList);

        listView.setAdapter(cAdapter);

    }
}

package yksg.kuangkuang.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.T_CalculatorActivity;
import yksg.kuangkuang.main.T_CertificateActivity;
import yksg.kuangkuang.utils.ToastUtil;

/**
 * 工具
 */
public class MToolFragment extends MyBaseFragment implements View.OnClickListener {
    View view;
    private ImageView im01, im02, im03, im04;

    public MToolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mtool, container, false);
        setTitle(getString(R.string.main_tool));
        initView();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
//        ToastUtil.show(getActivity(), "我是工具");
    }
    private void initView() {
        im01 = (ImageView) view.findViewById(R.id.im_mytool01);
        im02 = (ImageView) view.findViewById(R.id.im_mytool02);
        im03 = (ImageView) view.findViewById(R.id.im_mytool03);
        im04 = (ImageView) view.findViewById(R.id.im_mytool04);
        im01.setOnClickListener(this);
        im02.setOnClickListener(this);
        im03.setOnClickListener(this);
        im04.setOnClickListener(this);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_mytool01:
                myStartActivity(new Intent(getActivity(), T_CertificateActivity.class));
                break;
            case R.id.im_mytool02:

            case R.id.im_mytool04:
                ToastUtil.show(getActivity(), getString(R.string.please_wait));
                break;
            case R.id.im_mytool03:
                myStartActivity(new Intent(getActivity(), T_CalculatorActivity.class));
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("工具工具没了", "onDestroy() returned: " + "");
    }
}

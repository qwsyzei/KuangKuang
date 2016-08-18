package klsd.kuangkuang.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.utils.ToastUtil;

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
        ToastUtil.show(getActivity(), "我是工具");
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
            case R.id.im_mytool02:
            case R.id.im_mytool03:
            case R.id.im_mytool04:
                ToastUtil.show(getActivity(), getString(R.string.please_wait));
                break;
        }
    }
}

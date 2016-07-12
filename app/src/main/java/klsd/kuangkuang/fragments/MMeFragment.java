package klsd.kuangkuang.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.M_MyCollectActivity;
import klsd.kuangkuang.main.M_SetActivity;

/**
 * 我
 */
public class MMeFragment extends MyBaseFragment implements View.OnClickListener {
    View view;
    private ImageView im_set;
    private TextView tv_four;

    public MMeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        setTitle(getString(R.string.main_me));
        initView();
        return view;
    }

    private void initView() {
        im_set = (ImageView) view.findViewById(R.id.im_title_set);
        tv_four = (TextView) view.findViewById(R.id.me_four);
        tv_four.setOnClickListener(this);
        im_set.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_title_set:
                myStartActivity(new Intent(getActivity(), M_SetActivity.class));
                break;
            case R.id.me_four:
                myStartActivity(new Intent(getActivity(), M_MyCollectActivity.class));
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
}

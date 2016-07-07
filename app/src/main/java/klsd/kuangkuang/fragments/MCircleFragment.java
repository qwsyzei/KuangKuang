package klsd.kuangkuang.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import klsd.kuangkuang.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MCircleFragment extends MyBaseFragment {
View view;

    public MCircleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
 view=inflater.inflate(R.layout.fragment_mcircle, container, false);
        setTitle(getString(R.string.main_circle));
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
}

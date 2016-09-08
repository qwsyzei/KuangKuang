package yksg.kuangkuang.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yksg.kuangkuang.R;

/**
 * 我的基础frament
 */
public class MyBaseFragment extends Fragment {

View view;
    public MyBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view;
    }
    @SuppressLint("NewApi")
    public void aniBack() {
        getActivity().overridePendingTransition(R.anim.backin, R.anim.backout);
    }

    @SuppressLint("NewApi")
    public void aniStart() {
        getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
    public void myStartActivity(Intent intent) {
        getActivity().startActivity(intent);
        aniStart();
    }

    public void myStartActivityForResult(Intent intent, int code) {
        getActivity().startActivityForResult(intent, code);
        aniStart();
    }
    public void back() {
        getActivity().finish();
        aniBack();
    }

}

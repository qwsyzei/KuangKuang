package yksg.kuangkuang.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.common.CommonUtils;

/**
 * 我的基础frament
 */
public class MyBaseFragment extends Fragment {

    View view;
    public boolean isNetWork;
    public MyBaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConnectivityManager connectionManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            isNetWork=true;
        } else {
            isNetWork=false;
        }
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

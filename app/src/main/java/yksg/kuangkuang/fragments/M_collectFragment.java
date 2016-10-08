package yksg.kuangkuang.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import yksg.kuangkuang.R;
import yksg.kuangkuang.adapters.M_MyCollectAdapter;
import yksg.kuangkuang.main.BaseActivity;
import yksg.kuangkuang.models.MyCollect;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.SelfGridView;

/**
 * 收藏
 */
public class M_collectFragment extends MyBaseFragment {
    private M_MyCollectAdapter mywordAdapter;
    private ArrayList<MyCollect> sList;
    private SelfGridView gridview;
    private int page = 1;
    MyHTTP http;
    View view;
    private int flag=0;
    public M_collectFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_mycollect, container, false);
        initView();
        return view;
    }

    private void initView() {
        IntentFilter filter = new IntentFilter(MMeFragment.action_mycollect);
        getContext().registerReceiver(broadcastReceiver, filter);
        sList=new ArrayList<>();
        gridview= (SelfGridView) view.findViewById(R.id.gridview_fragment_mycollect);
        gridview.setFocusable(false);
        getCollectShowList123();
    }

    /**
     * 获取收藏列表
     */
    private void getCollectShowList123() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", "8");

        if (http == null) http = new MyHTTP(getActivity());
        http.baseRequest(Consts.articlesCollectShowApi, JSONHandler.JTYPE_COLLECT_SHOW, HttpRequest.HttpMethod.GET,
                params, handler);

    }
    private Handler handler = new BaseActivity.KelaHandler(getActivity()) {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            Bitmap bitmap = bundle.getParcelable("bitmap");
            String res = bundle.getString("result");
            String jtype = bundle.getString("jtype");

            if (res == null) {
                ToastUtil.show(getActivity(), getString(R.string.network_problem));
            } else if (res.equals("OK")) {
                if (jtype.equals(JSONHandler.JTYPE_COLLECT_SHOW)) {
                    int curTradesSize = sList.size();
                    ArrayList<MyCollect> os = (ArrayList<MyCollect>) bundle.getSerializable("collect_show");
                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        if (flag==1){
                            ToastUtil.show(getActivity(),getString(R.string.no_more_data));
                        }
//                        return;
                    }
                    else {
                        addTrades("bottom", os);
                        if (curTradesSize == 0) {
                            sList = os;
                            mywordAdapter = new M_MyCollectAdapter(getActivity(),sList, handler);
                            gridview.setAdapter(mywordAdapter);
                        } else {
                            mywordAdapter.notifyDataSetChanged();
                        }
                        flag=1;
                        page += 1;

                    }
                }
            } else {
                ToastUtil.show(getActivity(), res);
            }
        }
    };

    public void addTrades(String from, List<MyCollect> ess) {
        List<String> ids = new ArrayList<String>();
        for (MyCollect o : sList)
            ids.add(o.getId());

        for (MyCollect e : ess) {
            if (!ids.contains(e.getId())) {
                int i = from.equals("top") ? 0 : sList.size();
                sList.add(i, e);
            }
        }
        if (mywordAdapter != null) {
            mywordAdapter.notifyDataSetChanged();
        }
    }
    BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            getCollectShowList123();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}

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
import yksg.kuangkuang.adapters.M_MyWordsAdapter;
import yksg.kuangkuang.main.BaseActivity;
import yksg.kuangkuang.models.MyWord;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.DataCenter;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.SelfGridView;

/**
 * 说说
 */
public class M_wordFragment extends MyBaseFragment {
    private M_MyWordsAdapter mywordAdapter;
    private ArrayList<MyWord> sList;
    private SelfGridView gridview;
    private int limit = 8;
    private int page = 1;
    MyHTTP http;
    View view;
    private int flag=0;
    public M_wordFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_myword, container, false);
        initView();
        return view;
    }

    private void initView() {
        IntentFilter filter = new IntentFilter(MMeFragment.action_myword);
        getContext().registerReceiver(broadcastReceiver, filter);
        sList=new ArrayList<>();
        gridview= (SelfGridView) view.findViewById(R.id.gridview_fragment_myword);
        gridview.setFocusable(false);
        getMyWordList();
    }

    private   void getMyWordList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("limit", limit + "");
        params.addQueryStringParameter("page", page + "");
        if (http == null) http = new MyHTTP(getActivity());
        http.baseRequest(Consts.micropostsMemberListApi, JSONHandler.JTYPE_MYWORD_LIST, HttpRequest.HttpMethod.GET,
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
                if (jtype.equals(JSONHandler.JTYPE_MYWORD_LIST)) {
                    int curTradesSize = sList.size();
                    ArrayList<MyWord> os = (ArrayList<MyWord>) bundle.getSerializable("myword_list");
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
                            mywordAdapter = new M_MyWordsAdapter(getActivity(),sList, handler);
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

    public void addTrades(String from, List<MyWord> ess) {
        List<String> ids = new ArrayList<String>();
        for (MyWord o : sList)
            ids.add(o.getId());

        for (MyWord e : ess) {
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
            getMyWordList();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}

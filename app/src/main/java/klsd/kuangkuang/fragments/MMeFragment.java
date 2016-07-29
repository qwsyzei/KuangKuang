package klsd.kuangkuang.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_MywordAdapter;
import klsd.kuangkuang.main.BaseActivity;
import klsd.kuangkuang.main.C_ReleaseWordActivity;
import klsd.kuangkuang.main.LoginActivity;
import klsd.kuangkuang.main.M_MyCollectActivity;
import klsd.kuangkuang.main.M_SetActivity;
import klsd.kuangkuang.models.Documents;
import klsd.kuangkuang.models.MyWord;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.PullToRefresh123View;
import klsd.kuangkuang.views.SelfListView;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 我
 */
public class MMeFragment extends MyBaseFragment implements View.OnClickListener,PullToRefresh123View.OnFooterRefreshListener {
    View view;
    private List<MyWord> cirList;
    private M_MywordAdapter mywordAdapter;
    private ImageView im_set;
    private RelativeLayout layout_collect;
    private ArrayList<MyWord> sList;
    private SelfListView listView;
    private static Activity a;
    private int limit = 10;
    private int page = 1;
    MyHTTP http;
    private LinearLayout layout_release,layout_today;
    private Documents documents;
    private ImageView im_head_big, im_head_small;
private TextView tv_name,tv_signature;
    // 自定义的listview的上下拉动刷新
    private PullToRefresh123View mPullToRefreshView;
    public MMeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        a = this.getActivity();
        view = inflater.inflate(R.layout.fragment_me, container, false);
        Context context = getActivity().getApplicationContext();
        initImageLoader(context);
        setTitle(getString(R.string.main_me));
        if (DataCenter.isSigned()) {
            initView();
        } else {
            myStartActivity(new Intent(a, LoginActivity.class));
        }
        return view;
    }

    /**
     * 假数据
     *
     * @return
     */
    private List<MyWord> getSubjectList() {
        cirList = new ArrayList<MyWord>();
        MyWord sub = new MyWord(a);
        sub.setDay("10");
//        sub.setContent("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……");
        sub.setMonth("7");
//        sub.setBitmip(BitmapFactory.decodeResource(a.getResources(), R.mipmap.m31));
        cirList.add(sub);

        return cirList;
    }

    private void initView() {
        documents = new Documents();
        getData();
        sList = new ArrayList<>();
        im_head_big = (ImageView) view.findViewById(R.id.me_head_big);
        im_head_small = (ImageView) view.findViewById(R.id.me_head_small);
        im_set = (ImageView) view.findViewById(R.id.im_title_set);
        layout_collect = (RelativeLayout) view.findViewById(R.id.me_collect_layout);
        layout_release = (LinearLayout) view.findViewById(R.id.layout_me_release_word_now);
        layout_today= (LinearLayout) view.findViewById(R.id.layout_me_myword_today);
        listView = (SelfListView) view.findViewById(R.id.listview_me_myword);
        tv_name= (TextView) view.findViewById(R.id.me_nickname);
        tv_signature= (TextView) view.findViewById(R.id.me_signature);
        listView.setFocusable(false);
        layout_release.setOnClickListener(this);
        layout_collect.setOnClickListener(this);
        im_set.setOnClickListener(this);
        mPullToRefreshView= (PullToRefresh123View)view.findViewById(R.id.pull_refresh_view_me);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        mywordAdapter = new M_MywordAdapter(a, sList, handler);
        listView.setAdapter(mywordAdapter);
    }

    /**
     * 获取个人资料
     */
    private void getData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        if (http == null) http = new MyHTTP(a);
        http.baseRequest(Consts.memberDocumentsApi, JSONHandler.JTYPE_MEMBER_DOCUMENTS, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    private void getMyWordList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("limit", limit + "");
        params.addQueryStringParameter("page", page + "");
        if (http == null) http = new MyHTTP(a);
        http.baseRequest(Consts.micropostsMemberListApi, JSONHandler.JTYPE_MYWORD_LIST, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    private Handler handler = new BaseActivity.KelaHandler(a) {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            Bitmap bitmap = bundle.getParcelable("bitmap");
            String res = bundle.getString("result");
            String jtype = bundle.getString("jtype");

            if (res == null) {
                ToastUtil.show(a, getString(R.string.network_problem));
//                a.startActivity(new Intent(a, LoginActivity.class));
//                a.finish();
            } else if (res.equals("OK")) {
                if (jtype.equals(JSONHandler.JTYPE_MYWORD_LIST)) {
                    int curTradesSize = sList.size();
                    ArrayList<MyWord> os = (ArrayList<MyWord>) bundle.getSerializable("myword_list");
                    for (int i=0;i<os.size();i++){
                        if (os.get(i).get_the_time().equals(MyDate.todayDate())){
                            layout_today.setVisibility(View.GONE);
                            break;
                        }
                    }

                    Log.d("OS的长度", "handleMessage() returned: " + os.size());
                    if (os.size() == 0) {
                        ToastUtil.show(a, a.getString(R.string.no_more_data));
                        return;
                    }
                    addTrades("bottom", os);
                    if (curTradesSize == 0) {
                        sList = os;
                        mywordAdapter = new M_MywordAdapter(a, sList, handler);
                        listView.setAdapter(mywordAdapter);
                    } else {
                        mywordAdapter.notifyDataSetChanged();
                    }
                    page += 1;
                } else if (jtype.equals(JSONHandler.JTYPE_MEMBER_DOCUMENTS)) {
                    documents = (Documents) bundle.getSerializable("documents");
                    tv_name.setText(documents.getName());
                    tv_signature.setText(documents.getSignature());
                    ToastUtil.show(a, "已获取到个人资料");
                    getbitmap123();
                loadDataFrom();
                }
            } else if (res.equals("123")) {
                if (!documents.getPicture().equals("null")) {
                    ImageLoader.getInstance().displayImage(Consts.host + "/" + documents.getPicture(), im_head_small);
                    Bitmap bitmap1 = fastblur(a, bitmap, 5, false);
                    im_head_big.setImageBitmap(bitmap1);
                }
            } else {
                ToastUtil.show(a, res);
            }
        }
    };

    /**
     * 开启子线程来获取网络图片
     */
    private void getbitmap123() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = ImageLoader.getInstance().loadImageSync(Consts.host + "/" + documents.getPicture());
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", "123");
                bundle.putParcelable("bitmap", bitmap1);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_title_set:
                myStartActivity(new Intent(getActivity(), M_SetActivity.class));
                break;
            case R.id.me_collect_layout:
                myStartActivity(new Intent(getActivity(), M_MyCollectActivity.class));
                break;
            case R.id.layout_me_release_word_now:
                myStartActivity(new Intent(getActivity(), C_ReleaseWordActivity.class));
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



    //刷新数据的方法
    public void loadDataFrom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMyWordList();
            }
        }).start();

    }

    /**
     * 模糊效果
     *
     * @param context
     * @param sentBitmap       需要的图片
     * @param radius           模糊效果大小
     * @param canReuseInBitmap 位图是否可以重复使用
     * @return
     */
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];

        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.d("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    @Override
    public void onFooterRefresh(PullToRefresh123View view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();
                loadDataFrom();
                ToastUtil.show(a, "加载更多数据!");
            }
        }, 3000);
    }

}

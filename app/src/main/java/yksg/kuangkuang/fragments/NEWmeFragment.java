package yksg.kuangkuang.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.BaseActivity;
import yksg.kuangkuang.main.C_ReleaseWordActivity;
import yksg.kuangkuang.main.LoginActivity;
import yksg.kuangkuang.main.M_BlackListActivity;
import yksg.kuangkuang.main.M_FansListActivity;
import yksg.kuangkuang.main.M_FollowListActivity;
import yksg.kuangkuang.main.M_PersonalDataActivity;
import yksg.kuangkuang.main.M_SetActivity;
import yksg.kuangkuang.models.Documents;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.DataCenter;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.ObservableScrollView;
import yksg.kuangkuang.views.PullToRefresh123View;

import static yksg.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 我
 */
public class NEWmeFragment extends MyBaseFragment implements View.OnClickListener, PullToRefresh123View.OnFooterRefreshListener, RadioGroup.OnCheckedChangeListener {
    View view;
    private ImageView im_set;
    private RelativeLayout layout_blacklist;
    private static Activity a;
    MyHTTP http;
    private Documents documents;
    private ImageView im_head_big, im_head_small;
    private TextView tv_name, tv_signature;
    private TextView tv_fans, tv_follows;
    private LinearLayout layout_fans, layout_follows;
    // 自定义的listview的上下拉动刷新
    private PullToRefresh123View mPullToRefreshView;
    int flag = 0;
    RadioGroup radioGroup;
    RadioButton rbA;
    private int flag_no;
    private M_wordFragment wordFragment;
    private M_collectFragment collectFragment;
    public static final String action_myword = "myword.broadcast.action";
    public static final String action_mycollect = "mycollect.broadcast.action";
    private RelativeLayout layoutHead;
    private ObservableScrollView scrollView;
    private LinearLayout layout_zhan;//占位用的布局
    private int height;

    public NEWmeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        a = this.getActivity();
        view = inflater.inflate(R.layout.fragment_me_new, container, false);
        Context context = getActivity().getApplicationContext();
        initImageLoader(context);
        setTitle(getString(R.string.main_me));
        if (DataCenter.isSigned()) {
            initView();
        } else {
            myStartActivity(new Intent(a, LoginActivity.class));
            a.finish();
        }
        return view;
    }

    private void initView() {
        scrollView = (ObservableScrollView) view.findViewById(R.id.scrollview);
        layoutHead = (RelativeLayout) view.findViewById(R.id.title_RelativeLayout);
        layout_zhan = (LinearLayout) view.findViewById(R.id.layout_zhanwei);
        //获取顶部图片高度后，设置滚动监听
        ViewTreeObserver vto = layout_zhan.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout_zhan.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = layout_zhan.getHeight();

                scrollView.setScrollViewListener(scrollViewListener);
            }
        });
        documents = new Documents();
        radioGroup = (RadioGroup) view.findViewById(R.id.me_radiogroup);
        rbA = (RadioButton) view.findViewById(R.id.me_rb1);
        radioGroup.setOnCheckedChangeListener(this);
        showFragment(1);
        rbA.setChecked(true);
        im_head_big = (ImageView) view.findViewById(R.id.me_head_big);
        im_head_small = (ImageView) view.findViewById(R.id.me_head_small);
        im_set = (ImageView) view.findViewById(R.id.im_title_set);
        layout_blacklist = (RelativeLayout) view.findViewById(R.id.me_black_layout);

        tv_name = (TextView) view.findViewById(R.id.me_nickname);
        tv_signature = (TextView) view.findViewById(R.id.me_signature);
        tv_fans = (TextView) view.findViewById(R.id.me_fans_tv);
        tv_follows = (TextView) view.findViewById(R.id.me_follows_tv);
        layout_fans = (LinearLayout) view.findViewById(R.id.layout_me_top_fans);
        layout_follows = (LinearLayout) view.findViewById(R.id.layout_me_top_follows);

        layout_blacklist.setOnClickListener(this);
        im_head_small.setOnClickListener(this);
        layout_fans.setOnClickListener(this);
        layout_follows.setOnClickListener(this);
        im_set.setOnClickListener(this);
        mPullToRefreshView = (PullToRefresh123View) view.findViewById(R.id.pull_refresh_view_me);
        mPullToRefreshView.setOnFooterRefreshListener(this);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag == 1) {
            getData();
        }
    }

    private ObservableScrollView.ScrollViewListener scrollViewListener = new ObservableScrollView.ScrollViewListener() {

        @Override
        public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                    int oldx, int oldy) {
            //当向上滑动距离大于占位布局的高度值，就调整标题的背景
            if (y > height) {
                float alpha = (128);//0~255    完全透明~不透明

                //4个参数，第一个是透明度，后三个是红绿蓝三元色参数
                layoutHead.setBackgroundColor(Color.argb((int) alpha, 0, 0, 0));
            } else {
                layoutHead.setBackgroundColor(Color.BLACK);
            }

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_title_set:
                myStartActivity(new Intent(getActivity(), M_SetActivity.class));
                break;
            case R.id.me_black_layout:
                myStartActivity(new Intent(getActivity(), M_BlackListActivity.class));
                break;
            case R.id.layout_me_release_word_now:
                myStartActivity(new Intent(getActivity(), C_ReleaseWordActivity.class));
                break;
            case R.id.me_head_small:
                ConnectivityManager connectionManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                //当网络开关没开时点击头像不能进入个人资料界面
                if (networkInfo != null && networkInfo.isAvailable()) {
                    myStartActivity(new Intent(getActivity(), M_PersonalDataActivity.class));
                } else {
                    ToastUtil.show(getActivity(), getString(R.string.cannot_enter_info));
                }
                break;
            case R.id.layout_me_top_fans:
                myStartActivity(new Intent(getActivity(), M_FansListActivity.class));
                break;
            case R.id.layout_me_top_follows:
                myStartActivity(new Intent(getActivity(), M_FollowListActivity.class));
                break;
        }
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

    private Handler handler = new BaseActivity.KelaHandler(a) {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            Bitmap bitmap = bundle.getParcelable("bitmap");
            String res = bundle.getString("result");
            String jtype = bundle.getString("jtype");

            if (res == null) {
                ToastUtil.show(a, getString(R.string.network_problem));
            } else if (res.equals("OK")) {
                if (jtype.equals(JSONHandler.JTYPE_MEMBER_DOCUMENTS)) {
                    flag = 1;
                    documents = (Documents) bundle.getSerializable("documents");
                    tv_name.setText(documents.getName());
                    if (documents.getSignature().equals("null")) {
                        tv_signature.setText(getString(R.string.not_too_lazy));
                    } else {
                        tv_signature.setText(documents.getSignature());
                    }
                    if (documents.getFollow_number().contains(".0")) {
                        tv_follows.setText(documents.getFollow_number().replace(".0", ""));
                    } else {
                        tv_follows.setText(documents.getFollow_number());
                    }
                    if (documents.getFollowed_number().contains(".0")) {
                        tv_fans.setText(documents.getFollowed_number().replace(".0", ""));
                    } else {
                        tv_fans.setText(documents.getFollowed_number());
                    }
                    getbitmap123();
                }
            } else if (res.equals("123")) {
                if (!documents.getPicture().equals("null") && !documents.getPicture().equals("uploads/head_portrait")) {
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

    public void setTitle(String title) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }

    public void setTitleRight(String tv_right) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title_right);
        if (textView != null) textView.setText(tv_right);
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
                Log.d("发什么广播", "run() returned: " + flag_no + "");
                Intent intent;
                if (flag_no == 1) {
                    intent = new Intent(action_myword);
                } else {
                    intent = new Intent(action_mycollect);
                }
                a.sendBroadcast(intent);
                ToastUtil.show(a, getString(R.string.load_more));
            }
        }, 2800);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("我的我的被关闭了", "onDestroy() returned: " + "");
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.me_rb1:
                showFragment(1);
                flag_no = 1;
                break;
            case R.id.me_rb2:
                showFragment(2);
                flag_no = 2;
                break;

        }
    }

    public void showFragment(int index) {
        FragmentManager fm = getFragmentManager();  //获得Fragment管理器
        FragmentTransaction ft = fm.beginTransaction(); //开启一个事务
        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (index) {
            case 1:
                // 如果fragment1已经存在则将其显示出来
                if (wordFragment != null)
                    ft.show(wordFragment);
                    // 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    wordFragment = new M_wordFragment();
                    ft.add(R.id.just_me_layout, wordFragment);
                }
                break;
            case 2:
                if (collectFragment != null) {
                    //先清除再添加，目的在于每次都用新的收藏列表
                    ft.remove(collectFragment);
                    collectFragment = new M_collectFragment();
                    ft.add(R.id.just_me_layout, collectFragment);
                } else {
                    collectFragment = new M_collectFragment();
                    ft.add(R.id.just_me_layout, collectFragment);
                }
                break;

        }
        ft.commit();
    }

    // 当fragment已被实例化，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (wordFragment != null)
            ft.hide(wordFragment);
        if (collectFragment != null)
            ft.hide(collectFragment);
    }
}

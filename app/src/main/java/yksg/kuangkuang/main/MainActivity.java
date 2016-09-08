package yksg.kuangkuang.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import yksg.kuangkuang.R;
import yksg.kuangkuang.fragments.LeftFragment;
import yksg.kuangkuang.fragments.MCircleFragment;
import yksg.kuangkuang.fragments.MMeFragment;
import yksg.kuangkuang.fragments.MSubjectFragment;
import yksg.kuangkuang.fragments.MToolFragment;
import yksg.kuangkuang.utils.DataCenter;

/**
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private ImageView im_title_left;//标题左上方的图标
    RadioGroup radioGroup;

    private RadioButton rbA, rbB, rbC, rbD;
    private MSubjectFragment mSubjectFragment;
    private MCircleFragment mCircleFragment;
    private MToolFragment mToolFragment;
    private MMeFragment mMeFragment;
    private LinearLayout layout_main_layout;
    String str = "0";
    String sub_key="0";
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initView();
        initSlidingMenu();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void initView() {
        IntentFilter filter = new IntentFilter(BaseActivity.action);
        registerReceiver(broadcastReceiver, filter);
        Intent intent = getIntent();
        str = intent.getStringExtra("goto");

        sub_key=intent.getStringExtra("subject");
        if (sub_key==null){
            sub_key="0";
        }
        im_title_left = (ImageView) findViewById(R.id.im_more_subject);

        layout_main_layout = (LinearLayout) findViewById(R.id.layout_main_layout);
        rbA = (RadioButton) findViewById(R.id.main_rb1);
        rbB = (RadioButton) findViewById(R.id.main_rb2);
        rbC = (RadioButton) findViewById(R.id.main_rb3);
        rbD = (RadioButton) findViewById(R.id.main_rb4);
        im_title_left.setOnClickListener(this);

        /**
         * 应用进入后，默认选择点击Fragment01
         */
        if (str != null && str.equals("me")) {
            showFragment(4,sub_key);
            rbD.setChecked(true);
        }else if(str != null && str.equals("circle")){
            showFragment(2,sub_key);
            rbB.setChecked(true);
        } else {
            showFragment(1,sub_key);
            rbA.setChecked(true);
        }

        radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
    }



    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu() {

//        if (mContent == null) {
//            mContent = new MSubjectFragment("0");
//        }
        // 设置左侧滑动菜单
        setBehindContentView(R.layout.menu_frame_left);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commit();
        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 关闭手势滑动
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //设置某个控件不执行触摸滑动事件
        sm.addIgnoredView(layout_main_layout);//在这里设置整个布局都不能滑动
        // 设置可以左右滑动的菜单
        sm.setMode(SlidingMenu.LEFT);
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单阴影的图像资源
        sm.setShadowDrawable(null);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式,这里设置为全屏
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置下方视图的在滚动时的缩放比例
        sm.setBehindScrollScale(0.0f);

    }

    /**
     * radiobutton 改变选中状态
     * 进行切换fragment
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.main_rb1:
                im_title_left.setVisibility(View.VISIBLE);
                showFragment(1,sub_key);
                break;
            case R.id.main_rb2:
                im_title_left.setVisibility(View.GONE);
                showFragment(2,sub_key);

                break;
            case R.id.main_rb3:
                im_title_left.setVisibility(View.GONE);
                showFragment(3,sub_key);

                break;
            case R.id.main_rb4:
                if (DataCenter.isSigned()) {
                    im_title_left.setVisibility(View.GONE);
                    showFragment(4,sub_key);

                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }

    }

    public  void showFragment(int index,String subject_key) {
        FragmentManager fm = getSupportFragmentManager();  //获得Fragment管理器
        FragmentTransaction ft = fm.beginTransaction(); //开启一个事务

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (index) {
            case 1:
                // 如果fragment1已经存在则将其显示出来
                if (mSubjectFragment != null)
                    ft.show(mSubjectFragment);
                    // 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    mSubjectFragment = new MSubjectFragment(subject_key);
                    ft.add(R.id.just_subject_layout, mSubjectFragment);
                }
                break;
            case 2:
                if (mCircleFragment != null)
                    ft.show(mCircleFragment);
                else {
                    mCircleFragment = new MCircleFragment();
                    ft.add(R.id.just_subject_layout, mCircleFragment);
                }
                break;
            case 3:
                if (mToolFragment != null)
                    ft.show(mToolFragment);
                else {
                    mToolFragment = new MToolFragment();
                    ft.add(R.id.just_subject_layout, mToolFragment);
                }
                break;
            case 4:
                if (mMeFragment != null)
                    ft.show(mMeFragment);
                else {
                    mMeFragment = new MMeFragment();
                    ft.add(R.id.just_subject_layout, mMeFragment);
                }
                break;
        }
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_more_subject:
                toggle();
                break;

        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {

            } else {
                Toast.makeText(context, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

            }
        }
    }

    // 当fragment已被实例化，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (mSubjectFragment != null)
            ft.hide(mSubjectFragment);
        if (mCircleFragment != null)
            ft.hide(mCircleFragment);
        if (mToolFragment != null)
            ft.hide(mToolFragment);
        if (mMeFragment != null)
            ft.hide(mMeFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(networkChangeReceiver);
    }
}


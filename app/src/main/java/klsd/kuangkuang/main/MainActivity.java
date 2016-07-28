package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import klsd.kuangkuang.R;
import klsd.kuangkuang.fragments.LeftFragment;
import klsd.kuangkuang.fragments.MCircleFragment;
import klsd.kuangkuang.fragments.MMeFragment;
import klsd.kuangkuang.fragments.MSubjectFragment;
import klsd.kuangkuang.fragments.MToolFragment;
import klsd.kuangkuang.utils.DataCenter;

/**
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {
    private ImageView im_title_left;//标题左上方的图标
    RadioGroup radioGroup;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private   RadioButton rbA,rbD;
    private MSubjectFragment mSubjectFragment;
    private MCircleFragment mCircleFragment;
    private MToolFragment mToolFragment;
    private MMeFragment mMeFragment;
    private Fragment mContent;
    private LinearLayout layout_main_layout;
    String str="0";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        initView();
        initSlidingMenu();
}
    private void initView() {
        Intent intent=getIntent();
        str=intent.getStringExtra("release");
        im_title_left= (ImageView) findViewById(R.id.im_more_subject);
       im_title_left.setOnClickListener(this);
        layout_main_layout= (LinearLayout) findViewById(R.id.layout_main_layout);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        /**
         * 应用进入后，默认选择点击Fragment01
         */
        if (str!=null&&str.equals("123")){
            ft.replace(R.id.just_subject_layout, new MMeFragment());
            rbD= (RadioButton) findViewById(R.id.main_rb4);
            rbD.setChecked(true);
        }else{
            ft.replace(R.id.just_subject_layout, new MSubjectFragment("0"));//news_every_content是为fragment留出的空间，用fragment替换
            rbA= (RadioButton) findViewById(R.id.main_rb1);
            rbA.setChecked(true);
        }
        ft.commit();
        radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
    }
    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu() {

        if (mContent == null) {
            mContent = new MSubjectFragment("0");
        }
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
     * 切换Fragment
     *
     * @param fragment
     */
    public void switchConent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.just_subject_layout, fragment).commit();
        getSlidingMenu().showContent();
//        tv_title.setText(title);
    }


    /**
     * radiobutton 改变选中状态
     * 进行切换fragment
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (checkedId) {
            case R.id.main_rb1:
                im_title_left.setVisibility(View.VISIBLE);
                ft.replace(R.id.just_subject_layout, new MSubjectFragment("0"));

                break;
            case R.id.main_rb2:
                im_title_left.setVisibility(View.GONE);
                ft.replace(R.id.just_subject_layout, new MCircleFragment());

                break;
            case R.id.main_rb3:
                im_title_left.setVisibility(View.GONE);
                ft.replace(R.id.just_subject_layout, new MToolFragment());

                break;
            case R.id.main_rb4:
                Log.d("现在是登录状态吗", "onCheckedChanged() returned: " + DataCenter.isSigned());
                if ( DataCenter.isSigned()){
                    im_title_left.setVisibility(View.GONE);
                    ft.replace(R.id.just_subject_layout, new MMeFragment());
                }else{
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_more_subject:
                toggle();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("现在是登录状态吗", "onCheckedChanged() returned: " + DataCenter.isSigned());
    }
}
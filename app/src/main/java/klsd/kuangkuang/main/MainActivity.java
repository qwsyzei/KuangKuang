package klsd.kuangkuang.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import klsd.kuangkuang.R;
import klsd.kuangkuang.fragment.LeftFragment;
import klsd.kuangkuang.fragments.MCircleFragment;
import klsd.kuangkuang.fragments.MMeFragment;
import klsd.kuangkuang.fragments.MSubjectFragment;
import klsd.kuangkuang.fragments.MToolFragment;


/**
 * 主界面
 */
public class MainActivity extends SlidingFragmentActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener {
    private ImageView im_title_left;//标题左上方的图标
    RadioGroup radioGroup;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private   RadioButton rbA;
    private MSubjectFragment mSubjectFragment;
    private MCircleFragment mCircleFragment;
    private MToolFragment mToolFragment;
    private MMeFragment mMeFragment;
    private Fragment mContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        initSlidingMenu(savedInstanceState);
        initView();

}
    private void initView() {
        im_title_left= (ImageView) findViewById(R.id.im_more_subject);
       im_title_left.setOnClickListener(this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        /**
         * 应用进入后，默认选择点击Fragment01
         */
        ft.replace(R.id.just_subject_layout, new MSubjectFragment());//news_every_content是为fragment留出的空间，用fragment替换
        rbA= (RadioButton) findViewById(R.id.main_rb1);
        rbA.setChecked(true);
        ft.commit();
        radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
    }
    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        // 如果保存的状态不为空则得到之前保存的Fragment，否则实例化MyFragment
        if (savedInstanceState != null) {
            mContent  = fm.getFragment(
                    savedInstanceState, "mContent");
        }

        if (mContent == null) {
            mContent = new MSubjectFragment();
        }

        // 设置左侧滑动菜单
        setBehindContentView(R.layout.menu_frame_left);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftFragment()).commit();

        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
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
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
//    }

    /**
     * 切换Fragment
     *
     * @param fragment
     */
    public void switchConent(Fragment fragment, String title) {
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
                ft.replace(R.id.just_subject_layout, new MSubjectFragment());

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
                im_title_left.setVisibility(View.GONE);
                ft.replace(R.id.just_subject_layout, new MMeFragment());

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
}
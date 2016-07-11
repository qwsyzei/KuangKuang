package klsd.kuangkuang.main;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.http.client.HttpRequest;

import klsd.kuangkuang.R;
import klsd.kuangkuang.fragments.MCircleFragment;
import klsd.kuangkuang.fragments.MMeFragment;
import klsd.kuangkuang.fragments.MSubjectFragment;
import klsd.kuangkuang.fragments.MToolFragment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private ImageView im_title_left;//标题左上方的图标
    private TextView tv_title;//标题中间文字
    private TextView tv_right;
    RadioGroup radioGroup;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private   RadioButton rbA;
    private MSubjectFragment mSubjectFragment;
    private MCircleFragment mCircleFragment;
    private MToolFragment mToolFragment;
    private MMeFragment mMeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {


        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        /**
         * 应用进入后，默认选择点击Fragment01
         */
        ft.replace(R.id.main_layout, new MSubjectFragment());//news_every_content是为fragment留出的空间，用fragment替换
        rbA= (RadioButton) findViewById(R.id.main_rb1);
        rbA.setChecked(true);
        ft.commit();
        radioGroup = (RadioGroup) findViewById(R.id.main_radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
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
                ft.replace(R.id.main_layout, new MSubjectFragment());
                break;
            case R.id.main_rb2:
                ft.replace(R.id.main_layout, new MCircleFragment());
                break;
            case R.id.main_rb3:
                ft.replace(R.id.main_layout, new MToolFragment());
                break;
            case R.id.main_rb4:
                ft.replace(R.id.main_layout, new MMeFragment());
                break;
        }
        ft.commit();
    }

}
package klsd.kuangkuang.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.GuidePagerAdapter;

/**
 * 启动页
 * Created by qiwei.
 */
public class LogoActivity extends BaseActivity {
    private TextView tv_logo;
    private ViewPager vpGuide;
    private ImageView imEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        initView();
    }

    private void initView() {

        vpGuide = (ViewPager) findViewById(R.id.logo_vp_guide);
        vpGuide.setAdapter(new GuidePagerAdapter(getAllGuidePages()));
        vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                switch (arg0) {
                    case 0:

                        imEntry.setVisibility(View.INVISIBLE);
                        break;
                    case 1:

                        imEntry.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        imEntry.setVisibility(View.VISIBLE);

                        break;
                }
            }


            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        });

        imEntry = (ImageView) findViewById(R.id.im_right_now);

        imEntry.setOnClickListener(listener);


    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.im_right_now:
                    myStartActivity(new Intent(LogoActivity.this, MainActivity.class));
                    finish();
                    break;

            }
        }
    };

    private List<View> getAllGuidePages() {

        List<View> views = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            views.add(getGuidePage(i));
        }
        return views;
    }

    private View getGuidePage(int i) {

        View v = View.inflate(LogoActivity.this, R.layout.include_guide_page, null);
        ImageView ivGuidePage = (ImageView) v.findViewById(R.id.iv_guide_page);

        switch (i) {
            case 0:
                ivGuidePage.setImageResource(R.mipmap.logo01);
                break;
            case 1:
                ivGuidePage.setImageResource(R.mipmap.logo02);
                break;
            case 2:
                ivGuidePage.setImageResource(R.mipmap.logo03);
                break;
        }

        return ivGuidePage;
    }


}


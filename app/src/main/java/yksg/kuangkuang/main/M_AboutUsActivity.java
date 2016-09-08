package yksg.kuangkuang.main;

import android.os.Bundle;

import yksg.kuangkuang.R;

/**
 * 关于我们
 */
public class M_AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_about_us);
        setTitle(getString(R.string.about_us));

    }
}

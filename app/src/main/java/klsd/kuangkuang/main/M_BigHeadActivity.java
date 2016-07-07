package klsd.kuangkuang.main;

import android.os.Bundle;

import klsd.kuangkuang.R;

/**
 * 大头像
 */
public class M_BigHeadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__big_head);
        setTitle(getString(R.string.head_pic));
        initView();
    }

    private void initView() {

    }
}

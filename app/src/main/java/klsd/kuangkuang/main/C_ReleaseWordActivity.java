package klsd.kuangkuang.main;

import android.os.Bundle;
import android.widget.TextView;

import klsd.kuangkuang.R;

/**
 * 发表说说
 */
public class C_ReleaseWordActivity extends BaseActivity {

    private TextView tv_release;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_release_word);
        setTitle(getString(R.string.release_word));
        initView();
    }

    private void initView() {
        tv_release= (TextView) findViewById(R.id.tv_title_right);
        tv_release.setText(getString(R.string.send));
    }
}

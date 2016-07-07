package klsd.kuangkuang.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import klsd.kuangkuang.R;

/**
 * 意见反馈
 */
public class M_FeedBackActivity extends BaseActivity {
    private EditText edit;
private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__feed_back);
        setTitle(getString(R.string.feedback));
        initView();
    }

    private void initView() {
        edit= (EditText) findViewById(R.id.feedback_edit);
        btn= (Button) findViewById(R.id.feedback_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

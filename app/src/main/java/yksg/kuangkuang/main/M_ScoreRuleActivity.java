package yksg.kuangkuang.main;

import android.os.Bundle;

import yksg.kuangkuang.R;
/**
 * 积分规则
 */
public class M_ScoreRuleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__score_rule);
        setTitle(getString(R.string.score_rule));
    }
}

package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import klsd.kuangkuang.R;

/**
 * 帐户管理
 */
public class M_AdminManagerActivity extends BaseActivity implements View.OnClickListener{
private RelativeLayout layout_change_phone,layout_change_psd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_admin_manager);
        setTitle(getString(R.string.admin_manager));
        initView();
    }

    private void initView() {
        layout_change_phone= (RelativeLayout) findViewById(R.id.layout_admin_manager_change_phone);
        layout_change_psd= (RelativeLayout) findViewById(R.id.layout_admin_manager_change_psd);
        layout_change_phone.setOnClickListener(this);
        layout_change_psd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_admin_manager_change_phone:
                myStartActivity(new Intent(M_AdminManagerActivity.this,M_NewPhoneProActivity.class));
                break;
            case R.id.layout_admin_manager_change_psd:
                myStartActivity(new Intent(M_AdminManagerActivity.this,M_ChangePsdActivity.class));
                break;
        }
    }
}

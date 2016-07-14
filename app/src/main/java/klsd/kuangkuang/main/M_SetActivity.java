package klsd.kuangkuang.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.views.CleanCacheDialog;
import klsd.kuangkuang.views.ExitDialog;

/**
 * 设置界面
 */
public class M_SetActivity extends BaseActivity implements View.OnClickListener{
private RelativeLayout layout_personal,layout_admin,layout_about_us,layout_feedback,layout_give_mark,layout_clean_cache,layout_start_push;
    private CleanCacheDialog cleanDialog;
    private ExitDialog exitDialog;
    private Button btn;
    private TextView tv_clean_yes,tv_clean_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_set);
        setTitle(getString(R.string.set));
        initView();
    }

    private void initView() {
        layout_personal= (RelativeLayout) findViewById(R.id.set_personal_data);
        layout_admin= (RelativeLayout) findViewById(R.id.set_admin_manager);
        layout_about_us= (RelativeLayout) findViewById(R.id.set_about_us);
        layout_feedback= (RelativeLayout) findViewById(R.id.set_feedback);
        layout_give_mark= (RelativeLayout) findViewById(R.id.set_give_mark);
        layout_clean_cache= (RelativeLayout) findViewById(R.id.set_clean_cache);
        layout_start_push= (RelativeLayout) findViewById(R.id.set_start_push);
        btn= (Button) findViewById(R.id.btn_exit);

        layout_personal.setOnClickListener(this);
        layout_admin.setOnClickListener(this);
        layout_about_us.setOnClickListener(this);
        layout_feedback.setOnClickListener(this);
        layout_give_mark.setOnClickListener(this);
        layout_clean_cache.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_personal_data:
                myStartActivity(new Intent(M_SetActivity.this,M_PersonalDataActivity.class));
                break;
            case R.id.set_admin_manager:
                myStartActivity(new Intent(M_SetActivity.this,M_AdminManagerActivity.class));
                break;
            case R.id.set_about_us:
                myStartActivity(new Intent(M_SetActivity.this,M_AboutUsActivity.class));
                break;
            case R.id.set_feedback:
                myStartActivity(new Intent(M_SetActivity.this,M_FeedBackActivity.class));
                break;
            case R.id.set_give_mark:
                myStartActivity(new Intent(M_SetActivity.this,M_GiveMarkActivity.class));
                break;
            case R.id.set_clean_cache:
                //弹出窗口
                Clean_Dialog();
                break;
            case R.id.dialog_clean_yes:
                //开始清理缓存
                cleanDialog.dismiss();
//                ToastUtil.show(M_SetActivity.this, );
                break;
            case R.id.dialog_clean_no:
               cleanDialog.dismiss();
                break;
            case R.id.exit_yes:
                signOut();
                break;
            case R.id.exit_no:
                exitDialog.dismiss();
                break;
            case R.id.btn_exit:
                Exit_Dialog();
                break;
        }
    }
    /**
     * 退出
     */
    private void signOut() {
        setMember(null);
        DataCenter.setSignedOut();
        SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
        editor.clear();       //清除登录信息
        editor.commit();
        myStartActivity(new Intent(this, LoginActivity.class));
        finish();
    }
    //清理窗口
    private void Clean_Dialog() {
        cleanDialog = new CleanCacheDialog(M_SetActivity.this, R.style.MyDialogStyle, R.layout.dialog_clean_cache);
        cleanDialog.show();
        tv_clean_yes = (TextView) cleanDialog.findViewById(R.id.dialog_clean_yes);
        tv_clean_no = (TextView) cleanDialog.findViewById(R.id.dialog_clean_no);
        tv_clean_yes.setOnClickListener(this);
        tv_clean_no.setOnClickListener(this);

    }
    //退出窗口
    private void Exit_Dialog() {
        exitDialog = new ExitDialog(M_SetActivity.this, R.style.MyDialogStyle, R.layout.dialog_exit);

        exitDialog.show();

        tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
        tv_no = (TextView) exitDialog.findViewById(R.id.exit_no);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);

    }
}

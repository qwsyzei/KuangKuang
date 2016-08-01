package klsd.kuangkuang.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import klsd.kuangkuang.R;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.DataCleanManager;

import klsd.kuangkuang.views.CleanCacheDialog;
import klsd.kuangkuang.views.ExitDialog;
import klsd.kuangkuang.views.ToggleButton;

import static klsd.kuangkuang.R.id.dialog_exit_title;


public class M_SetActivity extends BaseActivity implements View.OnClickListener{
private RelativeLayout layout_personal,layout_admin,layout_about_us,layout_feedback,layout_give_mark,layout_clean_cache,layout_start_push;
    private CleanCacheDialog cleanDialog;
    private ExitDialog exitDialog;

    private Button btn;
    private TextView tv_clean_yes,tv_clean_no;
    private ToggleButton toggleButton;
    private TextView tv_cache;
    private String cacheSize;
    private static String dir = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/Android/data/klsd.kuangkuang/cache/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_set);
        setTitle(getString(R.string.set));
        initView();
    }

    private void initView() {
        toggleButton= (ToggleButton) findViewById(R.id.set_toggle);
        layout_personal= (RelativeLayout) findViewById(R.id.set_personal_data);
        layout_admin= (RelativeLayout) findViewById(R.id.set_admin_manager);
        layout_about_us= (RelativeLayout) findViewById(R.id.set_about_us);
        layout_feedback= (RelativeLayout) findViewById(R.id.set_feedback);
        layout_give_mark= (RelativeLayout) findViewById(R.id.set_give_mark);
        layout_clean_cache= (RelativeLayout) findViewById(R.id.set_clean_cache);
        layout_start_push= (RelativeLayout) findViewById(R.id.set_start_push);
        btn= (Button) findViewById(R.id.btn_exit);
        tv_cache= (TextView) findViewById(R.id.set_tv_cache);
        try {
            cacheSize = DataCleanManager.getCacheSize(new File(dir));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_cache.setText(cacheSize);
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
//                myStartActivity(new Intent(M_SetActivity.this,M_GiveMarkActivity.class));
                break;
            case R.id.set_clean_cache:
                Clean_Dialog();
                break;
            case R.id.dialog_clean_yes:
                cleanDialog.dismiss();
                DataCleanManager.deleteFolderFile(dir, false);
                tv_cache.setText("0.00MB");
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

    private void signOut() {
        setMember(null);
        DataCenter.setSignedOut();
        SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        myStartActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void Clean_Dialog() {
        cleanDialog = new CleanCacheDialog(M_SetActivity.this, R.style.MyDialogStyle, R.layout.dialog_clean_cache);
        cleanDialog.show();
        tv_clean_yes = (TextView) cleanDialog.findViewById(R.id.dialog_clean_yes);
        tv_clean_no = (TextView) cleanDialog.findViewById(R.id.dialog_clean_no);
        tv_clean_yes.setOnClickListener(this);
        tv_clean_no.setOnClickListener(this);

    }

    private void Exit_Dialog() {
        exitDialog = new ExitDialog(M_SetActivity.this, R.style.MyDialogStyle, R.layout.dialog_exit);

        exitDialog.show();

        tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
        tv_no = (TextView) exitDialog.findViewById(R.id.exit_no);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);

    }
}

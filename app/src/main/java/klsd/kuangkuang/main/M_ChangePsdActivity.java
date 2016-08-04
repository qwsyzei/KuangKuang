package klsd.kuangkuang.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.common.EncrypAES;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ContainsEmojiEditText;

/**
 * 修改密码
 * Created by qiwei.
 */
public class M_ChangePsdActivity extends BaseActivity {
    private ContainsEmojiEditText edit_psd, edit_newpsd, edit_newpsd_confirm;
    private Button btn_change;
    private TextView tv_title;
    private ImageView im1, im2, im3;
    private int flag1 = 0, flag2 = 0, flag3 = 0;
    /**
     *对用户名和密码进行加解密
     */
    private EncrypAES mAes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_change_login_psd);
        setTitle(getString(R.string.change_password));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {


        edit_psd = (ContainsEmojiEditText) findViewById(R.id.change_login_password);
        edit_newpsd = (ContainsEmojiEditText) findViewById(R.id.change_login_newpassword);
        edit_newpsd_confirm = (ContainsEmojiEditText) findViewById(R.id.change_login_newpassword_confirm);
        btn_change = (Button) findViewById(R.id.change_login_psd_button);
        im1 = (ImageView) findViewById(R.id.change_login_im1);
        im2 = (ImageView) findViewById(R.id.change_login_im2);
        im3 = (ImageView) findViewById(R.id.change_login_im3);

        im1.setOnClickListener(listener);
        im2.setOnClickListener(listener);
        im3.setOnClickListener(listener);
        btn_change.setOnClickListener(listener);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.change_login_psd_button:
                    if (edit_psd.getText().toString().equals("")){
                        ToastUtil.show(M_ChangePsdActivity.this, getString(R.string.no_input_psd));
                    }else if(edit_newpsd.getText().toString().equals("")){
                        ToastUtil.show(M_ChangePsdActivity.this,getString(R.string.no_input_new_psd));
                    }else if(edit_newpsd_confirm.getText().toString().equals("")){
                        ToastUtil.show(M_ChangePsdActivity.this,getString(R.string.no_confirm_new_psd));
                    }else{
                        tosendChange();
                    }

                    break;

                case R.id.change_login_im1:
                    if (flag1 == 0) {
                    //设为密码可见
                        im1.setImageResource(R.mipmap.icon_content_eye_open);//图标的
                        edit_psd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag1 = 1;
                    } else {
                        im1.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_psd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag1 = 0;
                    }
                    break;
                case R.id.change_login_im2:
                    if (flag2 == 0) {
                     //设为密码可见
                        im2.setImageResource(R.mipmap.icon_content_eye_open);//图标的
                        edit_newpsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag2 = 1;
                    } else {
                        im2.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_newpsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag2 = 0;
                    }
                    break;
                case R.id.change_login_im3:
                    if (flag3 == 0) {
                     //设为密码可见
                        im3.setImageResource(R.mipmap.icon_content_eye_open);//图标的
                        edit_newpsd_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag3 = 1;
                    } else {
                        im3.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_newpsd_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag3 = 0;
                    }
                    break;
            }
        }
    };

    private void tosendChange() {
        String psd = edit_psd.getText().toString();
        String newpsd = edit_newpsd.getText().toString();
        String newpsd_confirm = edit_newpsd_confirm.getText().toString();
        if (!psd.equals(newpsd)){
            sendChangeLoginPsd(psd, newpsd, newpsd_confirm, getHandler());

        }else{
            ToastUtil.show(M_ChangePsdActivity.this,getString(R.string.old_new));
        }

    }

    private void sendChangeLoginPsd(String psd, String newpsd, String newpsd_confirm, Handler handler) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("login_password", psd);
        params.addQueryStringParameter("password", newpsd);
        params.addQueryStringParameter("password_confirmation", newpsd_confirm);
        new MyHTTP(this).baseRequest(Consts.changeloginpasswordApi, JSONHandler.JTYPE_RESET, HttpRequest.HttpMethod.POST, params, handler);
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_RESET)) {
            if (handlerBundler.getBoolean("reset_password")) {
                //保存登录信息
                SharedPreferences.Editor editor=getSharedPreferences("login_info",MODE_PRIVATE).edit();
                mAes=new EncrypAES();

                String password=mAes.EncryptorString(edit_newpsd.getText().toString());//加密密码
                editor.putString("password", password);
                editor.commit();
                ToastUtil.show(M_ChangePsdActivity.this, getString(R.string.change_success));
                myStartActivity(new Intent(M_ChangePsdActivity.this, M_SetActivity.class));
            }
        }
    }
}

package klsd.kuangkuang.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.common.EncrypAES;
import klsd.kuangkuang.models.Member;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ContainsEmojiEditText;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity {
    private TextView tv_signup, tv_forget;//忘记密码
    private EditText edit_admin;
    private ContainsEmojiEditText editTPassword;
    private ImageView im_inputcancel, im_eye;//email输入时删除全部的图标，即“叉”,密码可见状态的图标
    private  int flag = 0;

    /**
     *对用户名和密码进行加解密
     */
    private EncrypAES mAes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle(getString(R.string.login));
        initViews();
    }


    @Override
    public void signIn(View v) {
        if (edit_admin.getText().length()==11){
            String phone = edit_admin.getText().toString();
            String psd = editTPassword.getText().toString();
            sendSignIn(phone, psd, getHandler());
        }else{
            ToastUtil.show(LoginActivity.this,getString(R.string.wrong_admin_name));
        }

    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_LOGIN)) {
            if (handlerBundler.getBoolean("signed")) {
                DataCenter.setSigned();
                getMemberData();

                //保存登录信息
                SharedPreferences.Editor editor=getSharedPreferences("login_info",MODE_PRIVATE).edit();
                mAes=new EncrypAES();
                String admin= mAes.EncryptorString(edit_admin.getText().toString());//加密用户名
                String password=mAes.EncryptorString(editTPassword.getText().toString());//加密密码

                editor.putBoolean("isLogin",true);
                editor.putString("admin", admin);
                editor.putString("password", password);
                editor.commit();

                myStartActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_MEMBER_ME)) {
            setMember((Member) handlerBundler.getSerializable("member"));
        }
    }

    private void initViews() {

        edit_admin = (EditText) findViewById(R.id.editTPhone);
        editTPassword = (ContainsEmojiEditText) findViewById(R.id.editTPassword);
        tv_signup = (TextView) findViewById(R.id.tv_signup);
        tv_forget = (TextView) findViewById(R.id.tv_forgetpassword);
        im_inputcancel = (ImageView) findViewById(R.id.getback_im_cancel);
        im_eye = (ImageView) findViewById(R.id.sign_change_eye);

        tv_signup.setOnClickListener(listener);
        tv_forget.setOnClickListener(listener);
        im_inputcancel.setOnClickListener(listener);
        im_eye.setOnClickListener(listener);
        edit_admin.addTextChangedListener(watcher);


    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.tv_signup:
                    Intent intent_phone = new Intent(LoginActivity.this, SignupActivity.class);
                    myStartActivity(intent_phone);
                    break;

                case R.id.getback_im_cancel:
                    edit_admin.setText("");
                    break;
                case R.id.tv_forgetpassword:
                   myStartActivity(new Intent(LoginActivity.this,ForgetPsdActivity.class));
                    break;

                case R.id.sign_change_eye:

                    if (flag == 0) {
                        //设为密码可见
                        im_eye.setImageResource(R.mipmap.icon_content_eye_open);
                        editTPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag = 1;
                    } else {
                        im_eye.setImageResource(R.mipmap.icon_content_eye_close);
                        editTPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag = 0;
                    }
                    break;
            }
        }
    };
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (edit_admin.getText().toString() != null && !edit_admin.getText().toString().equals("")) {
                im_inputcancel.setVisibility(View.VISIBLE);
            } else {
                im_inputcancel.setVisibility(View.INVISIBLE);
            }
        }
    };
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//          // 是否触发按键为back键
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            myStartActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//            return true;
//        }else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
}


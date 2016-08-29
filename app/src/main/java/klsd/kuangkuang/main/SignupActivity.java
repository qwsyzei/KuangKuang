package klsd.kuangkuang.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.common.EncrypAES;
import klsd.kuangkuang.models.Member;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ContainsEmojiEditText;

/**
 * 手机注册界面
 * @author qiwei
 */
public class SignupActivity extends BaseActivity {

    private TextView tv_email_signup;//email注册
    private EditText edit_phonenumber, edit_phoneyan;
    private ContainsEmojiEditText edit_password, edit_password_confirm;
    private ImageView im_signup;
    private TextView tv_get;

    private ImageView im_eye1, im_eye2;
    private ImageView im_delete;
    private int flag1 = 0, flag2 = 0;

    String member_id;
    /**
     *对用户名和密码进行加解密
     */
    private EncrypAES mAes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);
        setTitle(getString(R.string.sign_up));
        initView();
    }

    private void initView() {
        im_delete= (ImageView) findViewById(R.id.im_signup_delete);

        tv_email_signup = (TextView) findViewById(R.id.tv_email_signup);
        edit_phonenumber = (EditText) findViewById(R.id.signup_phone_number);
        edit_phoneyan = (EditText) findViewById(R.id.signup_phone_yan);
        edit_password = (ContainsEmojiEditText) findViewById(R.id.signup_phone_edit_password);
        edit_password_confirm = (ContainsEmojiEditText) findViewById(R.id.signup_phone_edit_password_again);
        EditTListener(edit_password);
        EditTListener(edit_password_confirm);
        tv_get = (TextView) findViewById(R.id.signup_phone_getyan);
       im_signup = (ImageView) findViewById(R.id.im_signup_phone);
        im_eye1 = (ImageView) findViewById(R.id.sign_up_im_change1);
        im_eye2 = (ImageView) findViewById(R.id.sign_up_im_change2);


        edit_phonenumber.addTextChangedListener(watcher);
        im_eye1.setOnClickListener(listener);
        im_eye2.setOnClickListener(listener);
        im_delete.setOnClickListener(listener);

        tv_get.setOnClickListener(listener);
        tv_email_signup.setOnClickListener(listener);
        im_signup.setOnClickListener(listener);

    }

    /**
     * 验证码的基础
     */
    private void toPhoneCode() {
        String phone_number = edit_phonenumber.getText().toString();

        if (phone_number != null) {
            getcode(phone_number, getHandler());
        } else {
            ToastUtil.show(SignupActivity.this, getString(R.string.wrong_phone_number));
        }

    }

    /**
     * 注册的基础
     */
    private void toPhoneSignup() {
        String phone_number = edit_phonenumber.getText().toString();
        String phone_yan = edit_phoneyan.getText().toString();
        String password = edit_password.getText().toString();
        String password_confirm = edit_password_confirm.getText().toString();
        sendSignupPhone(phone_number, password, password_confirm, phone_yan, getHandler());
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.signup_phone_getyan:
                    if (edit_phonenumber.getText().length()==11){
                        toPhoneCode();
                    }else{
                        ToastUtil.show(SignupActivity.this,getString(R.string.wrong_admin_name));
                    }
                    break;
                case R.id.im_signup_phone:
                    if (edit_phonenumber.getText().toString().equals("")){
                        ToastUtil.show(SignupActivity.this,getString(R.string.no_phone));
                    }else if(edit_phoneyan.getText().toString().equals("")){
                        ToastUtil.show(SignupActivity.this,getString(R.string.no_input_code));
                    }else if(edit_password.getText().toString().equals("")){
                        ToastUtil.show(SignupActivity.this,getString(R.string.no_input_psd));
                    }else if(edit_password_confirm.getText().toString().equals("")){
                        ToastUtil.show(SignupActivity.this,getString(R.string.no_confirm_psd));
                    }else{
                        toPhoneSignup();
                    }
                    break;
                case R.id.im_signup_delete:
                    edit_phonenumber.setText("");
                    break;
                case R.id.sign_up_im_change1:
                    if (flag1 == 0) {
                        //设为密码可见
                        im_eye1.setImageResource(R.mipmap.icon_content_eye_open);
                        edit_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag1 = 1;
                    } else {
                        im_eye1.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag1 = 0;
                    }
                    break;
                case R.id.sign_up_im_change2:
                    if (flag2 == 0) {
                        //设为密码可见
                        im_eye2.setImageResource(R.mipmap.icon_content_eye_open);
                        edit_password_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag2 = 1;
                    } else {
                        im_eye2.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_password_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag2 = 0;
                    }
                    break;

            }
        }
    };

    private void getcode(String phone_number, Handler handler) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_number", phone_number);
        params = KelaParams.generateSignParam("POST", Consts.getAuthCodeApi, params);
        new MyHTTP(this).baseRequest(Consts.getAuthCodeApi, JSONHandler.JTYPE_SMS_AUTH_CODE, HttpRequest.HttpMethod.POST, params, handler);
    }

    private void sendSignupPhone(String phone_number, String password, String password_confirm, String phone_yan, Handler handler) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_number", phone_number);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("password_confirmation", password_confirm);
        params.addQueryStringParameter("code", phone_yan);
        params = KelaParams.generateSignParam("POST", Consts.signUpPhoneApi, params);
        new MyHTTP(this).baseRequest(Consts.signUpPhoneApi, JSONHandler.JTYPE_SIGN, HttpRequest.HttpMethod.POST, params, handler);
    }

    @Override
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_SIGN)) {
                member_id=handlerBundler.getString("signup");
               DataCenter.setMember_id(member_id);
                ToastUtil.show(SignupActivity.this, R.string.signup_success);
                //保存登录信息
                SharedPreferences.Editor editor=getSharedPreferences("login_info",MODE_PRIVATE).edit();
                mAes=new EncrypAES();
                String admin= mAes.EncryptorString(edit_phonenumber.getText().toString());//加密用户名
                String password=mAes.EncryptorString(edit_password.getText().toString());//加密密码

                editor.putBoolean("isLogin", true);
                editor.putString("admin", admin);
                editor.putString("password", password);
                editor.commit();
            sendSignIn(edit_phonenumber.getText().toString(), edit_password.getText().toString(), getHandler());//注册成功就自动登录

        }else if (jtype.equals(JSONHandler.JTYPE_LOGIN)) {
            if (handlerBundler.getBoolean("signed")) {
                DataCenter.setSigned();
                getMemberData();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_MEMBER_ME)) {
            setMember((Member) handlerBundler.getSerializable("member"));
            startActivity(new Intent(SignupActivity.this, MainActivity.class));//主界面
            finish();
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (edit_phonenumber.getText().toString() != null && !edit_phonenumber.getText().toString().equals("")) {
                im_delete.setVisibility(View.VISIBLE);
            } else {
                im_delete.setVisibility(View.INVISIBLE);
            }
        }
    };
}
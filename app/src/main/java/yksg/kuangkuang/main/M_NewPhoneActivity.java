package yksg.kuangkuang.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.common.EncrypAES;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.DataCenter;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.KelaParams;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;

/**
 * 更新手机号，新手机验证
 */
public class M_NewPhoneActivity extends BaseActivity {
    private TextView tv_getcode;
    private EditText edit_phonenumber, edit_code;
    private ImageView im_delete;
    private Button btn_bind;
    /**
     * 对用户名和密码进行加解密
     */
    private EncrypAES mAes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_phone);
        initView();
    }

    private void initView() {
        setTitle(getString(R.string.new_phone));
        edit_phonenumber = (EditText) findViewById(R.id.new_phone_edit_phonenumber);
        edit_code = (EditText) findViewById(R.id.new_phone_edit_code);
        tv_getcode = (TextView) findViewById(R.id.new_phone_tv_getcode);
        btn_bind = (Button) findViewById(R.id.new_phone_btn_bind);
        im_delete = (ImageView) findViewById(R.id.new_phone_im_delete);

        im_delete.setOnClickListener(listener);
        tv_getcode.setOnClickListener(listener);
        btn_bind.setOnClickListener(listener);
        edit_phonenumber.addTextChangedListener(watcher);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.new_phone_im_delete:
                    edit_phonenumber.setText("");
                    break;
                case R.id.new_phone_tv_getcode:
                    if (edit_phonenumber.getText().length()==11){
                        toBindPhoneCode();
                    }else{
                        ToastUtil.show(M_NewPhoneActivity.this,getString(R.string.wrong_admin_name));
                    }
                    break;
                case R.id.new_phone_btn_bind:
                    toBindPhone();
                    break;
            }
        }
    };

    /**
     * 获取验证码的基础工作
     */
    private void toBindPhoneCode() {
        String phone = edit_phonenumber.getText().toString();
        getbackcode(phone, getHandler());
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param handler
     */
    private void getbackcode(String phone, Handler handler) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_number", phone);
        params = KelaParams.generateSignParam("POST", Consts.getAuthCodeApi, params);
        new MyHTTP(this).baseRequest(Consts.getAuthCodeApi, JSONHandler.JTYPE_SMS_AUTH_CODE, HttpRequest.HttpMethod.POST, params, handler);
        Log.d("手机号是：", "getbackcode() returned: " + phone);
    }

    /**
     * 绑定手机号的基础工作
     */
    private void toBindPhone() {
        String phone = edit_phonenumber.getText().toString();
        String code = edit_code.getText().toString();

        sendBindPhone(phone, code, getHandler());
    }

    private void sendBindPhone(String phone, String code, Handler handler) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_number", phone);
        params.addQueryStringParameter("code", code);
        new MyHTTP(this).baseRequest(Consts.upDatePhoneApi, JSONHandler.JTYPE_BIND_PHONE, HttpRequest.HttpMethod.POST, params, handler);

    }

    @Override
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_BIND_PHONE)) {
            if (handlerBundler.getBoolean("bind_phone")) {
                DataCenter.getMember().setPhone_number(edit_phonenumber.getText().toString());
                ToastUtil.show(M_NewPhoneActivity.this, getString(R.string.update_phone_number_success));
                //保存登录信息
                SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
                mAes = new EncrypAES();
                String admin = mAes.EncryptorString(edit_phonenumber.getText().toString());//加密用户名
                editor.putBoolean("isLogin", true);
                editor.putString("admin", admin);
                editor.commit();
                myStartActivity(new Intent(M_NewPhoneActivity.this, M_SetActivity.class));
                finish();
            }
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

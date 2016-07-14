package klsd.kuangkuang.main;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import klsd.kuangkuang.R;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;

/**
 * 忘记密码
 * Created by qiwei on 2016/7/3.
 */
public class ForgetPsdActivity extends BaseActivity {

    private EditText edit_phonenumber, edit_phoneyan, edit_newpsd, edit_newpsd_confirm;
    private ImageView im_inputcancel;//email输入时删除全部的图标，即“叉”
    private TextView tv_getcode;
    private ImageView im_set;
    private ImageView im_eye1, im_eye2;
    private int flag1 = 0, flag2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        im_inputcancel = (ImageView) findViewById(R.id.getback_im_cancel);
        edit_phonenumber = (EditText) findViewById(R.id.getback_phone_edit_number);
        edit_phoneyan = (EditText) findViewById(R.id.getback_phone_yan);
        edit_newpsd = (EditText) findViewById(R.id.edit_newpassword);
        edit_newpsd_confirm = (EditText) findViewById(R.id.edit_confirm_newpassword);
        tv_getcode = (TextView) findViewById(R.id.getback_code_phone);
        im_set = (ImageView) findViewById(R.id.forget_im_done);

        im_eye1 = (ImageView) findViewById(R.id.forget_im_change1);
        im_eye2 = (ImageView) findViewById(R.id.forget_im_change2);

        im_eye1.setOnClickListener(listener);
        im_eye2.setOnClickListener(listener);

        im_inputcancel.setOnClickListener(listener);

        tv_getcode.setOnClickListener(listener);
        im_set.setOnClickListener(listener);
        edit_phonenumber.addTextChangedListener(watcher);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.getback_im_cancel:
                    edit_phonenumber.setText("");
                    break;
                case R.id.getback_code_phone:
                    toPhoneCode();
                    break;
                case R.id.forget_im_done:
                    toGetbackPhone();
                    break;
                case R.id.forget_im_change1:
                    if (flag1 == 0) {
                        //设为密码可见
                        im_eye1.setImageResource(R.mipmap.icon_content_eye_open);
                        edit_newpsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag1 = 1;
                    } else {
                        im_eye1.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_newpsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag1 = 0;
                    }
                    break;
                case R.id.forget_im_change2:
                    if (flag2 == 0) {
                        //设为密码可见
                        im_eye2.setImageResource(R.mipmap.icon_content_eye_open);
                        edit_newpsd_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        flag2 = 1;
                    } else {
                        im_eye2.setImageResource(R.mipmap.icon_content_eye_close);
                        edit_newpsd_confirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag2 = 0;
                    }
                    break;
            }
        }
    };

    /**
     * 验证码的基础
     */
    private void toPhoneCode() {
        String phone_number = edit_phonenumber.getText().toString();

        if (phone_number != null) {
            getbackcode(phone_number, getHandler());
        } else {
            ToastUtil.show(ForgetPsdActivity.this, getString(R.string.wrong_phone_number));
        }

    }

    /**
     * 获取验证码
     *
     * @param phone_number
     * @param handler
     */
    private void getbackcode(String phone_number, Handler handler) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_number", phone_number);
        params = KelaParams.generateSignParam("POST", Consts.getAuthCodeApi, params);
        new MyHTTP(this).baseRequest(Consts.getAuthCodeApi, JSONHandler.JTYPE_SMS_AUTH_CODE, HttpRequest.HttpMethod.POST, params, handler);
    }

    /**
     * 手机找回的基础工作
     */
    private void toGetbackPhone() {
        String phone_number = edit_phonenumber.getText().toString();
        String phone_yan = edit_phoneyan.getText().toString();
        String newpassword = edit_newpsd.getText().toString();
        String newpassword_confirm = edit_newpsd_confirm.getText().toString();
        sendGetbackPhone(phone_number, newpassword, newpassword_confirm, phone_yan, getHandler());
    }

    private void sendGetbackPhone(String phone_number, String newpassword, String newpassword_confirm, String phone_yan, Handler handler) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("phone_number", phone_number);
        params.addQueryStringParameter("password", newpassword);
        params.addQueryStringParameter("password_confirmation", newpassword_confirm);
        params.addQueryStringParameter("code", phone_yan);
        params = KelaParams.generateSignParam("POST", Consts.getbackPasswordPhoneApi, params);
        new MyHTTP(this).baseRequest(Consts.getbackPasswordPhoneApi, JSONHandler.JTYPE_SIGN, HttpRequest.HttpMethod.POST, params, handler);
    }

    @Override
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_SIGN)) {
            if (handlerBundler.getBoolean("signup")) {
                ToastUtil.show(ForgetPsdActivity.this, getString(R.string.set_success));
//                myStartActivity(new Intent(ForgetPsdActivity.this, MarketActivity.class));
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
                im_inputcancel.setVisibility(View.VISIBLE);
            } else {
                im_inputcancel.setVisibility(View.INVISIBLE);
            }
        }
    };
}


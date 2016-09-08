package yksg.kuangkuang.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import yksg.kuangkuang.R;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;

/**
 * 更新手机号，旧手机验证
 */
public class M_NewPhoneProActivity extends BaseActivity {
    TextView tv_phone;
    Button btn_update;
    EditText edit_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_phone_pro);
        initView();
    }

    private void initView() {
        setTitle(getString(R.string.update_phone));
        btn_update = (Button) findViewById(R.id.new_phone_btn_change);
        tv_phone = (TextView) findViewById(R.id.new_phone_tv_phonenumber);
        tv_phone.setText(getMember().getPhone_number());
        edit_code = (EditText) findViewById(R.id.new_phone_pro_edit_code);
        btn_update.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.new_phone_btn_change:
                    VerifyCode();
                    break;
            }
        }
    };

    //核验验证码
    private void VerifyCode() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("code", edit_code.getText().toString());
        new MyHTTP(this).baseRequest(Consts.verifyCodeApi, JSONHandler.JTYPE_VERIFY_CODE, HttpRequest.HttpMethod.POST, params, getHandler());
    }

    @Override
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_VERIFY_CODE)) {

            myStartActivity(new Intent(M_NewPhoneProActivity.this, M_NewPhoneActivity.class));
            finish();
        }
    }
}

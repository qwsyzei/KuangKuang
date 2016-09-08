package yksg.kuangkuang.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import yksg.kuangkuang.R;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.KelaParams;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.ContainsEmojiEditText;

/**
 * 意见反馈
 */
public class M_FeedBackActivity extends BaseActivity {
    private ContainsEmojiEditText edit;
    private Button btn;
    private MyHTTP http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__feed_back);
        setTitle(getString(R.string.feedback));
        initView();
    }

    private void initView() {
        edit = (ContainsEmojiEditText) findViewById(R.id.feedback_edit);
        EditTListener(edit);
        btn = (Button) findViewById(R.id.feedback_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals("")) {
                    ToastUtil.show(M_FeedBackActivity.this, getString(R.string.blank_content));
                } else {
                    gotoSuggest();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit.getWindowToken(), 0); //强制隐藏键盘
                }
            }
        });
    }

    /**
     * 发送反馈
     */
    private void gotoSuggest() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("content", edit.getText().toString());
        params.addQueryStringParameter("species", "app");

//        params.addQueryStringParameter("picture", "");
        params = KelaParams.generateSignParam("POST", Consts.givesuggestApi, params);
        if (http == null) http = new MyHTTP(M_FeedBackActivity.this);
        http.baseRequest(Consts.givesuggestApi, JSONHandler.JTYPE_GIVE_SUGGEST, HttpRequest.HttpMethod.POST,
                params, getHandler());

    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_GIVE_SUGGEST)) {
            edit.setText("");
            ToastUtil.show(M_FeedBackActivity.this, getString(R.string.feedback_success));
        }

    }
}

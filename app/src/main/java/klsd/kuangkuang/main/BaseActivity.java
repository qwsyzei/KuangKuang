package klsd.kuangkuang.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.lang.ref.WeakReference;
import java.util.List;
import klsd.kuangkuang.R;
import klsd.kuangkuang.models.Member;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;
import klsd.kuangkuang.views.ExitDialog;

public class BaseActivity extends FragmentActivity {

    private Application app;
    public static final String action = "signout.broadcast.action";
    public Handler privatEventHandler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;

    boolean flag = false;
    ExitDialog exitDialog;
    TextView tv_yes, tv_no;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
        app = getApplication();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }
    public void setTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }

    public void setTitleRight(String tv_right) {
        TextView textView = (TextView) findViewById(R.id.tv_title_right);
        if (textView != null) textView.setText(tv_right);
    }

    public boolean isSigned() {
        return DataCenter.isSigned();
    }

    public Application getApp() {
        return app;
    }

    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public void back(View view) {
        back();
    }

    public void gotomain(View view) {
        myStartActivity(new Intent(BaseActivity.this, MainActivity.class));
        finish();
        aniStart();
    }

    public void back() {
        finish();
        aniBack();
    }

    public void myStartActivity(Intent intent) {
        startActivity(intent);
        aniStart();
    }

    public void myStartActivityForResult(Intent intent, int code) {
        startActivityForResult(intent, code);
        aniStart();
    }

    @SuppressLint("NewApi")
    public void aniBack() {
        overridePendingTransition(R.anim.backin, R.anim.backout);
    }

    @SuppressLint("NewApi")
    public void aniStart() {
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

    public void signIn(View v) {
        toSignView("signIn");
    }

    public void sendSignIn(String uname, String psd, Handler handler) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("user_name", uname);
        params.addQueryStringParameter("login_password", psd);
        params = KelaParams.generateSignParam("GET", Consts.signInApi, params);
        new MyHTTP(this).baseRequest(Consts.signInApi, JSONHandler.JTYPE_LOGIN, HttpRequest.HttpMethod.GET, params, handler);
    }

    public void signOut() {
        setMember(null);

        DataCenter.setSignedOut();
        SharedPreferences.Editor editor = getSharedPreferences("login_info", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(action);
        sendBroadcast(intent);
        myStartActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void signUp(View v) {
        toSignView("signUp");
    }


    public void toSignView(String type) {
        Intent intent;
        if (type.equals("signIn")) {
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, SignupActivity.class);
        }
        intent.putExtra("type", type);
        myStartActivity(intent);
        finish();
        if (this.getClass().toString().contains("Welcome"))
            finish();
    }


    public String getMarket() {
        return DataCenter.getMarket();
    }

    public void setMarket(String m) {
        DataCenter.setMarket(m);
    }

    public Member getMember() {
        return DataCenter.getMember();
    }

    public void setMember(Member member) {
        DataCenter.setMember(member);
    }


    public void getMemberData() {
        new MyHTTP(this).baseRequest(Consts.memberMeApi,
                JSONHandler.JTYPE_MEMBER_ME, handler);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setPrivatEventHandler(Handler handler) {
        privatEventHandler = handler;
    }

    public Handler getPrivatEventHandler() {
        return privatEventHandler;
    }

    public static class KelaHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public KelaHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
        }
    }

    private Handler handler = new KelaHandler(this) {
        public void handleMessage(android.os.Message msg) {
            handlerBundler = msg.getData();
            responseJson = handlerBundler.getString("result");
            error_code = handlerBundler.getString("error_code");
            jtype = handlerBundler.getString("jtype");
            String ava;
            if (handlerBundler.getString("unavailable") == "1") {
                ava = "1";
            } else {
                ava = "0";
            }
            if (ava.equals("0")) {
                if (responseJson == null || responseJson.equals(getString(R.string.checkup_network))) {
                    if (flag == false) {
                        ToastUtil.show(BaseActivity.this, responseJson);
//                    myStartActivity(new Intent(BaseActivity.this, LoginActivity.class));
                        flag = true;
                    }
                    return;
                } else if (responseJson.equals("OK")) {
                    updateData();
                } else {
                    toastError();
                }
            } else {
//                myStartActivity(new Intent(BaseActivity.this, LoginActivity.class));
                Toast.makeText(BaseActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
            }

        }
    };

    private View.OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.exit_yes:

                    exitDialog.dismiss();
                    break;
                case R.id.exit_no:
                    exitDialog.dismiss();
                    break;
            }
        }
    };

    public void toastError() {
        try {
            UIutils.cancelLoading();
            if (error_code.equals("2021")||error_code.equals("2022")||error_code.equals("2015")) {//发现用户名未注册,或者密码格式错误(用于当更改密钥时)       就进入登录界面
                ToastUtil.show(BaseActivity.this, getString(ErrorCodes.CODES.get(error_code)));
                signOut();
            } else if (error_code.equals("2007")) {
                Toast.makeText(BaseActivity.this, getString(ErrorCodes.CODES.get(error_code)), Toast.LENGTH_LONG).show();
                finish();
            } else {
                ToastUtil.show(BaseActivity.this, getString(ErrorCodes.CODES.get(error_code)));
            }
        } catch (Exception e) {
            ToastUtil.show(BaseActivity.this, responseJson);
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_SMS_AUTH_CODE)) {
            ToastUtil.show(this, R.string.sms_auth_code_tip);
        }
    }


    public void getAuthCode(View v) {
        new MyHTTP(this).baseRequest(Consts.authCodeFromSmsApi, JSONHandler.JTYPE_SMS_AUTH_CODE,
                HttpRequest.HttpMethod.POST, null, getHandler());
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
//                Toast.makeText(context, "网络可用", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("unavailable", "1");
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }
    }
    //EditText的监听，用来规范它的格式，如小数点后面只能有两位，不能再有点
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //以下三个是规范小数点后面只能用两位
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                //下面这个是点不能再次出现
                if (editText.getText().toString().indexOf(".") >= 0) {
                    if (editText.getText().toString().indexOf(".", editText.getText().toString().indexOf(".") + 1) > 0) {
                        Log.d("已经有点了", "onTextChanged() returned: ");
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

    }
}

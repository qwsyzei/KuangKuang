package klsd.kuangkuang.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.http.client.HttpRequest;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.common.CommonUtils;
import klsd.kuangkuang.main.common.EncrypAES;
import klsd.kuangkuang.models.Member;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.utils.MyHTTP;


/**
 * 启动页，每次进入都要启动它
 */
public class EnterActivity extends BaseActivity implements Runnable {
    //是否为第一次使用
    private boolean isFirstUse;
    //是否为登录状态
    boolean isLogin = false;

    boolean isNetWork;
public static String secret;
    public static long server_time_cha;
    /**
     * 对用户名和密码进行加解密
     */
    private EncrypAES mAes;
    String admin;    //用户名
    String password;  //密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter);//布局是空白的，什么都不需要有
        secret=getString(R.string.secret_key);
        isNetWork = CommonUtils.CheckNetwork(EnterActivity.this);
        Log.d("连接状态", "run() returned: " + isNetWork);
        if (isNetWork == false) {
            Toast.makeText(EnterActivity.this, getString(R.string.network_problem), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            /**
             * 启动一个延迟线程
             */
            new Thread(this).start();
        }
    }

    public void getTime() {
        new MyHTTP(this).baseRequest(Consts.getTimeApi, JSONHandler.JTYPE_GET_TIME,
                HttpRequest.HttpMethod.GET, null, getHandler());
    }
    @Override
    public void run() {
        try {
            /**
             * 延迟1.5秒时间
             */
            Thread.sleep(1500);

            //读取SharedPreferences中需要的数据
            SharedPreferences preferences = getSharedPreferences("isFirst", MODE_PRIVATE);
            isFirstUse = preferences.getBoolean("isFirstUse", true);

            SharedPreferences pref = getSharedPreferences("login_info", MODE_PRIVATE);
            mAes = new EncrypAES();
            isLogin = pref.getBoolean("isLogin", false);
            admin = mAes.DecryptorString(pref.getString("admin", ""));//解密
            password = mAes.DecryptorString(pref.getString("password", ""));
            getTime();//获取服务器时间
        } catch (InterruptedException e) {

        }

    }

    //对取出来的数据进行判断
    private void judge() {
        if (isLogin == true) {
            DataCenter.setSigned();
        }
        /**
         *如果用户不是第一次使用则直接调转到登录界面,否则调转到引导界面
         */

        if (isSigned()) {
            sendSignIn(admin, password, getHandler());

        } else {
            if (isFirstUse) {

                startActivity(new Intent(EnterActivity.this, LogoActivity.class));//引导界面
            } else {
                startActivity(new Intent(EnterActivity.this, MainActivity.class));//主界面
            }

        }
        finish();
        //实例化Editor对象
        SharedPreferences.Editor editor = getSharedPreferences("isFirst", MODE_PRIVATE).edit();
        //存入数据
        editor.putBoolean("isFirstUse", false);
        //提交修改
        editor.commit();
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_LOGIN)) {
            if (handlerBundler.getBoolean("signed")) {

                getMemberData();

            }
        } else if (jtype.equals(JSONHandler.JTYPE_MEMBER_ME)) {
            setMember((Member) handlerBundler.getSerializable("member"));

            startActivity(new Intent(EnterActivity.this, MainActivity.class));//交易界面
            finish();
        } else if (jtype.equals(JSONHandler.JTYPE_GET_TIME)) {
            long server_time = (Long) handlerBundler.getSerializable("get_time");
            server_time_cha = server_time * 1000 - MyDate.getSystemTime();
            Log.d("时间差是", "updateData() returned: " + server_time_cha);
            judge();
        }
    }

}
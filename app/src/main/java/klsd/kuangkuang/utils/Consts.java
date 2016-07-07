package klsd.kuangkuang.utils;

import android.app.Application;

public class Consts extends Application {

    // API
    public final static String host = "http://119.254.211.82:8080";

public final static String host123="http://192.168.1.20:3000";
    public final static String host2 = "http://119.254.100.167:8080";
    public final static String baseApiPath = host123+ "/api/v2";

    // email password_confirmation password common
    public final static String signUpEmailApi = baseApiPath + "/registers/email";
    // HTML
    public final static String htmlApi = baseApiPath + "/articles";
    public final static String articlesListApi = baseApiPath + "/articles/articles_list";
    // phone_number common
    public final static String getAuthCodeApi = baseApiPath + "/registers/code";
    public final static String ordersApi = baseApiPath + "/orders";
    //send a verify code for a logined member .
    public final static String authCodeFromSmsApi = baseApiPath + "/code";
    // phone_number code password_confirmation password common
    public final static String signUpPhoneApi = baseApiPath + "/registers/phone_number";

    public final static String upDatePhoneApi = baseApiPath + "/user_managers/update_phone_number";
    // user_name login_password common
    public final static String signInApi = baseApiPath + "/login";

    public final static String changeloginpasswordApi = baseApiPath + "/user_managers/change_password";

    // private
    public final static String memberMeApi = baseApiPath + "/members/me";
    public final static String forgetPasswordApi = baseApiPath + "/";
    public final static String getbackPasswordEmailApi = baseApiPath + "/user_managers/reset_password_email";
    public final static String getbackPasswordPhoneApi = baseApiPath + "/user_managers/reset_password_phone";
    public final static String marketsApi = baseApiPath + "/markets";
    public final static String getTickersJSON = baseApiPath + "/tickers";
    public final static String getKJSON = baseApiPath + "/k";
    public final static String getKWithPendingTradeJSON = baseApiPath + "/k_with_pending_trades";
    public final static String getTimeApi = baseApiPath + "/timestamp";


    public final static String verifyCodeApi = baseApiPath + "/verify_code";

    public static final String[] publicApis = new String[]{
        signUpEmailApi,
        signUpPhoneApi,
        getAuthCodeApi,
        signInApi,
        getbackPasswordEmailApi,
        getbackPasswordPhoneApi,
        marketsApi,
        getTickersJSON,
        getKJSON,
        getKWithPendingTradeJSON
    };

    public final static String MONIT_PUBLIC_TRADES_PUSHER = "monit_public_trades_pusher";

}

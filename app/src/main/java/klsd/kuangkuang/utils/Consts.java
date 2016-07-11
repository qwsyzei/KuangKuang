package klsd.kuangkuang.utils;

import android.app.Application;

public class Consts extends Application {

    // API
    public final static String host = "http://119.254.211.82:8080";

public final static String host123="http://192.168.1.20:3000";
    public final static String host2 = "http://119.254.100.167:8080";
    public final static String baseApiPath = host123+ "/api/v2";

    // HTML
    public final static String articlesListApi = baseApiPath + "/articles/articles_list";
    public final static String articlesLikeApi = baseApiPath + "/articles/numbers";//点赞
    public final static String articlesCommentApi = baseApiPath + "/articles/comment";//评论
    public final static String articlesCommentaryApi = baseApiPath + "/articles/commentary";//文章所有评论
    // phone_number common
    public final static String getAuthCodeApi = baseApiPath + "/registers/code";
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

    public final static String getbackPasswordPhoneApi = baseApiPath + "/user_managers/reset_password_phone";

    public final static String getTimeApi = baseApiPath + "/timestamp";


    public final static String verifyCodeApi = baseApiPath + "/verify_code";

    public static final String[] publicApis = new String[]{

        signUpPhoneApi,
        getAuthCodeApi,
        signInApi,
            getTimeApi,
        getbackPasswordPhoneApi,
            articlesListApi,
            articlesCommentaryApi,
    };

    public final static String MONIT_PUBLIC_TRADES_PUSHER = "monit_public_trades_pusher";

}

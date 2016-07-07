package klsd.kuangkuang.utils;

import android.app.Application;

public class Consts extends Application {

    // API
    public final static String host = "http://119.254.211.82:8080";

public final static String host123="http://192.168.1.20:3000";
    public final static String host2 = "http://119.254.100.167:8080";
    public final static String baseApiPath = host + "/api/v2";

    // email password_confirmation password common
    public final static String signUpEmailApi = baseApiPath + "/registers/email";
    // HTML
    public final static String htmlApi = baseApiPath + "/articles";

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
    // market (limit timestamp from to order_by)
    public final static String tradesApi = baseApiPath + "/trades";
    // common market (limit timestamp from to order_by)
    public final static String tradesMyApi = baseApiPath + "/trades/my";

    public final static String tradesOrderIdApi = baseApiPath + "/trades/order_id";

    // common market (state limit page order_by)
    public final static String ordersApi = baseApiPath + "/orders";
    public final static String ordersClearApi = baseApiPath + "/orders/clear";
    public final static String orderDeleteApi = baseApiPath + "/order/delete";

    public final static String fundSourcesApi = baseApiPath + "/fund_sources";
    public final static String addFundSourcesApi = baseApiPath + "/fund_sources/add";
    public final static String updateFundSourcesApi = baseApiPath + "/fund_sources/update";
    public final static String deleteFundSourcesApi = baseApiPath + "/fund_sources/delete";

    public final static String rechargeApi = baseApiPath + "/deposit_bank/create";
    public final static String rechargeCancelApi = baseApiPath + "/deposit_bank/cancel";
    public final static String rechargeRecordApi = baseApiPath + "/deposits";
    public final static String recharge_detailApi = baseApiPath + "/deposit";

    public final static String withDraw_bankApi = baseApiPath + "/withdraw_bank/create";
    public final static String withDrawCancelApi = baseApiPath + "/withdraw_bank/cancel";
    public final static String withDrawsApi = baseApiPath + "/withdraws";
    public final static String withDrawApi = baseApiPath + "/withdraw";
    public final static String accountVersionsApi = baseApiPath + "/account_versions";
    public final static String accountVersionApi = baseApiPath + "/account_version";

    public final static String orderBookApi = baseApiPath + "/order_book";
    public final static String depthApi = baseApiPath + "/depth";

    // Pusher
    public final static String pusherAuthApi = baseApiPath + "/pushers/auth";

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
    public final static String existsPayPasswordApi = baseApiPath + "/trade_password/exists";
    public final static String createPayPasswordApi = baseApiPath + "/trade_password/create";
    public final static String updatePayPasswordApi = baseApiPath + "/trade_password/update";
    public final static String verifyPayPasswordApi = baseApiPath + "/trade_password/verify";

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

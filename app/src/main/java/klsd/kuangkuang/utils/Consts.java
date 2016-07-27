package klsd.kuangkuang.utils;

import android.app.Application;

public class Consts extends Application {

    // API
    public final static String host = "http://119.254.211.82:8080";

public final static String host123="http://192.168.31.167:3000";
    public final static String host2 = "http://119.254.100.167:8080";
    public final static String baseApiPath = host+ "/api/v2";

    // HTML
    public final static String articlesListApi = baseApiPath + "/articles/articles_list";
    public final static String articlesTopApi = baseApiPath + "/articles/Top10";
    public final static String articlesViewApi = baseApiPath + "/articles/numbers";//观看
    public final static String articlesCommentApi = baseApiPath + "/articles/comment";//评论
    public final static String articlesCommentaryApi = baseApiPath + "/articles/commentary";//文章所有评论
    public final static String articlesCollectArticleApi = baseApiPath + "/collect/collect_article";//收藏
    public final static String articlesCollectDestroyApi = baseApiPath + "/collect/collect_destroy";//取消收藏
    public final static String articlesCollectShowApi = baseApiPath + "/collect/collect_show";//展示收藏列表
    public final static String memberDocumentsApi = baseApiPath + "/member_documents";//展示个人资料
    public final static String memberUpdateDocumentsApi = baseApiPath + "/update/member_documents";//更新个人资料
    public final static String memberUpdateHeadApi = baseApiPath + "/update/member_head_portrait";//上传头像
    public final static String micropostsMemberListApi = baseApiPath + "/microposts/microposts_member_list";//我的说说
    public final static String deleteMywordApi = baseApiPath + "/microposts/destroy_microposts";//删除我的说说
    public final static String createMicropostsApi = baseApiPath + "/microposts/create_microposts";//发表说说
    public final static String micropostsListApi = baseApiPath + "/microposts/microposts_list";//朋友圈列表
    public final static String picture1Api = baseApiPath + "/microposts/picture1";//发图片1
    public final static String picture2Api = baseApiPath + "/microposts/picture2";//发图片2
    public final static String picture3Api = baseApiPath + "/microposts/picture3";//发图片3
    public final static String picture4Api = baseApiPath + "/microposts/picture4";//发图片4
    public final static String picture5Api = baseApiPath + "/microposts/picture5";//发图片5
    public final static String picture6Api = baseApiPath + "/microposts/picture6";//发图片6
    public final static String picture7Api = baseApiPath + "/microposts/picture7";//发图片7
    public final static String picture8Api = baseApiPath + "/microposts/picture8";//发图片8
    public final static String picture9Api = baseApiPath + "/microposts/picture9";//发图片9
    public final static String addLikeApi = baseApiPath + "/like/add_like";//点赞
    public final static String cancelLikeApi = baseApiPath + "/like/destroy_like";//取消赞
    public final static String commentCircleApi = baseApiPath + "/microposts/create_comment";//给圈子评论
    public final static String circlelikeListApi = baseApiPath + "/like/like_list";//赞列表
    public final static String circlecommentListApi = baseApiPath + "/microposts/comment_list";//朋友圈评论列表
    public final static String givesuggestApi = baseApiPath + "/suggest/give_suggest";//意见反馈

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
            articlesTopApi,
            articlesViewApi,
            micropostsListApi,
            circlecommentListApi,
            givesuggestApi
    };

    public final static String MONIT_PUBLIC_TRADES_PUSHER = "monit_public_trades_pusher";

}

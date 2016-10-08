package yksg.kuangkuang.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import yksg.kuangkuang.models.AllComment;
import yksg.kuangkuang.models.Blacklist;
import yksg.kuangkuang.models.Certificate;
import yksg.kuangkuang.models.CircleAllComment;
import yksg.kuangkuang.models.CircleLike;
import yksg.kuangkuang.models.Circles;
import yksg.kuangkuang.models.Documents;
import yksg.kuangkuang.models.Fans;
import yksg.kuangkuang.models.Follows;
import yksg.kuangkuang.models.Member;
import yksg.kuangkuang.models.MyCollect;
import yksg.kuangkuang.models.MyWord;
import yksg.kuangkuang.models.OHLCEntity;
import yksg.kuangkuang.models.Order;
import yksg.kuangkuang.models.Subject;
import yksg.kuangkuang.models.Top;

public class JSONHandler {
    Context ctx;
    String jsonString, jtype;
    Handler handler;
    public final static String JTYPE_TICKES = "tickers";
    public final static String JTYPE_LOGIN = "login";
    public final static String JTYPE_EXISTS = "exists";
    public final static String JTYPE_K = "kcharts";
    public final static String JTYPE_K_WITH_TRADES = "k_with_trades";
    public final static String JTYPE_ORDERS_BUY = "buy";
    public final static String JTYPE_GET_ORDERS = "get_orders";
    public final static String JTYPE_GET_ORDERS_ITEM = "get_orders_item";
    public final static String JTYPE_GET_DEPTH = "get_depth";
    public final static String JTYPE_GET_FUND_SOURCES = "get_fund_source";
    public final static String JTYPE_GET_TRADES = "get_trades";
    public final static String JTYPE_GET_ORDER_BOOK = "get_order_book";
    public final static String JTYPE_DELETE_ORDERS = "delete_orders";
    public final static String JTYPE_ORDERS_SELL = "sell";
    public final static String JTYPE_SIGN = "signup";
    public final static String JTYPE_RESET = "reset_password";
    public final static String JTYPE_BIND_PHONE = "bind_phone";
    public final static String JTYPE_MEMBER_ME = "member_me";
    public final static String JTYPE_UPDATE_PAY_PASSWORD = "update_pay_password";
    public final static String JTYPE_VERIFY_PAY_PASSWORD = "verify_pay_password";
    public final static String JTYPE_GET_TIME = "get_time";

    public final static String JTYPE_PUSHER_MARKET_TICKER = "pusher-market-ticker";
    public final static String JTYPE_PUSHER_PUBLIC_TRADES = "pusher-public-trades";

    public final static String JTYPE_PUSHER_PRIVATE_ORDER = "pusher-privat-order";

    public final static String JTYPE_RECHARGE = "recharge";
    public final static String JTYPE_RECHARGE_CANCEL = "recharge_cancel";
    public final static String JTYPE_DEPOSITS = "deposits";
    public final static String JTYPE_WITHDRAWS = "withdraws";
    public final static String JTYPE_SMS_AUTH_CODE = "sms_code";
    public final static String JTYPE_VERIFY_CODE = "verify_code";
    public final static String JTYPE_GET_ACCOUNT_VERSIONS = "account_versions";


    public final static String JTYPE_ARTICLES_LIST = "articles_list";
    public final static String JTYPE_ARTICLES_TOP = "articles_top";

    public final static String JTYPE_ARTICLES_LIKE = "articles_like";
    public final static String JTYPE_ARTICLES_CANCEL_LIKE = "cancel_articles_like";
    public final static String JTYPE_ARTICLES_VIEWS = "articles_views";
    public final static String JTYPE_ARTICLES_COMMENT = "articles_comment";
    public final static String JTYPE_ARTICLES_ALL_COMMENT = "articles_all_comment";
    public final static String JTYPE_CIRCLE_ALL_COMMENT = "circle_all_comment";
    public final static String JTYPE_COLLECT_SHOW = "collect_show";
    public final static String JTYPE_COLLECT = "collect_article";
    public final static String JTYPE_COLLECT_DESTROY = "collect_destroy";
    public final static String JTYPE_COMMENT_DESTROY = "comment_destroy";
    public final static String JTYPE_MEMBER_DOCUMENTS = "member_documents";
    public final static String JTYPE_MEMBER_UPDATE_DOCUMENTS = "update_member_documents";
    public final static String JTYPE_MEMBER_UPDATE_HEAD = "update_member_head";
    public final static String JTYPE_MYWORD_LIST = "myword_list";
    public final static String JTYPE_DELETE_MYWORD = "deletemyword";
    public final static String JTYPE_CREATE_WORDS = "create_words";
    public final static String JTYPE_CIRCLE_LIST = "circle_list";
    public final static String JTYPE_CIRCLE_LIKE_LIST = "circle_like_list";
    public final static String JTYPE_PICTURE1 = "picture1";
    public final static String JTYPE_PICTURE2 = "picture2";
    public final static String JTYPE_PICTURE3 = "picture3";
    public final static String JTYPE_PICTURE4 = "picture4";
    public final static String JTYPE_PICTURE5 = "picture5";
    public final static String JTYPE_PICTURE6 = "picture6";
    public final static String JTYPE_PICTURE7 = "picture7";
    public final static String JTYPE_PICTURE8 = "picture8";
    public final static String JTYPE_PICTURE9 = "picture9";
    public final static String JTYPE_GIVE_SUGGEST = "give_suggest";//意见反馈
    public final static String JTYPE_ADD_BLACK= "add_black_list";//加入黑名单
    public final static String JTYPE_DELETE_BLACK= "delete_black_list";//从黑名单移除
    public final static String JTYPE_BLACK_LIST= "black_list";//黑名单列表
    public final static String JTYPE_ADD_FOLLOW= "add_follow";//加入关注
    public final static String JTYPE_DELETE_FOLLOW= "delete_follow";//从关注移除
    public final static String JTYPE_FOLLOW_LIST= "follow_list";//关注列表
    public final static String JTYPE_FANS_LIST= "fans_list";//粉丝列表
    public final static String JTYPE_CERTIFICATE= "certificate";//证书查询
    public final static String JTYPE_CALCULATOR= "calculator";//计算器计算

    public JSONHandler() {
    }
    public JSONHandler(Context ctx) {
        this.ctx = ctx;
    }

    public JSONHandler(Context ctx, String jsonString, Handler handler, String jtype) {
        this.ctx = ctx;
        this.jsonString = jsonString;
        this.handler = handler;
        this.jtype = jtype;
    }

    public void parseJSON() {
        JSONTokener jsonParser = new JSONTokener(jsonString);
        Message message = new Message();
        Bundle bundle = new Bundle();
        Log.i("ParseJSON", jsonString);
        try {
            // 由于API返回的json不规则，分为Object和array两种判断
            if (jsonString.contains("\"error\"")) {
                JSONObject object = (JSONObject) jsonParser.nextValue();
                JSONObject error = object.getJSONObject("error");
                bundle.putString("error_code", error.getString("code"));
                bundle.putString("result", error.getString("message"));
                sendToHandler(message, bundle);
                return;
            }
            if (isMutipleObjectJtype(jtype)) {
                JSONObject object = (JSONObject) jsonParser.nextValue();
                if (jtype.equals(JTYPE_K_WITH_TRADES)) {
                    List<JSONArray> k = getArraysFromJArray(object.getJSONArray("k"));
                    List<JSONObject> trades = getObjectsFromJArray(object.getJSONArray("trades"));

                    fillBundle(bundle, k, JTYPE_K);
                    fillBundleObjects(bundle, trades, JTYPE_GET_TRADES);
                } else if (jtype.equals(JTYPE_PUSHER_PUBLIC_TRADES)) {
                    //{"trades": [{},{}]}
                    List<JSONObject> trades = getObjectsFromJArray(object.getJSONArray("trades"));
                    fillBundleObjects(bundle, trades, JTYPE_PUSHER_PUBLIC_TRADES);
                }
            } else if (isObjectJtype(jtype)) {
                JSONObject objj = (JSONObject) jsonParser.nextValue();
                JSONObject object = null;
                if (jtype.equals(JTYPE_TICKES)) {
                    object = objj.getJSONObject(DataCenter.getMarket());
                    object = object.getJSONObject("ticker");
                } else if (jtype.equals(JTYPE_PUSHER_MARKET_TICKER)) {
                    object = objj.getJSONObject(DataCenter.getMarket());
                } else {
                    object = objj;
                }
                fillBundle(bundle, object, jtype);
            } else {
                JSONArray arrayList = (JSONArray) jsonParser.nextValue();
                if (isObjectJtypeInJSONArray(jtype)) {
                    List<JSONObject> olistArrays = getObjectsFromJArray(arrayList);
                    fillBundleObjects(bundle, olistArrays, jtype);
                } else {
                    List<JSONArray> alistArrays = getArraysFromJArray(arrayList);
                    fillBundle(bundle, alistArrays, jtype);
                }
            }

            bundle.putString("jtype", jtype);
            bundle.putString("result", "OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendToHandler(message, bundle);
    }

    private void sendToHandler(Message message, Bundle bundle) {
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public List<JSONObject> getObjectsFromJArray(JSONArray arrayList) {
        List<JSONObject> olistArrays = new ArrayList<JSONObject>();
        for (int i = 0; i < arrayList.length(); i++) {
            olistArrays.add((JSONObject) arrayList.opt(i));
        }
        return olistArrays;
    }

    public List<JSONArray> getArraysFromJArray(JSONArray arrayList) {
        List<JSONArray> alistArrays = new ArrayList<JSONArray>();
        for (int i = 0; i < arrayList.length(); i++) {
            alistArrays.add(arrayList.optJSONArray(i));
        }
        return alistArrays;
    }

    private void fillBundleObjects(Bundle bundle, List<JSONObject> olistArrays,
                                   String jtype2) {
        try {
            if (jtype2.equals(JTYPE_GET_ORDERS)) {
                ArrayList<Order> orders = new ArrayList<Order>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    Order order = new Order(ctx);
                    order.getFromJSONObject(object);
                    orders.add(order);
                }
                bundle.putSerializable("orders", orders);
            } else if (jtype2.equals(JTYPE_ARTICLES_LIST)) {
                ArrayList<Subject> as = new ArrayList<Subject>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    JSONObject object11=object.getJSONObject("describe");
                    String nickname=object11.getString("nickname");
                    String picture_son=object11.getString("picture");
                    String signature=object11.getString("signature");
                    String describe_son=object11.getString("describe");
                    String follow_state=object11.getString("follow");
                    String is_like=object11.getString("is_like");
                    String is_collect=object11.getString("is_collect");
                    JSONObject object22=object.getJSONObject("picture");
                    String picture=object22.getString("url");
                    Subject sub = new Subject(ctx);
                    sub.getFromJSONObjectItem(object);
                    sub.getauthorInfo(nickname,picture_son,signature,describe_son,follow_state,is_like,is_collect,picture);
                    as.add(sub);
                }
                bundle.putSerializable("subject_article", as);
            } else if (jtype2.equals(JTYPE_MYWORD_LIST)) {  //类型特别  [ {"a":"11","b":{ "c":"12345","d":"11"}}  {"a2":"22","b2":{ "c2":"12345","d2":"22"}}]
                ArrayList<MyWord> as = new ArrayList<MyWord>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    MyWord sub = new MyWord(ctx);
                    JSONObject object1=object.getJSONObject("content");
                    String content=object1.getString("mic_content");//content就是上面的12345
                    String picture=object1.getString("picture");
                    String nickname=object1.getString("nickname");
                    String is_like=object1.getString("is_like");
                    sub.getFromJSONObjectItem(object);
                    sub.getcontentfrom(content,picture,nickname,is_like);
                    as.add(sub);
                }
                bundle.putSerializable("myword_list", as);
            }else if (jtype2.equals(JTYPE_CIRCLE_LIST)) {  //同mywordlist
                ArrayList<Circles> as = new ArrayList<Circles>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    Circles sub = new Circles(ctx);
                    JSONObject object1=object.getJSONObject("content");
                    String content=object1.getString("mic_content");
                    String nickname=object1.getString("nickname");
                    String picture_son=object1.getString("picture");
                    String signature=object1.getString("signature");
                    String followed_number=object1.getString("followed_number");
                    String follow_state=object1.getString("is_follow");
                    String islike=object1.getString("is_like");
                    sub.getFromJSONObjectItem(object);
                    sub.getcontentfrom(content,nickname,picture_son,signature,followed_number,follow_state,islike);
                    as.add(sub);
                }
                bundle.putSerializable("circle_list", as);
            } else if (jtype2.equals(JTYPE_ARTICLES_ALL_COMMENT)) {
                ArrayList<AllComment> ac = new ArrayList<AllComment>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    String object123=object.getString("object");
                    JSONObject object11=object.getJSONObject("commenter");
                    String nickname=object11.getString("nickname");
                    String picture_son=object11.getString("picture");
                    String object_id,object_nickname;
                    if (object123.equals("null")){//判断object字段是否是null
                        object_id="0";
                        object_nickname="0";
                    }else{
                        JSONObject object22=object.getJSONObject("object");
                        object_id=object22.getString("object_id");
                        object_nickname=object22.getString("nickname");

                    }
                    AllComment sub = new AllComment(ctx);
                    sub.getFromJSONObjectItem(object);
                    sub.getAuthorInfo(nickname, picture_son);
                    sub.getObjectInfo(object_id, object_nickname);
                    ac.add(sub);
                }
                bundle.putSerializable("all_comment", ac);
            }
            else if (jtype2.equals(JTYPE_CIRCLE_ALL_COMMENT)) {
                ArrayList<CircleAllComment> ac = new ArrayList<CircleAllComment>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    String object123=object.getString("object");
                    JSONObject object11=object.getJSONObject("content");
                    String nickname=object11.getString("nickname");
                    String picture_son=object11.getString("picture");
                    String content_text=object11.getString("content_text");

                    String object_id,object_nickname;
                    if (object123.equals("null")){//判断object字段是否是null
                         object_id="0";
                         object_nickname="0";
                    }else{
                        JSONObject object22=object.getJSONObject("object");
                    object_id=object22.getString("object_id");
                     object_nickname=object22.getString("nickname");

                    }
                    CircleAllComment sub = new CircleAllComment(ctx);
                    sub.getFromJSONObjectItem(object);
                    sub.getAuthorInfo(nickname, picture_son, content_text);
                    sub.getObjectInfo(object_id, object_nickname);
                    ac.add(sub);
                }
                bundle.putSerializable("circle_all_comment", ac);
            }else if (jtype2.equals(JTYPE_CIRCLE_LIKE_LIST)) {
                ArrayList<CircleLike> ac = new ArrayList<CircleLike>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    CircleLike sub = new CircleLike(ctx);
                    sub.getFromJSONObjectItem(object);
                    ac.add(sub);
                }
                bundle.putSerializable("circle_like_list", ac);
            }else if (jtype2.equals(JTYPE_COLLECT_SHOW)) {
                ArrayList<MyCollect> mc = new ArrayList<MyCollect>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    JSONObject object11=object.getJSONObject("describe");
                    String nickname=object11.getString("nickname");
                    String picture_son=object11.getString("picture");
                    String signature=object11.getString("signature");
                    String describe_son=object11.getString("describe");
                    String follow_state=object11.getString("follow");
                    String is_like=object11.getString("is_like");
                    String is_collect=object11.getString("is_collect");
                    JSONObject object22=object.getJSONObject("picture");
                    String picture=object22.getString("url");
                    MyCollect sub = new MyCollect(ctx);
                    sub.getFromJSONObjectItem(object);
                    sub.getauthorInfo(nickname, picture_son, signature, describe_son,follow_state,is_like,is_collect,picture);
                    mc.add(sub);
                }
                bundle.putSerializable("collect_show", mc);
            } else if (jtype2.equals(JTYPE_ARTICLES_TOP)) {
                ArrayList<Top> mc = new ArrayList<Top>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    JSONObject object11=object.getJSONObject("describe");
                    String nickname=object11.getString("nickname");
                    String picture_son=object11.getString("picture");
                    String signature=object11.getString("signature");
                    String describe_son=object11.getString("describe");
                    String follow_state=object11.getString("follow");
                    String is_like=object11.getString("is_like");
                    String is_collect=object11.getString("is_collect");
                    JSONObject object22=object.getJSONObject("picture");
                    String picture=object22.getString("url");
                    Top top = new Top(ctx);
                    top.getFromJSONObjectItem(object);
                    top.getauthorInfo(nickname, picture_son, signature, describe_son,follow_state,is_like,is_collect,picture);
                    mc.add(top);
                }
                bundle.putSerializable("top", mc);
            }else if (jtype2.equals(JTYPE_BLACK_LIST)) {
                ArrayList<Blacklist> mc = new ArrayList<Blacklist>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    JSONObject object11=object.getJSONObject("object");
                    String nickname=object11.getString("nickname");
                    String picture_son=object11.getString("picture");
                    String object_id=object11.getString("object_id");
                    Blacklist top = new Blacklist(ctx);
                    top.getFromJSONObjectItem(object);
                    top.getauthorInfo(nickname, picture_son,object_id);
                    mc.add(top);
                }
                bundle.putSerializable("blacklist", mc);
            }else if (jtype2.equals(JTYPE_FOLLOW_LIST)) {
                ArrayList<Follows> mc = new ArrayList<Follows>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    Follows top = new Follows(ctx);
                    top.getFromJSONObjectItem(object);
                    mc.add(top);
                }
                bundle.putSerializable("follows", mc);
            }else if (jtype2.equals(JTYPE_FANS_LIST)) {
                ArrayList<Fans> mc = new ArrayList<Fans>();
                for (int i = 0; i < olistArrays.size(); i++) {
                    JSONObject object = olistArrays.get(i);
                    Fans top = new Fans(ctx);
                    top.getFromJSONObjectItem(object);
                    mc.add(top);
                }
                bundle.putSerializable("fans", mc);
            }
        } catch (Exception e) {
        }
    }

    private void fillBundle(Bundle bundle, List<JSONArray> alistArrays,
                            String jtype2) {
        try {
            if (jtype2.equals(JTYPE_K)) {
                ArrayList<OHLCEntity> ohlcEntities = new ArrayList<OHLCEntity>();
                for (int i = 0; i < alistArrays.size(); i++) {
                    double open = (Double) alistArrays.get(i).opt(1);
                    double high = (Double) alistArrays.get(i).opt(2);
                    double low = (Double) alistArrays.get(i).opt(3);
                    double close = (Double) alistArrays.get(i).opt(4);
//					int vol = Integer.parseInt((alistArrays.get(i).opt(5) + "").replace(".0", ""));//原来的
                    double vol = Double.parseDouble(alistArrays.get(i).opt(5).toString().replace(".0", ""));//我后改的

                    long date = (Integer) alistArrays.get(i).opt(0);
                    OHLCEntity ohlcEntity = new OHLCEntity(open, high, low, close, date, vol);
                    ohlcEntities.add(ohlcEntity);
                }

                bundle.putSerializable("oHLCEs", ohlcEntities);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isObjectJtype(String jtype2) {
        List<String> osStrings = new ArrayList<String>();
        osStrings.add(JTYPE_TICKES);
        osStrings.add(JTYPE_PUSHER_MARKET_TICKER);
        osStrings.add(JTYPE_ORDERS_BUY);
        osStrings.add(JTYPE_DELETE_ORDERS);
        osStrings.add(JTYPE_RECHARGE_CANCEL);
        osStrings.add(JTYPE_ORDERS_SELL);
        osStrings.add(JTYPE_LOGIN);
        osStrings.add(JTYPE_SIGN);
        osStrings.add(JTYPE_RESET);
        osStrings.add(JTYPE_SMS_AUTH_CODE);
        osStrings.add(JTYPE_VERIFY_CODE);
        osStrings.add(JTYPE_BIND_PHONE);
        osStrings.add(JTYPE_MEMBER_ME);
        osStrings.add(JTYPE_GET_ORDER_BOOK);
        osStrings.add(JTYPE_RECHARGE);
        osStrings.add(JTYPE_PUSHER_PRIVATE_ORDER);


        osStrings.add(JTYPE_GET_TIME);
        osStrings.add(JTYPE_ARTICLES_LIKE);
        osStrings.add(JTYPE_ARTICLES_CANCEL_LIKE);
        osStrings.add(JTYPE_COLLECT);
        osStrings.add(JTYPE_COLLECT_DESTROY);
        osStrings.add(JTYPE_COMMENT_DESTROY);
        osStrings.add(JTYPE_DELETE_MYWORD);
        osStrings.add(JTYPE_ARTICLES_VIEWS);
        osStrings.add(JTYPE_ARTICLES_COMMENT);

        osStrings.add(JTYPE_MEMBER_DOCUMENTS);
        osStrings.add(JTYPE_MEMBER_UPDATE_DOCUMENTS);
        osStrings.add(JTYPE_MEMBER_UPDATE_HEAD);
        osStrings.add(JTYPE_CREATE_WORDS);
        osStrings.add(JTYPE_PICTURE1);
        osStrings.add(JTYPE_PICTURE2);
        osStrings.add(JTYPE_PICTURE3);
        osStrings.add(JTYPE_PICTURE4);
        osStrings.add(JTYPE_PICTURE5);
        osStrings.add(JTYPE_PICTURE6);
        osStrings.add(JTYPE_PICTURE7);
        osStrings.add(JTYPE_PICTURE8);
        osStrings.add(JTYPE_PICTURE9);
        osStrings.add(JTYPE_GIVE_SUGGEST);
        osStrings.add(JTYPE_ADD_BLACK);
        osStrings.add(JTYPE_DELETE_BLACK);
        osStrings.add(JTYPE_ADD_FOLLOW);
        osStrings.add(JTYPE_DELETE_FOLLOW);
        osStrings.add(JTYPE_CERTIFICATE);
        osStrings.add(JTYPE_CALCULATOR);
        return osStrings.contains(jtype2);
    }

    private boolean isMutipleObjectJtype(String jtype2) {
        List<String> osStrings = new ArrayList<String>();
        osStrings.add(JTYPE_K_WITH_TRADES);

        osStrings.add(JTYPE_GET_DEPTH);

        return osStrings.contains(jtype2);
    }

    private boolean isObjectJtypeInJSONArray(String jtype2) {
        List<String> osStrings = new ArrayList<String>();
        osStrings.add(JTYPE_GET_ORDERS_ITEM);
        osStrings.add(JTYPE_ARTICLES_LIST);
        osStrings.add(JTYPE_ARTICLES_ALL_COMMENT);
        osStrings.add(JTYPE_CIRCLE_ALL_COMMENT);
        osStrings.add(JTYPE_COLLECT_SHOW);
        osStrings.add(JTYPE_ARTICLES_TOP);
        osStrings.add(JTYPE_BLACK_LIST);
        osStrings.add(JTYPE_FOLLOW_LIST);
        osStrings.add(JTYPE_FANS_LIST);
        osStrings.add(JTYPE_GET_FUND_SOURCES);
        osStrings.add(JTYPE_GET_ACCOUNT_VERSIONS);
        osStrings.add(JTYPE_DEPOSITS);
        osStrings.add(JTYPE_WITHDRAWS);
        osStrings.add(JTYPE_MYWORD_LIST);
        osStrings.add(JTYPE_CIRCLE_LIST);
        osStrings.add(JTYPE_CIRCLE_LIKE_LIST);
        return osStrings.contains(jtype2);
    }

    public void fillBundle(Bundle bundle, JSONObject object, String jtype) {
        try {
            if (jtype.equals(JTYPE_MEMBER_ME)) {
                Member m = new Member();
                m.getFromJSONObject(object);
                bundle.putSerializable("member", m);
            } else if (jtype.equals(JTYPE_MEMBER_DOCUMENTS)) {
                Documents m = new Documents();
                m.getFromJSONObject(object);
                bundle.putSerializable("documents", m);
            } else if (jtype.equals(JTYPE_CERTIFICATE)) {
                Certificate m = new Certificate(ctx);
                m.getFromJSONObject(object);
                bundle.putSerializable("certificate", m);
            }else if (jtype.equals(JTYPE_LOGIN)) {
                DataCenter.setAccessKey(object.getString("access_key"));
                DataCenter.setSecretKey(object.getString("secret_key"));
                DataCenter.setMember_id(object.getString("member_id"));
                bundle.putBoolean("signed", true);
//	        	DataCenter.setSigned();
            } else if (jtype.equals(JTYPE_SIGN)) {
                String member_id = object.getString("member_id");
                bundle.putString("signup", member_id);
            } else if (jtype.equals(JTYPE_CREATE_WORDS)) {
                String id = object.getString("micropost");
                String url=object.getString("url");
                bundle.putString("id", id);
                bundle.putString("url", url);
            }else if (jtype.equals(JTYPE_CALCULATOR)) {
                String cny = object.getString("cny_price");
                String usd=object.getString("usd_price");
                String rate=object.getString("exchange_rate");
                bundle.putString("cny", cny);
                bundle.putString("usd", usd);
                bundle.putString("rate", rate);
            }else if (jtype.equals(JTYPE_RESET)) {
                bundle.putBoolean("reset_password", true);
            } else if (jtype.equals(JTYPE_RECHARGE)) {
                String data = object.toString();
                bundle.putSerializable("data", data);
            } else if (jtype.equals(JTYPE_EXISTS)) {
                boolean exsits = object.getBoolean("result");
                bundle.putSerializable("exsits", exsits);
            } else if (jtype.equals(JTYPE_BIND_PHONE)) {
                if (object.getBoolean("result")) {
                    bundle.putBoolean("bind_phone", true);
                }
            } else if (jtype.equals(JTYPE_UPDATE_PAY_PASSWORD)) {
                if (object.getBoolean("result")) {
                    bundle.putBoolean("success", true);
                }

            } else if (jtype.equals(JTYPE_PUSHER_PRIVATE_ORDER)) {
                Order order = new Order(ctx);
                order.getFromJSONObjectFromPusher(object);
                bundle.putSerializable("order", order);
            } else if (jtype.equals(JTYPE_VERIFY_PAY_PASSWORD)) {
                bundle.putBoolean(JTYPE_VERIFY_PAY_PASSWORD, true);
            } else if (jtype.equals(JTYPE_GET_TIME)) {
                long time = object.getLong("time");
                bundle.putSerializable("get_time", time);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package klsd.kuangkuang.utils;

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

import klsd.kuangkuang.models.AccountVersion;
import klsd.kuangkuang.models.Deposits;
import klsd.kuangkuang.models.Member;
import klsd.kuangkuang.models.OHLCEntity;
import klsd.kuangkuang.models.Order;
import klsd.kuangkuang.models.OrderId;

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
	public final static String JTYPE_CLEAR_ORDERS = "clear_orders";
	public final static String JTYPE_ORDERS_SELL = "sell";
	public final static String JTYPE_SIGN = "signup";
	public final static String JTYPE_RESET = "reset_password";
	public final static String JTYPE_BIND_PHONE = "bind_phone";
	public final static String JTYPE_MEMBER_ME = "member_me";
    public final static String JTYPE_AUTH_PUSHER = "auth_pusher";
    public final static String JTYPE_UPDATE_PAY_PASSWORD = "update_pay_password";
    public final static String JTYPE_VERIFY_PAY_PASSWORD = "verify_pay_password";
	public final static String JTYPE_GET_TIME = "get_time";
	
	public final static String JTYPE_PUSHER_MARKET_TICKER 	= "pusher-market-ticker";
	public final static String JTYPE_PUSHER_MARKET_TRADES 	= "pusher-market-trades";
	public final static String JTYPE_PUSHER_PUBLIC_TRADES 	= "pusher-public-trades";
	public final static String JTYPE_PUSHER_PUBLIC_UPDATE 	= "pusher-public-update";
	
	public final static String JTYPE_PUSHER_PRIVATE_ACCOUNT					= "pusher-privat-account";
	public final static String JTYPE_PUSHER_PRIVATE_ACCOUNTS				= "pusher-privat-accounts";
	public final static String JTYPE_PUSHER_PRIVATE_DEPOSITES				= "pusher-privat-deposits";
	public final static String JTYPE_PUSHER_PRIVATE_MEMBERS					= "pusher-privat-members";
	public final static String JTYPE_PUSHER_PRIVATE_DEPOSIT_ADDRESS			= "pusher-privat-deposit_address";
	public final static String JTYPE_PUSHER_PRIVATE_WITHDRAWS				= "pusher-privat-withdraws";
	public final static String JTYPE_PUSHER_PRIVATE_ORDER					= "pusher-privat-order";
	public final static String JTYPE_PUSHER_PRIVATE_TRADE				= "pusher-privat-trade";
	
	public final static String JTYPE_ADD_FUND_SOURCE 		= "add_fund_source";
	public final static String JTYPE_UPDATE_FUND_SOURCE 	= "update_fund_source";
	public final static String JTYPE_DELETE_FUND_SOURCE 	= "delete_fund_source";
	public final static String JTYPE_RECHARGE 				= "recharge";
	public final static String JTYPE_RECHARGE_CANCEL				= "recharge_cancel";
	public final static String JTYPE_DEPOSITS				= "deposits";
	public final static String JTYPE_DEPOSIT				= "deposit";
	public final static String JTYPE_WITHDRAW 				= "withdraw";
	public final static String JTYPE_WITHDRAWS				= "withdraws";
	public final static String JTYPE_WITHDRAW_CANCEL	= "withdraw_cancel";
	public final static String JTYPE_SMS_AUTH_CODE			= "sms_code";
	public final static String JTYPE_VERIFY_CODE			= "verify_code";
	public final static String JTYPE_GET_ACCOUNT_VERSIONS 	= "account_versions";
	public final static String JTYPE_GET_ACCOUNT_VERSION 	= "account_version";

	
	public JSONHandler(){
		
	}
	
	public JSONHandler(Context ctx){
		this.ctx = ctx;
	}
	
	public JSONHandler(Context ctx, String jsonString, Handler handler, String jtype) {
		this.ctx = ctx;
		this.jsonString = jsonString;
		this.handler = handler;
		this.jtype = jtype;
	}
	
	public void parseJSON(){
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
	    	if (isMutipleObjectJtype(jtype)){
		    	JSONObject object = (JSONObject) jsonParser.nextValue();
	    		if (jtype.equals(JTYPE_K_WITH_TRADES)){
			    	List<JSONArray> k = getArraysFromJArray(object.getJSONArray("k"));
			    	List<JSONObject> trades = getObjectsFromJArray(object.getJSONArray("trades"));

		            fillBundle(bundle, k, JTYPE_K);
		            fillBundleObjects(bundle, trades, JTYPE_GET_TRADES);
	    		}  else if (jtype.equals(JTYPE_PUSHER_PUBLIC_TRADES)){
	    			//{"trades": [{},{}]}
			    	List<JSONObject> trades = getObjectsFromJArray(object.getJSONArray("trades"));
		            fillBundleObjects(bundle, trades, JTYPE_PUSHER_PUBLIC_TRADES);
				}
	    	} else if (isObjectJtype(jtype)){
		    	JSONObject objj = (JSONObject) jsonParser.nextValue();
		    	JSONObject object = null;
	    		if (jtype.equals(JTYPE_TICKES)){
		    		object = objj.getJSONObject(DataCenter.getMarket());
		    		object = object.getJSONObject("ticker");
	    		} else if(jtype.equals(JTYPE_PUSHER_MARKET_TICKER)) {
                    object = objj.getJSONObject(DataCenter.getMarket());
	    		} else {
		    		object = objj;
	    		}
	            fillBundle(bundle, object, jtype);
	    	} else {
		    	JSONArray arrayList = (JSONArray) jsonParser.nextValue();
		    	if (isObjectJtypeInJSONArray(jtype)){
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

    private void sendToHandler(Message message, Bundle bundle){
        message.setData(bundle);
        handler.sendMessage(message);
    }
	
	public List<JSONObject> getObjectsFromJArray(JSONArray arrayList) {
    	List<JSONObject> olistArrays = new ArrayList<JSONObject>();
    	for (int i = 0;i < arrayList.length();i ++){
    		olistArrays.add((JSONObject)arrayList.opt(i));
    	}
    	return olistArrays;
	}
	
	public List<JSONArray> getArraysFromJArray(JSONArray arrayList) {
    	List<JSONArray> alistArrays = new ArrayList<JSONArray>();
    	for (int i = 0;i < arrayList.length();i ++){
    		alistArrays.add(arrayList.optJSONArray(i));
    	}
    	return alistArrays;
	}
	
	private void fillBundleObjects(Bundle bundle, List<JSONObject> olistArrays,
			String jtype2) {
		try {
			if (jtype2.equals(JTYPE_GET_ORDERS)){
				ArrayList<Order> orders = new ArrayList<Order>();
				for (int i=0;i<olistArrays.size();i++){
					JSONObject object = olistArrays.get(i);
					Order order = new Order(ctx);
					order.getFromJSONObject(object);
					orders.add(order);
				}
				bundle.putSerializable("orders", orders);
			} else if (jtype2.equals(JTYPE_GET_ORDERS_ITEM)){
				ArrayList<OrderId> orders = new ArrayList<OrderId>();
				for (int i=0;i<olistArrays.size();i++){
					JSONObject object = olistArrays.get(i);
					OrderId order = new OrderId(ctx);
					order.getFromJSONObjectItem(object);
					orders.add(order);
				}
				bundle.putSerializable("orders_item", orders);
			}   else if (jtype2.equals(JTYPE_GET_ACCOUNT_VERSIONS)){
				ArrayList<AccountVersion> avs = new ArrayList<AccountVersion>();
				for (int i=0;i<olistArrays.size();i++){
					JSONObject object = olistArrays.get(i);
					AccountVersion av = new AccountVersion();
					av.getFromJSONObject(object);
					avs.add(av);
				}
				bundle.putSerializable("account_versions", avs);
			} else if (jtype2.equals(JTYPE_DEPOSITS)){
				ArrayList<Deposits> ade = new ArrayList<Deposits>();
				for (int i=0;i<olistArrays.size();i++){
					JSONObject object = olistArrays.get(i);
					Deposits deposits = new Deposits();
					deposits.getFromJSONObject(object);
					ade.add(deposits);
				}

				bundle.putSerializable("deposits", ade);
			}
		} catch (Exception e) {
		}
	}

	private void fillBundle(Bundle bundle, List<JSONArray> alistArrays,
			String jtype2) {
		try {
			if (jtype2.equals(JTYPE_K)){
				ArrayList<OHLCEntity> ohlcEntities = new ArrayList<OHLCEntity>();
				for (int i=0;i<alistArrays.size();i++){
					double open = (Double) alistArrays.get(i).opt(1);
					double high = (Double) alistArrays.get(i).opt(2);
					double low = (Double) alistArrays.get(i).opt(3);
					double close = (Double) alistArrays.get(i).opt(4);
//					int vol = Integer.parseInt((alistArrays.get(i).opt(5) + "").replace(".0", ""));//原来的
					double vol= Double.parseDouble(alistArrays.get(i).opt(5).toString().replace(".0",""));//我后改的

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
		osStrings.add(JTYPE_EXISTS);
		osStrings.add(JTYPE_SIGN);
		osStrings.add(JTYPE_RESET);
		osStrings.add(JTYPE_SMS_AUTH_CODE);
		osStrings.add(JTYPE_VERIFY_CODE);
		osStrings.add(JTYPE_BIND_PHONE);
		osStrings.add(JTYPE_MEMBER_ME);
		osStrings.add(JTYPE_GET_ORDER_BOOK);
		osStrings.add(JTYPE_RECHARGE);
		osStrings.add(JTYPE_WITHDRAW);
		osStrings.add(JTYPE_WITHDRAW_CANCEL);

		osStrings.add(JTYPE_PUSHER_PRIVATE_ACCOUNT);
		osStrings.add(JTYPE_PUSHER_PRIVATE_ACCOUNTS);
		osStrings.add(JTYPE_PUSHER_PRIVATE_ORDER);
		osStrings.add(JTYPE_PUSHER_PRIVATE_TRADE);

		osStrings.add(JTYPE_ADD_FUND_SOURCE);
		osStrings.add(JTYPE_UPDATE_FUND_SOURCE);
		osStrings.add(JTYPE_DELETE_FUND_SOURCE);
		
		osStrings.add(JTYPE_VERIFY_PAY_PASSWORD);
		osStrings.add(JTYPE_UPDATE_PAY_PASSWORD);
		osStrings.add(JTYPE_GET_TIME);
		return osStrings.contains(jtype2);
	}

	private boolean isMutipleObjectJtype(String jtype2) {
		List<String> osStrings = new ArrayList<String>();
		osStrings.add(JTYPE_K_WITH_TRADES);
		osStrings.add(JTYPE_PUSHER_PUBLIC_UPDATE);
		osStrings.add(JTYPE_PUSHER_PUBLIC_TRADES);
		osStrings.add(JTYPE_GET_DEPTH);

		return osStrings.contains(jtype2);
	}
	
	private boolean isObjectJtypeInJSONArray(String jtype2) {
		List<String> osStrings = new ArrayList<String>();
		osStrings.add(JTYPE_GET_ORDERS);
		osStrings.add(JTYPE_GET_ORDERS_ITEM);

		osStrings.add(JTYPE_GET_TRADES);
		osStrings.add(JTYPE_GET_FUND_SOURCES);
		osStrings.add(JTYPE_GET_ACCOUNT_VERSIONS);
		osStrings.add(JTYPE_DEPOSITS);
		osStrings.add(JTYPE_WITHDRAWS);
		return osStrings.contains(jtype2);
	}

	public void fillBundle(Bundle bundle, JSONObject object, String jtype) {
		try {
			  if (jtype.equals(JTYPE_ORDERS_BUY) || jtype.equals(JTYPE_ORDERS_SELL)){
	        	Order order = new Order(ctx);
	        	order.getFromJSONObject(object);
				bundle.putSerializable("order", order);
	        } else if (jtype.equals(JTYPE_MEMBER_ME)) {
	        	Member m = new Member();
	        	m.getFromJSONObject(object);
				bundle.putSerializable("member", m);
	        } else if (jtype.equals(JTYPE_LOGIN)){
	        	DataCenter.setAccessKey(object.getString("access_key"));
	        	DataCenter.setSecretKey(object.getString("secret_key"));

				bundle.putBoolean("signed", true);
//	        	DataCenter.setSigned();
	        } else if (jtype.equals(JTYPE_SIGN)){
				 bundle.putBoolean("signup", true);
	        } else if (jtype.equals(JTYPE_RESET)){
				 bundle.putBoolean("reset_password", true);
			 }else if (jtype.equals(JTYPE_RECHARGE)){
				 String data=object.toString();
				 bundle.putSerializable("data", data);
			 }
			 else if (jtype.equals(JTYPE_EXISTS)){
				 boolean exsits=object.getBoolean("result");
				 bundle.putSerializable("exsits", exsits);
			 }else if (jtype.equals(JTYPE_BIND_PHONE)) {
				 if (object.getBoolean("result")) {
					 bundle.putBoolean("bind_phone", true);
				 }
			 }else if (jtype.equals(JTYPE_UPDATE_PAY_PASSWORD)){
                   if (object.getBoolean("result")){
					   bundle.putBoolean("success", true);
				   }

			 }else if (jtype.equals(JTYPE_K_WITH_TRADES)){
	        	
	        } else if (jtype.equals(JTYPE_PUSHER_PRIVATE_ORDER)){
	        	Order order = new Order(ctx);
	        	order.getFromJSONObjectFromPusher(object);
				bundle.putSerializable("order", order);
	        } else if (jtype.equals(JTYPE_VERIFY_PAY_PASSWORD)){
				bundle.putBoolean(JTYPE_VERIFY_PAY_PASSWORD, true);
			} else if(jtype.equals(JTYPE_GET_TIME)){
				long time=object.getLong("time");
				 bundle.putSerializable("get_time", time);
			 }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

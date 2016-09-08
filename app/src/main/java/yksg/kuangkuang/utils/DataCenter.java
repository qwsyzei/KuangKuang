package yksg.kuangkuang.utils;

import android.content.SharedPreferences;

import yksg.kuangkuang.models.Member;


public class DataCenter {
	public static final String LOGIN_NAME_TAG = "login_name";
	public static final String LOGIN_PASS_TAG = "login_pass";
	public static final String LOGIN_ACCESSKEY = "r7u3xuzdtqAgxadxaKMl8SMSogx1ZvXVkdJump40";
	public static final String LOGIN_SECRET = "xxiBqCaJKqcoDsCyuT7sGszf2x63w8Ucs4boRUY9";

	private static boolean isSign = false;
	private static String market;
	private static String payPassword, privateSN;
private static String member_id;
	private static Member member;
	// User Related
	public static String accessKey;
	public static String secretKey;
	public static SharedPreferences sharedPreferences;
	private static double curXZMValue;

	public static String getMember_id() {
		return member_id;
	}

	public static void setMember_id(String member_id) {
		DataCenter.member_id = member_id;
	}

	public static boolean isSigned() {
		return isSign;
	}

	public static String getSecretKey() {
		return secretKey;
	}

	public static void setSecretKey(String secretKey) {
		DataCenter.secretKey = secretKey;
	}

	public SharedPreferences getSharedPreferences() {
		return sharedPreferences;
	}

	public static void setSharedPreferences(SharedPreferences sharedPreferences) {
		DataCenter.sharedPreferences = sharedPreferences;
	}

	public static String getAccessKey() {
		return accessKey;
	}

	public static void setAccessKey(String accessKey) {
		DataCenter.accessKey = accessKey;
	}

	public static void setSigned() {
		isSign = true;
	}

	public static void setSignedOut() {
		isSign = false;
		accessKey = null;
		secretKey = null;
		member_id=null;
	}

	public static String getMarket() {
		return market == null ? "btccny" : market;
	}

	public static void setMarket(String market) {
		DataCenter.market = market;
	}

	public static String getPayPassword() {
//		return "123456";
		return payPassword;
	}

	public static void setPayPassword(String payPassword) {
		DataCenter.payPassword = payPassword;
	}

	public static String getPrivateSN() {
		return privateSN;
	}

	public static void setPrivateSN(String privateSN) {
		DataCenter.privateSN = privateSN;
	}

	public static Member getMember() {
		return member;
	}

	public static void setMember(Member member) {
		DataCenter.member = member;
	}

	public static double getCurXZMValue() {
		return curXZMValue;
	}

	public static void setCurXZMValue(double curXZMValue) {
		DataCenter.curXZMValue = curXZMValue;
	}
}

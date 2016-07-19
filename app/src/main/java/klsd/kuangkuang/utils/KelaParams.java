package klsd.kuangkuang.utils;

import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KelaParams {

    public static String generateSignature(String macData, String macKey) {

        String sig = "";
        try {

            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = macKey.getBytes("UTF-8");
            byte[] dataBytes = macData.getBytes("UTF-8");
            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");

            mac.init(secret);
            byte[] doFinal = mac.doFinal(dataBytes);
            sig = Hex.byte2HexStr(doFinal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sig;
    }

    public static RequestParams dealWithParams(HttpMethod method, String url, RequestParams params) {
        for (String purlString : Consts.publicApis) {
            if (purlString.equals(url) )
                return params;
        }

        if (DataCenter.isSigned()) {
            if (DataCenter.isSigned()) {
                params.addQueryStringParameter("access_key", DataCenter.getAccessKey());
                params.addQueryStringParameter("tonce", MyDate.getTonceInt() + "");
            }

            String mString = method.equals(HttpMethod.GET) ? "GET" : "POST";
            if (DataCenter.isSigned()) {
                String sig = getSignature(mString, url, paramsToHashMap(params), DataCenter.getSecretKey());
                params.addQueryStringParameter("signature", sig);
            }
        }
        return params;
    }

    public static HashMap<String, String> paramsToHashMap(RequestParams params) {
        HashMap<String, String> paramsHashMap = new HashMap<String, String>();
        List<NameValuePair> ps = params.getQueryStringParams();
        if (ps != null) {
            for (int i = 0; i < ps.size(); i++) {
                paramsHashMap.put(ps.get(i).getName(), ps.get(i).getValue());
            }
        }
        return paramsHashMap;
    }

    public static String paramsToString(RequestParams params) {
        return hashMapToVerString(paramsToHashMap(params));
    }

    public static String hashMapToVerString(HashMap<String, String> params) {
        String paramsStr = "";
        List<String> keysLis = new ArrayList<String>();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            keysLis.add(key + "=" + val);
        }

        Collections.sort(keysLis);

        String pname, beginString;
        for (int i = 0; i < keysLis.size(); i++) {
            pname = keysLis.get(i);
            beginString = i == 0 ? "" : "&";
            paramsStr += beginString + pname;
        }
        return paramsStr;
    }

    public static String getSignature(String mString, String url, HashMap<String, String> params, String secret) {
        String paramsStr = hashMapToVerString(params);
        String path = url.replace(Consts.host, "");
        String dataString = mString + "|" + path + "|" + paramsStr;
        String sig = KelaParams.generateSignature(dataString, secret);
        Log.i("PrivateParams: ", "curl -X " + mString + " " + url + " -d '" + paramsStr + "&signature=" + sig + "'");
        return sig;
    }

    public static String generateSignParamString(String mString, String url, RequestParams params) {
        return paramsToString(generateSignParam(mString, url, params));
    }


        //在不用登录的情况下使用
    public static RequestParams generateSignParam(String mString, String url, RequestParams params) {
        params.addQueryStringParameter("access_key", DataCenter.LOGIN_ACCESSKEY);
        params.addQueryStringParameter("tonce", MyDate.getTonceInt() + "");

        HashMap<String, String> hashMap = paramsToHashMap(params);
        String sig = getSignature(mString, url, hashMap, DataCenter.LOGIN_SECRET);
        params.addQueryStringParameter("signature", sig);
        return params;
    }
}
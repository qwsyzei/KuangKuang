package yksg.kuangkuang.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import yksg.kuangkuang.R;

public class MyHTTP {

    private Context ctx;
    String url, jtype;
    HttpRequest.HttpMethod method;
    Handler handler;
    RequestParams params;
    ProgressDialog progress;

    public MyHTTP(Context ctx) {
        this.ctx = ctx;
    }

    public void baseRequest(String url, String jtype, Handler handler) {
        baseRequest(url, jtype, HttpRequest.HttpMethod.GET, new RequestParams(), handler);
    }

    public void baseRequest(String url, String jtype, RequestParams params, Handler handler) {
        baseRequest(url, jtype, HttpRequest.HttpMethod.GET, params, handler);
    }

    public void baseRequest(final String url, final String jtype, final HttpRequest.HttpMethod method,
                            RequestParams _params, final Handler handler) {
            this.jtype = jtype;
            this.handler = handler;
            this.method = method;
            this.params = _params;
            this.url = url;
            if (params == null) this.params = new RequestParams();
            params.setHeader("request_from", "android");
            params = KelaParams.dealWithParams(method, url, params);
            new Thread() {
                public void run() {
                    sendOkHttp(url, jtype, method, params, handler);
                }
            }.start();
    }

    private static OkHttpClient okHttpClient;
    public static final MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient getInstance() {
        if (okHttpClient == null)
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

        return okHttpClient;
    }

    public void sendOkHttp(String url, String jtype, HttpRequest.HttpMethod method,
                           RequestParams params, final Handler handler) {
        if (method.equals(HttpRequest.HttpMethod.GET)) {
            doGet(url + "?" + KelaParams.paramsToString(params));
        } else {
            doPost(url, params);
        }
    }

    private String doGet(String url) {
        Request request = new Request.Builder().url(url)
                .build();
        return getResponseJSON(request);
    }

    public String doPost(String url, RequestParams params) {
        HashMap<String, String> textParams = KelaParams.paramsToHashMap(params);
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (textParams != null)
            for (String key : textParams.keySet())
                requestBodyBuilder.addFormDataPart(key, textParams.get(key));
        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.post(requestBodyBuilder.build()).build();
        return getResponseJSON(request);
    }

    private String getResponseJSON(Request request) {

        String res = "";
        try {
            Response response = getInstance().newCall(request).execute();
            res = response.body().string();
            Log.i("返回的JSON: ", "JSON: " + jtype + ": " + res);
        } catch (IOException e) {
            Log.d("", "Catch是什么 " + e.toString());
            Message message = new Message();
            Bundle bundle=new Bundle();

            bundle.putString("result",ctx.getString(R.string.checkup_network));
            message.setData(bundle);
            handler.sendMessage(message);
            e.printStackTrace();
        }
        new JSONHandler(ctx, res, handler, jtype).parseJSON();
        return res;
    }

}

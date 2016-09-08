package yksg.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 朋友圈赞列表实体类
 * Created by qiwei on 2016/7/26.
 */
public class CircleLike implements Serializable {
    String user_head_url;
private Context context;

    public CircleLike(Context context) {
        this.context = context;
    }

    public String getUser_head_url() {
        return user_head_url;
    }

    public void setUser_head_url(String user_head_url) {
        this.user_head_url = user_head_url;
    }
    public CircleLike getFromJSONObjectItem(JSONObject object) {
        try {
            setUser_head_url(object.getString("species"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 粉丝
 * Created by qiwei on 2016/8/29.
 */
public class Fans implements Serializable {

    private String nickname, picture_son;//名字  头像 url
    private String signature;
    private String object_id;
    private String isfollow;

    private Context context;

    public Fans(Context context) {
        this.context = context;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(String isfollow) {
        this.isfollow = isfollow;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPicture_son() {
        return picture_son;
    }

    public void setPicture_son(String picture_son) {
        this.picture_son = picture_son;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Fans getFromJSONObjectItem(JSONObject object) {
        try {
            setNickname(object.getString("nickname"));
            setPicture_son(object.getString("picture"));
            setObject_id(object.getString("object_id"));
            setIsfollow(object.getString("follow"));
            setSignature(object.getString("signature"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

}



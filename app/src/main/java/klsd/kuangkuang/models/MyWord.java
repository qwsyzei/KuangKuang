package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 我的说说实体类
 * Created by qiwei on 2016/7/14.
 */
public class MyWord implements Serializable{
    String id,content,member_id,created_at,picture_url;
    private Context context;

    public MyWord(Context context) {
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }
    public MyWord getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setContent(object.getString("content"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setPicture_url(object.getString("picture"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

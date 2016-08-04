package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 黑名单列表的实体类
 * Created by qiwei on 2016/8/4.
 */
public class Blacklist implements Serializable {
    private String id;
    private String member_id, created_at, updated_at;//
    private String nickname,picture_son;//名字  头像 url
    private String object_id;

    private Context context;

    public Blacklist(Context context) {
        this.context = context;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    public Blacklist getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setCreated_at(object.getString("created_at"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public Blacklist getauthorInfo(String nickname,String picture_son,String object_id) {
        try {
            setNickname(nickname);
            setPicture_son(picture_son);
            setObject_id(object_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by qiwei on 2016/7/26.
 */
public class CircleAllComment implements Serializable {
    private String id;
    private String created_at;
    private String updated_at;
    private String micropost_id;
    private String member_id;
    private String object;//对某条评论进行评论
    private String content_text, nickname, picture_son;

    private Context context;

    public CircleAllComment(Context context) {
        this.context = context;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMicropost_id() {
        return micropost_id;
    }

    public void setMicropost_id(String micropost_id) {
        this.micropost_id = micropost_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
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

    public CircleAllComment getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setCreated_at(object.getString("created_at"));
            setUpdated_at(object.getString("updated_at"));
            setObject(object.getString("object"));
            setMember_id(object.getString("member_id"));
            setMicropost_id(object.getString("micropost_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public CircleAllComment getAuthorInfo(String nickname, String picture_son, String content_text) {
        try {
            setPicture_son(picture_son);
            setNickname(nickname);
            setContent_text(content_text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

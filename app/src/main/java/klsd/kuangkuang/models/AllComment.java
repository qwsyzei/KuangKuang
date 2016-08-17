package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by qiwei on 2016/7/11.
 */
public class AllComment implements Serializable {
    private String id;
    private String body;//内容
    private String article_id;//文章编号
    private String created_at;
    private String updated_at;
private String nickname,picture_son;
    private String member_id;
private String object_id,object_nickname;
    private Context context;

    public AllComment(Context context) {
        this.context = context;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getObject_nickname() {
        return object_nickname;
    }

    public void setObject_nickname(String object_nickname) {
        this.object_nickname = object_nickname;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
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

    public AllComment getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setBody(object.getString("body"));
            setArticle_id(object.getString("article_id"));
            setCreated_at(object.getString("created_at"));
            setUpdated_at(object.getString("updated_at"));
            setMember_id(object.getString("member_id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public AllComment getAuthorInfo(String nickname,String picture_son) {
        try {
            setPicture_son(picture_son);
            setNickname(nickname);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public AllComment getObjectInfo(String object_id,String object_nickname) {
        try {
            setObject_id(object_id);
            setObject_nickname(object_nickname);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

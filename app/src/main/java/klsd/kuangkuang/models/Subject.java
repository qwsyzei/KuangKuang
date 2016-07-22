package klsd.kuangkuang.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;

/**
 * 专题的文章列表
 * Created by qiwei on 2016/7/6.
 */
public class Subject implements Serializable {
    private String id,content,title, describe_son, member_id, created_at, updated_at, tag, display;//标题，内容描述
    private String picture;//背景图

    private String views, like, comment;
    private String nickname,signature,picture_son;//作者名字  作者描述   作者头像 url
    private Bitmap head_pic;          //头像                                                     暂时不用

    private Context context;

    public Subject(Context context) {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe_son() {
        return describe_son;
    }

    public void setDescribe_son(String describe_son) {
        this.describe_son = describe_son;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(Bitmap head_pic) {
        this.head_pic = head_pic;
    }

    public Subject getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setContent(object.getString("content"));
            setTitle(object.getString("title"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setUpdated_at(object.getString("updated_at"));
           setPicture(object.getString("picture"));
            setTag(object.getString("tag"));
            setDisplay(object.getString("display"));
            setLike(object.getString("like"));
            setViews(object.getString("views"));
            setComment(object.getString("comment_number"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public Subject getauthorInfo(String nickname,String picture_son,String signature,String describe_son) {
        try {
            setNickname(nickname);
            setPicture_son(picture_son);
            setSignature(signature);
            setDescribe_son(describe_son);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

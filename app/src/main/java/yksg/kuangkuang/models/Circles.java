package yksg.kuangkuang.models;

import android.content.Context;
import android.graphics.Bitmap;


import org.json.JSONObject;

import java.io.Serializable;


/**
 * 圈子的实体类
 * Created by qiwei on 2016/7/8.
 */
public class Circles implements Serializable {
    private String id, member_id, created_at, updated_at;//标题，内容描述
    private String content_son, nickname, picture_son;//内容，作者名字，作者头像
    private String like, comment;
    private String signature,followed_number,follow_state;
    private Bitmap head;//这个是假的，回头删了
    private String url1, url2, url3, url4, url5, url6, url7, url8, url9;
    private String islike;
    private Context context;

    public Circles(Context context) {
        this.context = context;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getFollowed_number() {
        return followed_number;
    }

    public void setFollowed_number(String followed_number) {
        this.followed_number = followed_number;
    }

    public String getFollow_state() {
        return follow_state;
    }

    public void setFollow_state(String follow_state) {
        this.follow_state = follow_state;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getUrl3() {
        return url3;
    }

    public void setUrl3(String url3) {
        this.url3 = url3;
    }

    public String getUrl4() {
        return url4;
    }

    public void setUrl4(String url4) {
        this.url4 = url4;
    }

    public String getUrl5() {
        return url5;
    }

    public void setUrl5(String url5) {
        this.url5 = url5;
    }

    public String getUrl6() {
        return url6;
    }

    public void setUrl6(String url6) {
        this.url6 = url6;
    }

    public String getUrl7() {
        return url7;
    }

    public void setUrl7(String url7) {
        this.url7 = url7;
    }

    public String getUrl8() {
        return url8;
    }

    public void setUrl8(String url8) {
        this.url8 = url8;
    }

    public String getUrl9() {
        return url9;
    }

    public void setUrl9(String url9) {
        this.url9 = url9;
    }

    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getContent_son() {
        return content_son;
    }

    public void setContent_son(String content_son) {
        this.content_son = content_son;
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


    public Circles getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setUpdated_at(object.getString("updated_at"));
            setLike(object.getString("like_number"));
            setComment(object.getString("comment_number"));
            setUrl1(object.getString("url1"));
            setUrl2(object.getString("url2"));
            setUrl3(object.getString("url3"));
            setUrl4(object.getString("url4"));
            setUrl5(object.getString("url5"));
            setUrl6(object.getString("url6"));
            setUrl7(object.getString("url7"));
            setUrl8(object.getString("url8"));
            setUrl9(object.getString("url9"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Circles getcontentfrom(String content, String nickname, String picture_son,String signature,String followed_number,String follow_state,String islike) {
        try {
            setContent_son(content);
            setNickname(nickname);
            setPicture_son(picture_son);
            setSignature(signature);
            setFollowed_number(followed_number);
            setFollow_state(follow_state);
            setIslike(islike);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

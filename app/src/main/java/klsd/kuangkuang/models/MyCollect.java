package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 我的收藏列表的实体类
 * Created by qiwei on 2016/7/12.
 */
public class MyCollect implements Serializable {

    private Context context;

    public MyCollect(Context context) {
        this.context = context;
    }

    private String title, content, picture_url;
    private String describe_son,id;
    private String member_id, created_at, updated_at, tag, display;//暂时不用
    private String views, like, comment;
    private String nickname,signature,picture_son;//作者名字  作者描述   作者头像 url
    private String follow_state;
    private String is_like;
    private String is_collect;

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getFollow_state() {
        return follow_state;
    }

    public void setFollow_state(String follow_state) {
        this.follow_state = follow_state;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescribe_son() {
        return describe_son;
    }

    public void setDescribe_son(String describe_son) {
        this.describe_son = describe_son;
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

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public MyCollect getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setContent(object.getString("content"));
            setTitle(object.getString("title"));
            setPicture_url(object.getString("picture"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setUpdated_at(object.getString("updated_at"));
            setTag(object.getString("tag"));
            setDisplay(object.getString("display"));
            setViews(object.getString("views"));
            setLike(object.getString("like"));
            setComment(object.getString("comment_number"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
    public MyCollect getauthorInfo(String nickname,String picture_son,String signature,String describe_son,String follow_state,String is_like,String is_collect) {
        try {
            setNickname(nickname);
            setPicture_son(picture_son);
            setSignature(signature);
            setDescribe_son(describe_son);
            setFollow_state(follow_state);
            setIs_like(is_like);
            setIs_collect(is_collect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

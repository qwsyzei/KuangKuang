package klsd.kuangkuang.models;

import android.content.Context;


import org.json.JSONObject;

import java.io.Serializable;


/**
 * 圈子的实体类
 * Created by qiwei on 2016/7/8.
 */
public class Circles implements Serializable {
    private String id,content,title, describe, member_id, created_at, updated_at, tag, display;//标题，内容描述
    private String head_pic_url;//头像

    private String views, like, comment;//观看，点赞和评论    数量           暂时不用
    private String author;//作者名字                                                              暂时不用


    private Context context;

    public Circles(Context context) {
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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getHead_pic_url() {
        return head_pic_url;
    }

    public void setHead_pic_url(String head_pic_url) {
        this.head_pic_url = head_pic_url;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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



    public Circles getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setContent(object.getString("content"));
            setTitle(object.getString("title"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setUpdated_at(object.getString("updated_at"));
            setHead_pic_url(object.getString("picture"));
            setTag(object.getString("tag"));
            setDescribe(object.getString("describe"));
            setDisplay(object.getString("display"));
            setLike(object.getString("like"));
            setViews(object.getString("views"));
            setComment(object.getString("comment_number"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

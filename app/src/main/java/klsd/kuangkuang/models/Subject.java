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
    private String id,content,title, describe, member_id, created_at, updated_at, tag, display;//标题，内容描述
    private Bitmap picture;//背景图

    private String looked, praise, comment;//观看，点赞和评论    数量           暂时不用
    private String author;//作者名字                                                              暂时不用
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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLooked() {
        return looked;
    }

    public void setLooked(String looked) {
        this.looked = looked;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
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
           setPicture(BitmapFactory.decodeStream((new URL(object.getString("picture"))).openStream()));
            setTag(object.getString("tag"));
            setDescribe(object.getString("describe"));
            setDisplay(object.getString("display"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

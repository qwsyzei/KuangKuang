package yksg.kuangkuang.models;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.Serializable;

import yksg.kuangkuang.utils.MyDate;

/**
 * 我的说说实体类
 * Created by qiwei on 2016/7/14.
 */
public class MyWord implements Serializable {
    String id, member_id, created_at;
    private String url1, url2, url3, url4, url5, url6, url7, url8, url9;
    private String content_son, nickname, picture_son;
    private String like_number,comment_number;
    private String is_like;
    private Context context;
    String day, month;
    private Bitmap bitmip;//假的

    public MyWord(Context context) {
        this.context = context;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }

    public String getComment_number() {
        return comment_number;
    }

    public void setComment_number(String comment_number) {
        this.comment_number = comment_number;
    }

    public String getPicture_son() {
        return picture_son;
    }

    public void setPicture_son(String picture_son) {
        this.picture_son = picture_son;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent_son() {
        return content_son;
    }

    public void setContent_son(String content_son) {
        this.content_son = content_son;
    }

    public String getUrl9() {
        return url9;
    }

    public void setUrl9(String url9) {
        this.url9 = url9;
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

    public Bitmap getBitmip() {
        return bitmip;
    }

    public void setBitmip(Bitmap bitmip) {
        this.bitmip = bitmip;
    }

    public String getDay() {
        return MyDate.Day(created_at);
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return MyDate.month(created_at);
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String get_the_time() {    //日期
        return MyDate.yearmonthDay(created_at) ;
    }
    public String get_create_time() {    //时间
        String time="";
        try {
            time=MyDate. timeLogic(created_at) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public MyWord getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setLike_number(object.getString("like_number"));
            setComment_number(object.getString("comment_number"));
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

    public MyWord getcontentfrom(String content,String picture_son,String nickname,String is_like) {
        try {
            setContent_son(content);
            setPicture_son(picture_son);
            setNickname(nickname);
            setIs_like(is_like);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

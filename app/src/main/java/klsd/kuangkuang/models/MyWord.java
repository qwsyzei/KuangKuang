package klsd.kuangkuang.models;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 我的说说实体类
 * Created by qiwei on 2016/7/14.
 */
public class MyWord implements Serializable {
    String id, member_id, created_at;
    private String url1, url2, url3, url4, url5, url6, url7, url8, url9;
    private String content_son, nickname, picture_son;
    private Context context;
    String day, month;//假的
    private Bitmap bitmip;//假的

    public MyWord(Context context) {
        this.context = context;
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
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
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

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public MyWord getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setMember_id(object.getString("member_id"));
            setCreated_at(object.getString("created_at"));
            setContent_son(object.getString("content"));
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

    public MyWord getcontentfrom(String content) {
        try {
            setContent_son(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

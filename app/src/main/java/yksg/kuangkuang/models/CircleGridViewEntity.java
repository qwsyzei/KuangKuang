package yksg.kuangkuang.models;

import android.content.Context;

import java.io.Serializable;

/**
 * 圈子9宫格的实体类
 * Created by qiwei on 2016/7/8.
 */
public class CircleGridViewEntity implements Serializable {
    private String picture_url;


    private Context context;


    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public CircleGridViewEntity(Context context) {
        this.context = context;
    }

    public CircleGridViewEntity( Context context,String picture_url) {
        this.picture_url = picture_url;
        this.context = context;
    }

    //    public CircleGridViewEntity getFromJSONObjectItem(JSONObject object) {
//        try {
//            setPicture_url(object.getString("picture"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return this;
//    }
}


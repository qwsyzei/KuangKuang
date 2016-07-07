package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by qiwei on 2016/7/3.
 */
public class My_message implements Serializable {
    String title,time,content;
    private Context context;

    public My_message(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public My_message getFromJSONObjectItem(JSONObject object) {
        try {
            setTitle(object.getString("title"));
            setTime(object.getString("time"));
            setContent(object.getString("content"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

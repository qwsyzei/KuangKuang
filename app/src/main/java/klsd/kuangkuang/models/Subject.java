package klsd.kuangkuang.models;

import android.content.Context;
import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 专题的文章列表
 * Created by qiwei on 2016/7/6.
 */
public class Subject implements Serializable {
  private   String title,content,author;//标题，内容关键词，作者名字
   private String looked,praise,comment;//观看，点赞和评论    数量
private Bitmap background,head_pic;
    private Context context;

    public Subject(Context context) {
        this.context = context;
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

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public Bitmap getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(Bitmap head_pic) {
        this.head_pic = head_pic;
    }

    public Subject getFromJSONObjectItem(JSONObject object) {
        try {
            setTitle(object.getString("title"));
            setContent(object.getString("content"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}

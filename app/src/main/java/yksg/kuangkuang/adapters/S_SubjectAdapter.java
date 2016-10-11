package yksg.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.S_ArticleActivity;
import yksg.kuangkuang.models.Subject;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.ErrorCodes;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.KelaParams;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;

import static yksg.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 专题文章列表的adapter
 * Created by qiwei on 2016/7/6.
 */
public class S_SubjectAdapter extends ArrayAdapter<Subject> {

    private Context ctx;
    MyHTTP http;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private Picasso picasso;

    public S_SubjectAdapter(Context context, List<Subject> objects, Handler h) {
        super(context, R.layout.item_s_subject, objects);
        this.ctx = context;
        this.handler = h;
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                handlerBundler = msg.getData();
                responseJson = handlerBundler.getString("result");
                error_code = handlerBundler.getString("error_code");
                jtype = handlerBundler.getString("jtype");
                if (responseJson == null) {
                    //用于当用户在专题列表界面忽然断网也能进入文章
                } else if (responseJson.equals("OK")) {
                    updateData();
                } else {
                    toastError();
                }
            }
        };
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Subject subject = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_s_subject, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_subject_title);
            viewHolder.describe_son = (TextView) convertView.findViewById(R.id.item_subject_describe);
            viewHolder.tv_author_name = (TextView) convertView.findViewById(R.id.item_subject_author_name);
            viewHolder.views = (TextView) convertView.findViewById(R.id.item_subject_views);
            viewHolder.like = (TextView) convertView.findViewById(R.id.item_subject_like);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_subject_comment);
            viewHolder.im_picture = (ImageView) convertView.findViewById(R.id.item_subject_picture);
            viewHolder.layout_item = (LinearLayout) convertView.findViewById(R.id.layout_item_subject);
            viewHolder.im_head_pic = (ImageView) convertView.findViewById(R.id.item_subject_head_pic);
            viewHolder.tv_tag = (TextView) convertView.findViewById(R.id.item_subject_tag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(subject.getTitle());
        viewHolder.describe_son.setText(subject.getDescribe_son());
        if (subject.getViews().toString().equals("null")) {
            viewHolder.views.setText("0");
        } else if (subject.getViews().toString().contains(".0")) {
            viewHolder.views.setText(subject.getViews().replace(".0", ""));
        } else {
            viewHolder.views.setText(subject.getViews());
        }
        if (subject.getLike().toString().equals("null")) {
            viewHolder.like.setText("0");
        } else if (subject.getLike().toString().contains(".0")) {
            viewHolder.like.setText(subject.getLike().replace(".0", ""));
        } else {
            viewHolder.like.setText(subject.getLike());
        }
        if (subject.getComment().toString().equals("null")) {
            viewHolder.comment.setText("0");
        } else if (subject.getComment().toString().contains(".0")) {
            viewHolder.comment.setText(subject.getComment().replace(".0", ""));
        } else {
            viewHolder.comment.setText(subject.getComment());
        }
        if (subject.getNickname().equals("null")) {
            viewHolder.tv_author_name.setText("硄硄用户");
        } else {
            viewHolder.tv_author_name.setText(subject.getNickname());
        }
        viewHolder.tv_tag.setText("[" + subject.getTag() + "]");


        picasso.with(ctx).load(Consts.host + subject.getPicture()).into(viewHolder.im_picture);
        picasso.with(ctx).load(Consts.host + "/" + subject.getPicture_son()).into(viewHolder.im_head_pic);
        viewHolder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readread(subject.getId());//阅读
                Intent intent = new Intent(ctx, S_ArticleActivity.class);
                intent.putExtra("article_id", subject.getId());
                intent.putExtra("content_html", subject.getContent());
                intent.putExtra("title", subject.getTitle());
                intent.putExtra("tag", subject.getTag());
                intent.putExtra("views", subject.getViews());
                intent.putExtra("like", subject.getLike());
                intent.putExtra("comment", subject.getComment());
                intent.putExtra("created_at", subject.getCreated_at());
                intent.putExtra("nickname", subject.getNickname());
                intent.putExtra("picture_son", subject.getPicture_son());
                intent.putExtra("signature", subject.getSignature());
                intent.putExtra("author_member_id", subject.getMember_id());
                intent.putExtra("follow_state", subject.getFollow_state());
                intent.putExtra("is_like", subject.getIs_like());
                intent.putExtra("is_collect", subject.getIs_collect());
                ctx.startActivity(intent);
            }
        });


        return convertView;
    }

    /**
     * 阅读
     */
    private void readread(String article_id) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params = KelaParams.generateSignParam("GET", Consts.articlesViewApi, params);
        if (http == null) http = new MyHTTP(ctx);
        http.baseRequest(Consts.articlesViewApi, JSONHandler.JTYPE_ARTICLES_VIEWS, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_VIEWS)) {

        }
    }

    public void toastError() {
        try {
            ToastUtil.show(ctx, ErrorCodes.CODES.get(error_code));
        } catch (Exception e) {
            ToastUtil.show(ctx, responseJson);
        }
    }

    public final class ViewHolder {
        public TextView title, describe_son, views, like, comment;
        public ImageView im_picture;
        LinearLayout layout_item;
        ImageView im_head_pic;
        TextView tv_author_name;
        TextView tv_tag;
    }
}
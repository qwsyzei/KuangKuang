package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.S_ArticleActivity;
import klsd.kuangkuang.models.Top;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * Created by qiwei on 2016/7/12.
 */
public class S_TopAdapter extends ArrayAdapter<Top> {
    MyHTTP http;
    private Context ctx;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;

    public S_TopAdapter(Context context, List<Top> list, Handler h) {
        super(context, R.layout.item_top_ten, list);
        this.ctx = context;
        this.handler = h;
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                handlerBundler = msg.getData();
                responseJson = handlerBundler.getString("result");
                error_code = handlerBundler.getString("error_code");
                jtype = handlerBundler.getString("jtype");
                if (responseJson.equals("OK")) {
                    updateData();
                } else {
                    toastError();
                }
            }
        };
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Top ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_top_ten, null);
            viewHolder.top = (TextView) convertView.findViewById(R.id.item_top_tv);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_top_tv_title);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.item_top_tv_tag);
            viewHolder.im_pic = (ImageView) convertView.findViewById(R.id.item_top_im);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.top.setText("Top" + (position + 1) + "");
        viewHolder.title.setText(ac.getTitle());
        viewHolder.tag.setText(ac.getTag());
        Log.d("我在GIEDVIEW", "getView() returned: " + ac.getIs_like());
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(ac.getPicture_url(), viewHolder.im_pic);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestParams params = new RequestParams();
                params.addQueryStringParameter("article_id", ac.getId());
                params = KelaParams.generateSignParam("GET", Consts.articlesViewApi, params);
                if (http == null) http = new MyHTTP(ctx);
                http.baseRequest(Consts.articlesViewApi, JSONHandler.JTYPE_ARTICLES_VIEWS, HttpRequest.HttpMethod.GET,
                        params, handler);

                Intent intent = new Intent(ctx, S_ArticleActivity.class);
                intent.putExtra("article_id", ac.getId());
                intent.putExtra("content_html", ac.getContent());
                intent.putExtra("title", ac.getTitle());
                intent.putExtra("tag", ac.getTag());
                intent.putExtra("views", ac.getViews());
                intent.putExtra("like", ac.getLike());
                intent.putExtra("comment", ac.getComment());
                intent.putExtra("created_at", ac.getCreated_at());
                intent.putExtra("nickname", ac.getNickname());
                intent.putExtra("picture_son", ac.getPicture_son());
                intent.putExtra("signature", ac.getSignature());
                intent.putExtra("author_member_id", ac.getMember_id());
                intent.putExtra("follow_state", ac.getFollow_state());
                intent.putExtra("is_like",ac.getIs_like());
                intent.putExtra("is_collect",ac.getIs_collect());
                ctx.startActivity(intent);
            }
        });

        return convertView;
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
        TextView title, tag, top;
        ImageView im_pic;
    }
}


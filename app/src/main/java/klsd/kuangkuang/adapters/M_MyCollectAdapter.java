package klsd.kuangkuang.adapters;

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

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.S_ArticleActivity;
import klsd.kuangkuang.models.MyCollect;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ExitDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 我的收藏列表的adapter
 * Created by qiwei on 2016/7/12.
 */
public class M_MyCollectAdapter extends ArrayAdapter<MyCollect> {
    private ExitDialog exitDialog;
    private Context ctx;
    private TextView tv_cancel;
    private String aid;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;

    public M_MyCollectAdapter(Context context, List<MyCollect> objects, Handler h) {
        super(context, R.layout.item_my_collect, objects);
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
        final MyCollect ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_my_collect, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_my_collect_tv_title);
            viewHolder.describe_son = (TextView) convertView.findViewById(R.id.item_my_collect_tv_describe);
            viewHolder.im_pic = (ImageView) convertView.findViewById(R.id.item_my_collect_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        aid = ac.getId();
        viewHolder.title.setText(ac.getTitle());
        viewHolder.describe_son.setText(ac.getDescribe_son());
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
                ctx.startActivity(intent);

            }
        });
        convertView.setOnLongClickListener(longClickListener);
        return convertView;
    }

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Cancel_Dialog();
            return true;
        }
    };

    //窗口
    private void Cancel_Dialog() {
        exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.show();

        tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCollectCancel();
            }
        });


    }

    MyHTTP http;

    /**
     * 取消收藏
     */
    private void gotoCollectCancel() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", aid);
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        if (http == null) http = new MyHTTP(ctx);
        http.baseRequest(Consts.articlesCollectDestroyApi, JSONHandler.JTYPE_COLLECT_DESTROY, HttpRequest.HttpMethod.GET,
                params, handler);

    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_COLLECT_DESTROY)) {
            ToastUtil.show(ctx, "取消收藏成功");
            exitDialog.dismiss();
        }else if (jtype.equals(JSONHandler.JTYPE_ARTICLES_VIEWS)) {
            ToastUtil.show(ctx, "观看了");

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
        TextView title, describe_son;
        ImageView im_pic;


    }
}

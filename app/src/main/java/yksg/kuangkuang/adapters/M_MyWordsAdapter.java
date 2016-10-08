package yksg.kuangkuang.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.picasso.Picasso;
import java.util.List;
import yksg.kuangkuang.R;
import yksg.kuangkuang.main.M_CircleDetailActivity;
import yksg.kuangkuang.main.MainActivity;
import yksg.kuangkuang.models.MyWord;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.ErrorCodes;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.ExitDialog;

/**
 * 我的说说gridview的adapter
 * Created by qiwei on 2016/9/19.
 */
public class M_MyWordsAdapter extends ArrayAdapter<MyWord> {
    private Context ctx;
    private List<MyWord> list;
    private Picasso picasso;
    MyHTTP http;
    private ExitDialog exitDialog;
    private TextView tv_delete;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private int position123;
    public M_MyWordsAdapter(Context context, List<MyWord> list, Handler h) {
        super(context,R.layout.item_myword_gridview,list);
        this.ctx = context;
        this.list = list;
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
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView != null) {
            ret = convertView;
        } else {
            ViewHolder holder = null;
            ret = LayoutInflater.from(ctx).inflate(R.layout.item_myword_gridview, parent, false);
            holder = new ViewHolder();
            holder.bg_im = (ImageView) ret.findViewById(R.id.item_myword_gridview_im);
            holder.tv_time = (TextView) ret.findViewById(R.id.item_myword_gridview_tv_time);
            holder.tv_content = (TextView) ret.findViewById(R.id.item_myword_gridview_tv_content);
            ret.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) ret.getTag();

        picasso.with(ctx).load(Consts.host + "/" + list.get(position).getUrl1()).into(holder.bg_im);
        holder.tv_time.setText(list.get(position).get_create_time());
        holder.tv_content.setText(list.get(position).getContent_son());
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, M_CircleDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("head_pic", list.get(position).getPicture_son());
                intent.putExtra("created_at", list.get(position).getCreated_at());
                intent.putExtra("nickname", list.get(position).getNickname());
                intent.putExtra("content", list.get(position).getContent_son());
                intent.putExtra("like", list.get(position).getLike_number());
                intent.putExtra("comment", list.get(position).getComment_number());
                intent.putExtra("is_like",list.get(position).getIs_like());
                intent.putExtra("url1", list.get(position).getUrl1());
                intent.putExtra("url2", list.get(position).getUrl2());
                intent.putExtra("url3", list.get(position).getUrl3());
                intent.putExtra("url4", list.get(position).getUrl4());
                intent.putExtra("url5", list.get(position).getUrl5());
                intent.putExtra("url6", list.get(position).getUrl6());
                intent.putExtra("url7", list.get(position).getUrl7());
                intent.putExtra("url8", list.get(position).getUrl8());
                intent.putExtra("url9", list.get(position).getUrl9());
                intent.putExtra("url9", list.get(position).getUrl9());
                intent.putExtra("type", "me");
                ctx.startActivity(intent);
            }
        });
        ret.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                position123 = position;
                exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_delete);
                exitDialog.setCanceledOnTouchOutside(true);
                exitDialog.show();

                tv_delete = (TextView) exitDialog.findViewById(R.id.dialog_tv_delete_myword);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestParams params = new RequestParams();
                        params.addQueryStringParameter("id", list.get(position).getId());

                        if (http == null) http = new MyHTTP(ctx);
                        http.baseRequest(Consts.deleteMywordApi, JSONHandler.JTYPE_DELETE_MYWORD, HttpRequest.HttpMethod.GET,
                                params, handler);
                    }
                });
                return true;
            }
        });
        return ret;
    }
    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_DELETE_MYWORD)) {
            ToastUtil.show(ctx, R.string.delete_success);
            exitDialog.dismiss();
            Intent intent = new Intent(ctx, MainActivity.class);
            intent.putExtra("goto", "me");
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
        }
    }

    public void toastError() {
        try {
            ToastUtil.show(ctx, ErrorCodes.CODES.get(error_code));
        } catch (Exception e) {
            ToastUtil.show(ctx, responseJson);
        }
    }
    class ViewHolder {
        ImageView bg_im;
        TextView tv_time;
        TextView tv_content;
    }

}


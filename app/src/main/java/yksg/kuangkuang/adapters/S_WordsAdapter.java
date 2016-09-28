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
import yksg.kuangkuang.main.M_CircleDetailActivity;
import yksg.kuangkuang.models.MyWord;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.ErrorCodes;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyDate;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.ExitDialog;

import static yksg.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 其它朋友的说说adapter
 * Created by qiwei on 2016/8/31.
 */
public class S_WordsAdapter extends ArrayAdapter<MyWord> {
    MyHTTP http;
    private Context ctx;
    private ExitDialog exitDialog;
    private TextView tv_delete;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private List<MyWord> mylist;
    private int position123;
    private Picasso picasso;

    public S_WordsAdapter(Context context, List<MyWord> objects, Handler h) {
        super(context, R.layout.item_myword, objects);
        this.ctx = context;
        this.handler = h;
        this.mylist = objects;
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
        final MyWord ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_myword, null);
            viewHolder.content_son = (TextView) convertView.findViewById(R.id.item_myword_content);
            viewHolder.day = (TextView) convertView.findViewById(R.id.item_myword_day);
            viewHolder.month = (TextView) convertView.findViewById(R.id.item_myword_month);
            viewHolder.pic_url1 = (ImageView) convertView.findViewById(R.id.item_myword_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        picasso.with(ctx).load(Consts.host + "/" + ac.getUrl1()).into(viewHolder.pic_url1);
        String today = MyDate.todayDate();

        if (today.equals(ac.get_the_time())) {
            viewHolder.month.setVisibility(View.GONE);
            viewHolder.day.setText("今天");
        } else {
            viewHolder.day.setText(ac.getDay());
            viewHolder.month.setText(ac.getMonth() + "月");
        }
        viewHolder.content_son.setText(ac.getContent_son());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, M_CircleDetailActivity.class);
                intent.putExtra("id", ac.getId());
                intent.putExtra("head_pic", ac.getPicture_son());
                intent.putExtra("created_at", ac.getCreated_at());
                intent.putExtra("nickname", ac.getNickname());
                intent.putExtra("content", ac.getContent_son());
                intent.putExtra("like", ac.getLike_number());
                intent.putExtra("comment", ac.getComment_number());
                intent.putExtra("url1", ac.getUrl1());
                intent.putExtra("url2", ac.getUrl2());
                intent.putExtra("url3", ac.getUrl3());
                intent.putExtra("url4", ac.getUrl4());
                intent.putExtra("url5", ac.getUrl5());
                intent.putExtra("url6", ac.getUrl6());
                intent.putExtra("url7", ac.getUrl7());
                intent.putExtra("url8", ac.getUrl8());
                intent.putExtra("url9", ac.getUrl9());
                intent.putExtra("url9", ac.getUrl9());
                intent.putExtra("type", "author");
                ctx.startActivity(intent);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
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
                        params.addQueryStringParameter("id", ac.getId());

                        if (http == null) http = new MyHTTP(ctx);
                        http.baseRequest(Consts.deleteMywordApi, JSONHandler.JTYPE_DELETE_MYWORD, HttpRequest.HttpMethod.GET,
                                params, handler);
                    }
                });
                return true;
            }
        });
        return convertView;
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_DELETE_MYWORD)) {
            ToastUtil.show(ctx, R.string.delete_success);
            exitDialog.dismiss();
            mylist.remove(position123);
            notifyDataSetChanged();

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
        TextView content_son, day, month;
        ImageView pic_url1;
    }
}


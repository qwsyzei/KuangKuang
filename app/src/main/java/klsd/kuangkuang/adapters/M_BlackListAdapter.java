package klsd.kuangkuang.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.M_BlackListActivity;
import klsd.kuangkuang.models.Blacklist;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ExitDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 黑名单列表的adapter
 * Created by qiwei on 2016/8/4.
 */
public class M_BlackListAdapter extends ArrayAdapter<Blacklist> {
    MyHTTP http;
    private Context ctx;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private ExitDialog exitDialog;
    private TextView tv_cancel;
    public M_BlackListAdapter(Context context, List<Blacklist> list,Handler h) {
        super(context, R.layout.item_blacklist, list);
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
        final Blacklist ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_blacklist, null);
            viewHolder.name= (TextView) convertView.findViewById(R.id.item_blacklist_tv_name);
            viewHolder.im_pic= (ImageView) convertView.findViewById(R.id.item_blacklist_im_head);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(ac.getNickname());
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(Consts.host+"/"+ac.getPicture_son(), viewHolder.im_pic);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
                exitDialog.setCanceledOnTouchOutside(true);
                exitDialog.show();

                tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
                tv_cancel.setText(R.string.delete_black_list);

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exitDialog.dismiss();
                        RequestParams params = new RequestParams();
                        params.addQueryStringParameter("object_id",ac.getObject_id() );
                        if (http == null) http = new MyHTTP(ctx);
                        http.baseRequest(Consts.deleteblacklistApi, JSONHandler.JTYPE_DELETE_BLACK, HttpRequest.HttpMethod.GET,
                                params, handler);
                    }
                });
                return true;
            }
        });
        return convertView;
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_DELETE_BLACK)) {
            ToastUtil.show(ctx, "移除成功");
            Intent intent=new Intent(ctx, M_BlackListActivity.class);
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
    public final class ViewHolder {
        TextView name;
        ImageView im_pic;
    }
}


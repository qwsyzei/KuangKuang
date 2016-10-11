package yksg.kuangkuang.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
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
import yksg.kuangkuang.models.Fans;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.ErrorCodes;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.ExitDialog;

import static yksg.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 粉丝列表的adapter
 * Created by qiwei on 2016/8/29.
 */
public class M_FansListAdapter extends ArrayAdapter<Fans> {
    MyHTTP http;
    private Context ctx;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private ExitDialog exitDialog;
    private TextView tv_cancel;
    List<Fans> mylist;
    private int position123;
    private Picasso picasso;

    public M_FansListAdapter(Context context, List<Fans> list, Handler h) {
        super(context, R.layout.item_fanslist, list);
        this.ctx = context;
        this.handler = h;
        this.mylist = list;
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
        final Fans ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_fanslist, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_fanslist_tv_name);
            viewHolder.signature = (TextView) convertView.findViewById(R.id.item_fanslist_tv_signature);
            viewHolder.im_pic = (ImageView) convertView.findViewById(R.id.item_fanslist_im_head);
            viewHolder.im_follow_state = (ImageView) convertView.findViewById(R.id.item_fanslist_im_follow);
            viewHolder.tv_follow_state = (TextView) convertView.findViewById(R.id.item_fanslist_tv_im_tv);
            viewHolder.layout_follow_state = (LinearLayout) convertView.findViewById(R.id.layout_item_fanslist_follow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(ac.getNickname());
        if (ac.getSignature().equals("null")) {
            viewHolder.signature.setText(R.string.too_lazy);
        } else {
            viewHolder.signature.setText(ac.getSignature());
        }

        picasso.with(ctx).load(Consts.host + "/" + ac.getPicture_son()).into(viewHolder.im_pic);
        if (ac.getIsfollow().equals("1")) {
            viewHolder.im_follow_state.setImageResource(R.mipmap.follow_done01);
            viewHolder.tv_follow_state.setText(R.string.already_follows);
        } else if (ac.getIsfollow().equals("2")) {
            viewHolder.im_follow_state.setImageResource(R.mipmap.follow_done);
            viewHolder.tv_follow_state.setText(R.string.already_follows);
        } else {
            viewHolder.im_follow_state.setImageResource(R.mipmap.follow_togo);
        }
        viewHolder.layout_follow_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ac.getIsfollow().equals("0")) {
                    position123 = position;
                    exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
                    exitDialog.setCanceledOnTouchOutside(true);
                    exitDialog.show();

                    tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
                    tv_cancel.setText(R.string.add_follows);

                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            AddFollow(ac.getObject_id());//加关注
                        }
                    });
                }
            }
        });

        return convertView;
    }

    /**
     * 加关注
     */
    private void AddFollow(String object_id) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("object", object_id);
        if (http == null) http = new MyHTTP(ctx);
        http.baseRequest(Consts.addfollowsApi, JSONHandler.JTYPE_ADD_FOLLOW, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_ADD_FOLLOW)) {
            ToastUtil.show(ctx, R.string.add_follows_success);
            //关注成功后把图标更换了
            mylist.get(position123).setIsfollow("2");
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
        TextView name, signature;
        TextView tv_follow_state;
        ImageView im_pic;
        ImageView im_follow_state;
        LinearLayout layout_follow_state;
    }
}



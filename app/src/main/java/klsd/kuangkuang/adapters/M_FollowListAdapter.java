package klsd.kuangkuang.adapters;

import android.content.Context;
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
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.Follows;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ExitDialog;
import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 关注列表adapter
 * Created by qiwei on 2016/8/18.
 */
public class M_FollowListAdapter extends ArrayAdapter<Follows> {
    MyHTTP http;
    private Context ctx;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private ExitDialog exitDialog;
    private TextView tv_cancel;
    List<Follows> mylist;
    private int position123;
    public M_FollowListAdapter(Context context, List<Follows> list,Handler h) {
        super(context, R.layout.item_followlist, list);
        this.ctx = context;
        this.handler = h;
        this.mylist=list;
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
        final Follows ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_followlist, null);
            viewHolder.name= (TextView) convertView.findViewById(R.id.item_followlist_tv_name);
            viewHolder.signature= (TextView) convertView.findViewById(R.id.item_followlist_tv_signature);
            viewHolder.im_pic= (ImageView) convertView.findViewById(R.id.item_followlist_im_head);
            viewHolder.im_follow_state= (ImageView) convertView.findViewById(R.id.item_followlist_im_follow);
            viewHolder.tv_follow_state= (TextView) convertView.findViewById(R.id.item_followlist_tv_im_tv);
            viewHolder.layout_follow_state= (LinearLayout) convertView.findViewById(R.id.layout_item_followlist_follow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(ac.getNickname());
        if (ac.getSignature().equals("null")){
            viewHolder.signature.setText(R.string.too_lazy);
        }else{
            viewHolder.signature.setText(ac.getSignature());
        }
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(Consts.host + "/" + ac.getPicture_son(), viewHolder.im_pic);
viewHolder.layout_follow_state.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        position123=position;
        exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.show();

        tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
        tv_cancel.setText(R.string.cancel_follow);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("object",ac.getObject_id());
                if (http == null) http = new MyHTTP(ctx);
                http.baseRequest(Consts.destroyfollowsApi, JSONHandler.JTYPE_DELETE_FOLLOW, HttpRequest.HttpMethod.GET,
                        params, handler);
            }
        });
    }
});

        return convertView;
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_DELETE_FOLLOW)) {
            ToastUtil.show(ctx, R.string.success_cancel_follow);
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
        TextView name,signature;
        ImageView im_pic;
        TextView tv_follow_state;
        ImageView im_follow_state;
        LinearLayout layout_follow_state;
    }
}



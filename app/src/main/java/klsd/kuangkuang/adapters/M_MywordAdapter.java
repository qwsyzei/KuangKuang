package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import klsd.kuangkuang.fragments.MMeFragment;
import klsd.kuangkuang.main.M_CircleDetailActivity;
import klsd.kuangkuang.main.MainActivity;
import klsd.kuangkuang.models.MyWord;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ExitDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 我的说说adapter
 * Created by qiwei on 2016/7/14.
 */
public class M_MywordAdapter extends ArrayAdapter<MyWord> {
    MyHTTP http;
    private Context ctx;
    private ExitDialog exitDialog;
    private TextView tv_delete;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private Fragment fragment;

    public M_MywordAdapter(Context context, List<MyWord> objects, Handler h) {
        super(context, R.layout.item_myword, objects);
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

        viewHolder.content_son.setText(ac.getContent_son());
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(Consts.host + "/" + ac.getUrl1(), viewHolder.pic_url1);

        viewHolder.day.setText(ac.getDay());
        viewHolder.month.setText(ac.getMonth());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, M_CircleDetailActivity.class);
                intent.putExtra("id",ac.getId());
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
                ctx.startActivity(intent);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
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
            ToastUtil.show(ctx, "删除成功");
            exitDialog.dismiss();
            fragment=new MMeFragment();
            if (fragment != null) {
                switchFragment(fragment);
            }
        }
    }

    public void toastError() {
        try {
            ToastUtil.show(ctx, ErrorCodes.CODES.get(error_code));
        } catch (Exception e) {
            ToastUtil.show(ctx, responseJson);
        }
    }
    /**
     * 切换fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        if (ctx== null) {
            return;
        }
        if (ctx instanceof MainActivity) {
            MainActivity fca = (MainActivity) ctx;
            fca.switchConent(fragment);
        }
    }
    public final class ViewHolder {
        TextView content_son, day, month;
        ImageView pic_url1;

    }
}

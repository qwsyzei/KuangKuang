package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.fragments.MCircleFragment;
import klsd.kuangkuang.main.C_CircleAllCommentActivity;
import klsd.kuangkuang.main.ImagePagerActivity;
import klsd.kuangkuang.main.MainActivity;
import klsd.kuangkuang.models.Circles;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.CircleImageView;
import klsd.kuangkuang.views.ExitDialog;
import klsd.kuangkuang.views.MoreDialog;
import klsd.kuangkuang.views.SelfGridView;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 圈子的adapter
 * Created by qiwei on 2016/7/8.
 */
public class C_CircleAdapter extends ArrayAdapter<Circles> {
    MyHTTP http;
    private Context ctx;
    private int number;//9宫格图片的个数
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    private ExitDialog exitDialog;
    private MoreDialog moreDialog;
    private TextView tv_yes,tv_no,tv_title;
    private TextView tv_dialog_cancel;
private LinearLayout layout_black,layout_tip_off;
    private List<CircleGridViewEntity> headerEntitiesList;
    private C_CircleGridAdapter cGridAdapter;
    private Fragment fragment;

    public C_CircleAdapter(Context context, List<Circles> objects, Handler h) {
        super(context, R.layout.item_circle, objects);
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
        final Circles circles = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_circle, null);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.item_circle_describe);
            viewHolder.author = (TextView) convertView.findViewById(R.id.item_circle_author_name);
            viewHolder.like = (TextView) convertView.findViewById(R.id.item_circle_like);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_circle_comment);
            viewHolder.im_head_pic = (CircleImageView) convertView.findViewById(R.id.item_circle_head_pic);
            viewHolder.selfGridView = (SelfGridView) convertView.findViewById(R.id.gridview_circle);
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_circle_time);
            viewHolder.layout_like = (LinearLayout) convertView.findViewById(R.id.layout_item_circle_like);
            viewHolder.layout_comment = (LinearLayout) convertView.findViewById(R.id.layout_item_circle_comment);
            viewHolder.im_black= (ImageView) convertView.findViewById(R.id.item_circle_black);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (circles.getUrl1().equals("null")) {
            number = 0;
        } else if (circles.getUrl2().equals("null")) {
            number = 1;
        } else if (circles.getUrl3().equals("null")) {
            number = 2;
        } else if (circles.getUrl4().equals("null")) {
            number = 3;
        } else if (circles.getUrl5().equals("null")) {
            number = 4;
        } else if (circles.getUrl6().equals("null")) {
            number = 5;
        } else if (circles.getUrl7().equals("null")) {
            number = 6;
        } else if (circles.getUrl8().equals("null")) {
            number = 7;
        } else if (circles.getUrl9().equals("null")) {
            number = 8;
        } else {
            number = 9;
        }
        headerEntitiesList = new ArrayList<>();

        String[] url = new String[]{circles.getUrl1(), circles.getUrl2(), circles.getUrl3(), circles.getUrl4(), circles.getUrl5(), circles.getUrl6(), circles.getUrl7(), circles.getUrl8(), circles.getUrl9()};
        for (int i = 0; i < number; i++) {
            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, Consts.host + "/" + url[i]);
            headerEntitiesList.add(cirEntity);
        }
        final ArrayList<String> imageUrls;//9宫格URL列表(final必须加，目前不知道为什么)
        imageUrls = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            imageUrls.add(Consts.host + "/" + url[i]);
        }
        viewHolder.layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DataCenter.isSigned()) {
                    //点赞
                    RequestParams params = new RequestParams();
                    params.addQueryStringParameter("object_id", circles.getId());
                    params.addQueryStringParameter("species", "micropost");
                    if (http == null) http = new MyHTTP(ctx);
                    http.baseRequest(Consts.addLikeApi, JSONHandler.JTYPE_ARTICLES_LIKE, HttpRequest.HttpMethod.GET,
                            params, handler);
                } else {
                    ToastUtil.show(ctx, R.string.not_login);
                }

            }
        });
        viewHolder.im_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreDialog = new MoreDialog(ctx, R.style.dialog123);//创建Dialog并设置样式主题
                Window window = moreDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
                window.setWindowAnimations(R.style.mystyle);  //添加动画
                moreDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
                window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                moreDialog.show();

                layout_black = (LinearLayout) moreDialog.findViewById(R.id.layout_more_black);
                layout_tip_off = (LinearLayout) moreDialog.findViewById(R.id.layout_more_tip_off);
                tv_dialog_cancel = (TextView) moreDialog.findViewById(R.id.tv_dialog_cancel);
                layout_black.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moreDialog.dismiss();
                        if (DataCenter.isSigned()){
                            exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_exit);
                            exitDialog.show();
                            tv_title= (TextView) exitDialog.findViewById(R.id.dialog_exit_title);
                            tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
                            tv_no = (TextView) exitDialog.findViewById(R.id.exit_no);
                            tv_title.setText(R.string.if_add_black);
                            tv_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    exitDialog.dismiss();
                                    if (DataCenter.getMember_id().equals(circles.getMember_id())){
                                        ToastUtil.show(ctx,R.string.cannot_add_self_black_list);
                                    }else{
                                        //加入黑名单
                                        RequestParams params = new RequestParams();
                                        params.addQueryStringParameter("object_id", circles.getMember_id());
                                        if (http == null) http = new MyHTTP(ctx);
                                        http.baseRequest(Consts.addblacklistApi, JSONHandler.JTYPE_ADD_BLACK, HttpRequest.HttpMethod.GET,
                                                params, handler);
                                    }

                                }
                            });
                            tv_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    exitDialog.dismiss();
                                }
                            });
                        }else{
                            //提示未登录   或弹窗登录
                            ToastUtil.show(ctx,R.string.not_login);
                        }

                    }
                });
                layout_tip_off.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moreDialog.dismiss();
                        exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_exit);
                        exitDialog.show();
                        tv_title= (TextView) exitDialog.findViewById(R.id.dialog_exit_title);
                        tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
                        tv_no = (TextView) exitDialog.findViewById(R.id.exit_no);
                        tv_title.setText(R.string.if_tip_off);
                        tv_yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                exitDialog.dismiss();
                                //举报
                                RequestParams params = new RequestParams();
                                params.addQueryStringParameter("object_id", circles.getId());
                                params.addQueryStringParameter("species", "micropost");
                                params = KelaParams.generateSignParam("POST", Consts.givesuggestApi, params);
                                if (http == null) http = new MyHTTP(ctx);
                                http.baseRequest(Consts.givesuggestApi, JSONHandler.JTYPE_GIVE_SUGGEST, HttpRequest.HttpMethod.POST,
                                        params, handler);
                            }
                        });
                        tv_no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                exitDialog.dismiss();
                            }
                        });
                    }
                });
                tv_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moreDialog.dismiss();
                    }
                });

            }
        });
        viewHolder.layout_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, C_CircleAllCommentActivity.class);
                intent.putExtra("micropost_id", circles.getId());
                ctx.startActivity(intent);
            }
        });
        viewHolder.author.setText(circles.getNickname());
        viewHolder.describe.setText(circles.getContent_son());
        if (circles.getLike().equals("null")) {
            viewHolder.like.setText("0");
        } else if (circles.getLike().contains("0")) {
            viewHolder.like.setText(circles.getLike().replace(".0", ""));
        } else {
            viewHolder.like.setText(circles.getLike());
        }
        if (circles.getComment().equals("null")) {
            viewHolder.comment.setText("0");
        } else if (circles.getComment().contains("0")) {
            viewHolder.comment.setText(circles.getComment().replace(".0", ""));
        } else {
            viewHolder.comment.setText(circles.getComment());
        }

                Context context = ctx.getApplicationContext();
                initImageLoader(context);
                ImageLoader.getInstance().displayImage(Consts.host + "/" + circles.getPicture_son(), viewHolder.im_head_pic);

        String common_time = MyDate.timeLogic(circles.getCreated_at().substring(0, 19).replace("T", " "));
        viewHolder.time.setText(common_time);
        cGridAdapter = new C_CircleGridAdapter(ctx, headerEntitiesList);
        viewHolder.selfGridView.setAdapter(cGridAdapter);
        viewHolder.selfGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageBrower(i, imageUrls);
            }
        });

        return convertView;
    }
    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(ctx, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        ctx.startActivity(intent);
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIKE)) {
            ToastUtil.show(ctx, R.string.praise_success);
        }else if (jtype.equals(JSONHandler.JTYPE_ADD_BLACK)) {
            ToastUtil.show(ctx, R.string.add_black_success);
            fragment=new MCircleFragment();
            if (fragment != null) {
                switchFragment(fragment);
            }
        }else if (jtype.equals(JSONHandler.JTYPE_GIVE_SUGGEST)) {
            ToastUtil.show(ctx, R.string.tip_off_success);
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
        public TextView describe, like, comment;
        public CircleImageView im_head_pic;
        public SelfGridView selfGridView;
        TextView author, time;
        LinearLayout layout_like, layout_comment;
        ImageView im_black;
    }
}
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.S_AllCommentActivity;
import klsd.kuangkuang.models.AllComment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.ErrorCodes;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.CircleImageView;
import klsd.kuangkuang.views.ContainsEmojiEditText;
import klsd.kuangkuang.views.ExitDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;


/**
 * 获取所有评论的adapter
 * Created by qiwei on 2016/7/11.
 */
public class S_AllCommentAdapter extends ArrayAdapter<AllComment> {

    private Context ctx;
    private ExitDialog exitDialog;
    private TextView tv_yes, tv_cancel;
    private ContainsEmojiEditText editText;
    MyHTTP http;
    private Handler handler;
    String jtype, responseJson;
    String error_code;
    Bundle handlerBundler;
    int flag=0;

    public S_AllCommentAdapter(Context context, List<AllComment> objects, Handler h) {
        super(context, R.layout.item_s_allcomment, objects);
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
        final AllComment ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_s_allcomment, null);
            viewHolder.created_at = (TextView) convertView.findViewById(R.id.item_allcomment_time);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.item_allcomment_nickname);
            viewHolder.tv_replytext= (TextView) convertView.findViewById(R.id.item_comment_detail_replytext);
            viewHolder.im_head = (CircleImageView) convertView.findViewById(R.id.item_allcomment_head_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (ac.getObject_id().equals("0")&&ac.getMember_id().equals(DataCenter.getMember_id())) {//没有对谁，又是自己的
            viewHolder.tv_replytext.setText(ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
                    exitDialog.setCanceledOnTouchOutside(true);
                    exitDialog.show();

                    tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
                    tv_cancel.setText(R.string.delete_the_comment);

                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            RequestParams params = new RequestParams();
                            params.addQueryStringParameter("id",ac.getId());
                            params.addQueryStringParameter("article_id",ac.getArticle_id() );
                            if (http == null) http = new MyHTTP(ctx);
                            http.baseRequest(Consts.articlesDeleteCommentApi, JSONHandler.JTYPE_COMMENT_DESTROY, HttpRequest.HttpMethod.GET,
                                    params, handler);
                        }
                    });
                }
            });

        } else  if (ac.getObject_id().equals("0")&&!ac.getMember_id().equals(DataCenter.getMember_id())) {//没有对谁，是别人的
            viewHolder.tv_replytext.setText(ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_reply);
                    exitDialog.setCanceledOnTouchOutside(true);
                    exitDialog.show();
                    editText = (ContainsEmojiEditText) exitDialog.findViewById(R.id.dialog_reply_edit);
                    tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
                    tv_cancel = (TextView) exitDialog.findViewById(R.id.exit_no);
                    tv_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //调用回复评论的接口
                            RequestParams params = new RequestParams();
                            params.addQueryStringParameter("article_id", ac.getArticle_id());
                            params.addQueryStringParameter("comment", editText.getText().toString());
                            params.addQueryStringParameter("object", ac.getMember_id());

                            if (http == null) http = new MyHTTP(ctx);
                            http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                                    params, handler);
                        }
                    });
                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                        }
                    });
                }
            });


        }else if(!ac.getObject_id().equals("0")&&ac.getMember_id().equals(DataCenter.getMember_id())){//有对谁，是自己的
            viewHolder.tv_replytext.setText("回复  " + ac.getObject_nickname() + "：" + ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
                    exitDialog.setCanceledOnTouchOutside(true);
                    exitDialog.show();

                    tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
                    tv_cancel.setText(R.string.delete_the_comment);

                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            RequestParams params = new RequestParams();
                            params.addQueryStringParameter("id", ac.getId());
                            params.addQueryStringParameter("article_id", ac.getArticle_id());
                            if (http == null) http = new MyHTTP(ctx);
                            http.baseRequest(Consts.articlesDeleteCommentApi, JSONHandler.JTYPE_COMMENT_DESTROY, HttpRequest.HttpMethod.GET,
                                    params, handler);
                        }
                    });
                }
            });

        } else{                    //有对谁，是别人的
            viewHolder.tv_replytext.setText("回复  " + ac.getObject_nickname() + "：" + ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_reply);
                    exitDialog.setCanceledOnTouchOutside(true);
                    exitDialog.show();
                    editText = (ContainsEmojiEditText) exitDialog.findViewById(R.id.dialog_reply_edit);
                    tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
                    tv_cancel = (TextView) exitDialog.findViewById(R.id.exit_no);
                    tv_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //调用回复评论的接口
                            RequestParams params = new RequestParams();
                            params.addQueryStringParameter("article_id", ac.getArticle_id());
                            params.addQueryStringParameter("comment", editText.getText().toString());
                            params.addQueryStringParameter("object", ac.getMember_id());

                            if (http == null) http = new MyHTTP(ctx);
                            http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                                    params, handler);
                        }
                    });
                    tv_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                        }
                    });
                }
            });

        }
        String time = MyDate.timeLogic(ac.getCreated_at().substring(0, 19).replace("T", " "));
        viewHolder.created_at.setText(time);
        viewHolder.tv_nickname.setText(ac.getNickname());
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(Consts.host + "/" + ac.getPicture_son(), viewHolder.im_head);

        return convertView;
    }
    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(ctx, R.string.reply_success);
            Intent intent = new Intent(ctx, S_AllCommentActivity.class);
            intent.putExtra("a_id", S_AllCommentActivity.article_id);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
            exitDialog.dismiss();
        }else if (jtype.equals(JSONHandler.JTYPE_COMMENT_DESTROY)) {
            ToastUtil.show(ctx, R.string.delete_success);
            Intent intent = new Intent(ctx, S_AllCommentActivity.class);
            intent.putExtra("a_id", S_AllCommentActivity.article_id);
            ctx.startActivity(intent);
            ((Activity)ctx).finish();
            exitDialog.dismiss();
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
        public TextView  created_at;
        TextView tv_nickname;
        TextView tv_replytext;
        CircleImageView im_head;
    }
}

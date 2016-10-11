package yksg.kuangkuang.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.S_AllCommentActivity;
import yksg.kuangkuang.models.AllComment;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.DataCenter;
import yksg.kuangkuang.utils.ErrorCodes;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.MyDate;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.views.CircleImageView;
import yksg.kuangkuang.views.ContainsEmojiEditText;
import yksg.kuangkuang.views.ExitDialog;

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
    int flag = 0;
    private List<AllComment> mylist;
    private int position123;
    private Picasso picasso;

    public S_AllCommentAdapter(Context context, List<AllComment> objects, Handler h) {
        super(context, R.layout.item_s_allcomment, objects);
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
        final AllComment ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_s_allcomment, null);
            viewHolder.created_at = (TextView) convertView.findViewById(R.id.item_allcomment_time);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.item_allcomment_nickname);
            viewHolder.tv_replytext = (TextView) convertView.findViewById(R.id.item_comment_detail_replytext);
            viewHolder.im_head = (CircleImageView) convertView.findViewById(R.id.item_allcomment_head_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (ac.getObject_id().equals("0") && ac.getMember_id().equals(DataCenter.getMember_id())) {//没有对谁，又是自己的

            viewHolder.tv_replytext.setText(ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position123 = position;
                    if (DataCenter.isSigned()) {
                        dialog_delete(ac.getId(), ac.getArticle_id());//删除弹窗
                    } else {
                        ToastUtil.show(ctx, R.string.not_login_forbid);
                    }
                }
            });

        } else if (ac.getObject_id().equals("0") && !ac.getMember_id().equals(DataCenter.getMember_id())) {//没有对谁，是别人的
            viewHolder.tv_replytext.setText(ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position123 = position;
                    if (DataCenter.isSigned()) {
                        dialog_reply(ac.getArticle_id(), ac.getMember_id());//回复弹窗
                    } else {
                        ToastUtil.show(ctx, R.string.not_login_forbid);
                    }
                }
            });

        } else if (!ac.getObject_id().equals("0") && ac.getMember_id().equals(DataCenter.getMember_id())) {//有对谁，是自己的
            viewHolder.tv_replytext.setText("回复  " + ac.getObject_nickname() + "：" + ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position123 = position;
                    if (DataCenter.isSigned()) {
                        dialog_delete(ac.getId(), ac.getArticle_id());//删除弹窗
                    } else {
                        ToastUtil.show(ctx, R.string.not_login_forbid);
                    }
                }
            });

        } else {                    //有对谁，是别人的
            viewHolder.tv_replytext.setText("回复  " + ac.getObject_nickname() + "：" + ac.getBody());
            viewHolder.tv_replytext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position123 = position;
                    if (DataCenter.isSigned()) {
                        dialog_reply(ac.getArticle_id(), ac.getMember_id());//回复弹窗
                    } else {
                        ToastUtil.show(ctx, R.string.not_login_forbid);
                    }
                }
            });

        }
        String time = null;
        try {
            time = MyDate.timeLogic(ac.getCreated_at());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.created_at.setText(time);
        viewHolder.tv_nickname.setText(ac.getNickname());

        picasso.with(ctx).load(Consts.host + "/" + ac.getPicture_son()).into(viewHolder.im_head);
        return convertView;
    }

    /**
     * 删除弹窗
     */
    private void dialog_delete(final String idid, final String article_id) {
        exitDialog = new ExitDialog(ctx, R.style.MyDialogStyle, R.layout.dialog_cancel);
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.show();

        tv_cancel = (TextView) exitDialog.findViewById(R.id.dialog_tv_cancel_collect);
        tv_cancel.setText(R.string.delete_the_comment);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
                delete(idid, article_id);//删除
            }
        });
    }

    /**
     * 回复评论弹窗
     */
    private void dialog_reply(final String art_id, final String mem_id) {
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
                reply(art_id, editText.getText().toString(), mem_id);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog.dismiss();
            }
        });
    }

    /**
     * 删除评论
     *
     * @param comment_id
     * @param article_id
     */
    private void delete(String comment_id, String article_id) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", comment_id);
        params.addQueryStringParameter("article_id", article_id);
        if (http == null) http = new MyHTTP(ctx);
        http.baseRequest(Consts.articlesDeleteCommentApi, JSONHandler.JTYPE_COMMENT_DESTROY, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    /**
     * 回复评论
     */
    private void reply(String article_id, String comment, String object) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("article_id", article_id);
        params.addQueryStringParameter("comment", comment);
        params.addQueryStringParameter("object", object);
        if (http == null) http = new MyHTTP(ctx);
        http.baseRequest(Consts.articlesCommentApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, handler);
    }

    public void updateData() {
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(ctx, R.string.reply_success);
            InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘//清空数据并让它失去焦点
            exitDialog.dismiss();
            Intent intent = new Intent(ctx, S_AllCommentActivity.class);
            intent.putExtra("a_id", S_AllCommentActivity.article_id);
            ctx.startActivity(intent);
            ((Activity) ctx).finish();

        } else if (jtype.equals(JSONHandler.JTYPE_COMMENT_DESTROY)) {
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
        public TextView created_at;
        TextView tv_nickname;
        TextView tv_replytext;
        CircleImageView im_head;
    }
}

package klsd.kuangkuang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.AllComment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.views.CircleImageView;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;


/**
 * 获取所有评论的adapter
 * Created by qiwei on 2016/7/11.
 */
public class S_AllCommentAdapter extends ArrayAdapter<AllComment> {

    private Context ctx;

    public S_AllCommentAdapter(Context context, List<AllComment> objects) {
        super(context, R.layout.item_s_allcomment, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AllComment ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_s_allcomment, null);
            viewHolder.body = (TextView) convertView.findViewById(R.id.item_allcomment_body);
            viewHolder.created_at = (TextView) convertView.findViewById(R.id.item_allcomment_time);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.item_allcomment_nickname);
            viewHolder.im_head = (CircleImageView) convertView.findViewById(R.id.item_allcomment_head_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.body.setText(ac.getBody());
        String time = MyDate.timeLogic(ac.getCreated_at().substring(0, 19).replace("T", " "));
        viewHolder.created_at.setText(time);
        viewHolder.tv_nickname.setText(ac.getNickname());
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(Consts.host + "/" + ac.getPicture_son(), viewHolder.im_head);


        return convertView;
    }

    public final class ViewHolder {
        public TextView body, created_at;
        TextView tv_nickname;
        CircleImageView im_head;
    }
}

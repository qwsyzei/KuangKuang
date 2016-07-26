package klsd.kuangkuang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.CircleAllComment;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.views.CircleImageView;

/**
 * 说说评论列表的adapter
 * Created by qiwei on 2016/7/26.
 */
public class C_CircleCommentAdapter extends ArrayAdapter<CircleAllComment> {

    private Context ctx;

    public C_CircleCommentAdapter(Context context, List<CircleAllComment> objects) {
        super(context, R.layout.item_s_allcomment, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CircleAllComment ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_s_allcomment, null);
            viewHolder.content = (TextView) convertView.findViewById(R.id.item_allcomment_body);
            viewHolder.created_at = (TextView) convertView.findViewById(R.id.item_allcomment_time);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.item_allcomment_nickname);
            viewHolder.im_head = (CircleImageView) convertView.findViewById(R.id.item_allcomment_head_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(ac.getContent_text());
        String time = MyDate.timeLogic(ac.getCreated_at().substring(0, 19).replace("T", " "));
        viewHolder.created_at.setText(time);
        viewHolder.tv_nickname.setText(ac.getNickname());
        BitmapUtils bitmapUtils = new BitmapUtils(ctx);
        bitmapUtils.display(viewHolder.im_head, Consts.host+"/"+ac.getPicture_son());

        return convertView;
    }

    public final class ViewHolder {
        public TextView content, created_at;
        TextView tv_nickname;
        CircleImageView im_head;
    }
}

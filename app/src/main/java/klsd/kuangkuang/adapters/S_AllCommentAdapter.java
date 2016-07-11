package klsd.kuangkuang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import klsd.kuangkuang.R;
import klsd.kuangkuang.models.AllComment;


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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.body.setText(ac.getBody());
        viewHolder.created_at.setText(ac.getCreated_at());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        Intent intent=new Intent(ctx, S_ArticleActivity.class);
//        intent.putExtra("content_html",circle.getContent());
//        ctx.startActivity(intent);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView body, created_at;

    }
}

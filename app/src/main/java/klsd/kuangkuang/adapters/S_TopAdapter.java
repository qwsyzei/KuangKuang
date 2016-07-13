package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.S_ArticleActivity;
import klsd.kuangkuang.models.MyCollect;
import klsd.kuangkuang.models.Top;

/**
 * Created by qiwei on 2016/7/12.
 */
public class S_TopAdapter extends ArrayAdapter<Top> {

    private Context ctx;

    public S_TopAdapter(Context context, List<Top> objects) {
        super(context, R.layout.item_top_ten, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Top ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_top_ten, null);
            viewHolder.top= (TextView) convertView.findViewById(R.id.item_top_tv);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_top_tv_title);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.item_top_tv_tag);
            viewHolder.im_pic= (ImageView) convertView.findViewById(R.id.item_top_im);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(ac.getTitle());
        viewHolder.tag.setText(ac.getTag());
        BitmapUtils bitmapUtils=new BitmapUtils(ctx);
        bitmapUtils.display(viewHolder.im_pic,ac.getPicture_url());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, S_ArticleActivity.class);
                intent.putExtra("article_id", ac.getId());
                intent.putExtra("content_html", ac.getContent());
                ctx.startActivity(intent);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        TextView title, tag,top;
        ImageView im_pic;


    }
}


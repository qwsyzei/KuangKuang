package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.S_ArticleActivity;
import klsd.kuangkuang.models.MyCollect;
import klsd.kuangkuang.utils.MyHTTP;

/**
 * 我的收藏列表的adapter
 * Created by qiwei on 2016/7/12.
 */
public class M_MyCollectAdapter extends ArrayAdapter<MyCollect> {

    private Context ctx;

    public M_MyCollectAdapter(Context context, List<MyCollect> objects) {
        super(context, R.layout.item_my_collect, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyCollect ac = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_my_collect, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_my_collect_tv_title);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.item_my_collect_tv_describe);
            viewHolder.im_pic= (ImageView) convertView.findViewById(R.id.item_my_collect_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(ac.getTitle());
        viewHolder.describe.setText(ac.getDescribe());
        BitmapUtils bitmapUtils=new BitmapUtils(ctx);
        bitmapUtils.display(viewHolder.im_pic,ac.getPicture_url());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent=new Intent(ctx, S_ArticleActivity.class);
                intent.putExtra("article_id", ac.getId());
                intent.putExtra("content_html",ac.getContent());
                intent.putExtra("title",ac.getTitle());
                intent.putExtra("tag",ac.getTag());
                intent.putExtra("views",ac.getViews());
                intent.putExtra("like",ac.getLike());
                intent.putExtra("comment",ac.getComment());
                intent.putExtra("created_at",ac.getCreated_at());
        ctx.startActivity(intent);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
         TextView title, describe;
        ImageView im_pic;


    }
}

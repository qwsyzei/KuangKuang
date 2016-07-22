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

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.MyWord;
import klsd.kuangkuang.utils.Consts;

/**
 * 我的说说adapter
 * Created by qiwei on 2016/7/14.
 */
public class M_MywordAdapter extends ArrayAdapter<MyWord> {

    private Context ctx;

    public M_MywordAdapter(Context context, List<MyWord> objects) {
        super(context, R.layout.item_myword, objects);
        this.ctx = context;
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

                    BitmapUtils bitmapUtils = new BitmapUtils(ctx);
        bitmapUtils.display(viewHolder.pic_url1, Consts.host + "/" +ac.getUrl1());

//        viewHolder.pic_url1.setImageBitmap(ac.getUrl1());
        viewHolder.day.setText(ac.getDay());
        viewHolder.month.setText(ac.getMonth());
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
        TextView content_son, day, month;
        ImageView pic_url1;

    }
}

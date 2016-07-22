package klsd.kuangkuang.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.Circles;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.views.CircleImageView;
import klsd.kuangkuang.views.SelfGridView;

/**
 * 圈子的adapter
 * Created by qiwei on 2016/7/8.
 */
public class C_CircleAdapter extends ArrayAdapter<Circles> {

    private Context ctx;
    private int number;//9宫格图片的个数
    private List<CircleGridViewEntity> headerEntitiesList;
    private C_CircleGridAdapter cGridAdapter;

    public C_CircleAdapter(Context context, List<Circles> objects) {
        super(context, R.layout.item_circle, objects);
        this.ctx = context;
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (circles.getUrl1().equals("null")){
            number=0;
        }else if (circles.getUrl2().equals("null")){
            number=1;
        }else if (circles.getUrl3().equals("null")){
            number=2;
        }else if (circles.getUrl4().equals("null")){
            number=3;
        }else if (circles.getUrl5().equals("null")){
            number=4;
        }else if (circles.getUrl6().equals("null")){
            number=5;
        }else if (circles.getUrl7().equals("null")){
            number=6;
        }else if (circles.getUrl8().equals("null")){
            number=7;
        }else if (circles.getUrl9().equals("null")){
            number=8;
        }else {
            number=9;
        }
        headerEntitiesList = new ArrayList<>();

        String[] url = new String[]{circles.getUrl1(), circles.getUrl2(), circles.getUrl3(), circles.getUrl4(), circles.getUrl5(), circles.getUrl6(), circles.getUrl7(), circles.getUrl8(), circles.getUrl9()};
        for (int i = 0; i < number; i++) {
            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, Consts.host + "/" + url[i]);
            headerEntitiesList.add(cirEntity);
        }
        Log.d("地址是", "getView() returned: " + circles.getUrl1());
        viewHolder.author.setText(circles.getNickname());
        viewHolder.describe.setText(circles.getContent_son());
//       if (circles.getLike().equals("null")){
//           viewHolder.like.setText("0");
//       }else if (circles.getLike().contains("0")){
//           viewHolder.like.setText(circles.getLike().replace(".0", ""));
//       }else{
//           viewHolder.like.setText(circles.getLike());
//       }
//       if (circles.getComment().equals("null")){
//           viewHolder.comment.setText("0");
//       }else if (circles.getComment().contains("0")){
//           viewHolder.comment.setText(circles.getComment().replace(".0", ""));
//       }else{
//           viewHolder.comment.setText(circles.getComment());
//       }

        BitmapUtils bitmapUtils = new BitmapUtils(getContext());
        bitmapUtils.display(viewHolder.im_head_pic, Consts.host + "/" + circles.getPicture_son());
       String common_time = MyDate.timeLogic(circles.getCreated_at().substring(0, 19).replace("T", " "));
        viewHolder.time.setText(common_time);
        cGridAdapter = new C_CircleGridAdapter(ctx, headerEntitiesList);
        viewHolder.selfGridView.setAdapter(cGridAdapter);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        Intent intent=new Intent(ctx, S_ArticleActivity.class);
//        intent.putExtra("content_html",circles.getContent());
//        ctx.startActivity(intent);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView describe, like, comment;
        public CircleImageView im_head_pic;
        public SelfGridView selfGridView;
        TextView author, time;
    }
}
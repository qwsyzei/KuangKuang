package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
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
import klsd.kuangkuang.models.Circle;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.views.SelfGridView;

/**
 * 圈子的adapter
 * Created by qiwei on 2016/7/8.
 */
public class C_CircleAdapter extends ArrayAdapter<Circle> {

    private Context ctx;
    private ArrayList<String> list;//9宫格图片url
    private List<CircleGridViewEntity> headerEntitiesList;
    private C_CircleGridAdapter cGridAdapter;

    public C_CircleAdapter(Context context, List<Circle> objects) {
        super(context, R.layout.item_circle, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Circle circle = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_circle, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_circle_title);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.item_circle_describe);
//            viewHolder.author = (TextView) convertView.findViewById(R.id.item_circle_author_name);
            viewHolder.views = (TextView) convertView.findViewById(R.id.item_circle_views);
            viewHolder.like = (TextView) convertView.findViewById(R.id.item_circle_like);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_circle_comment);
            viewHolder.im_head_pic = (ImageView) convertView.findViewById(R.id.item_circle_head_pic);
viewHolder.selfGridView= (SelfGridView) convertView.findViewById(R.id.gridview_circle);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        list = new ArrayList<>();
        headerEntitiesList=new ArrayList<>();
        list.add(0,"http://img13.mpimg.cn/upload/2014/07/13/189/pinterest_202551086306036.jpg");
        list.add("http://img15.3lian.com/2015/f2/5/105.jpg");
        list.add("http://img4.duitang.com/uploads/item/201511/19/20151119173330_vhFsZ.jpeg");
        list.add("http://sc.jb51.net/uploads/allimg/150219/10-1502191RA80-L.jpg");
        list.add("http://cdn.duitang.com/uploads/item/201411/14/20141114135308_ZXaU3.thumb.224_0.jpeg");
        list.add("http://img1.3lian.com/2015/w7/68/87.jpg");
        list.add("http://img5q.duitang.com/uploads/blog/201407/11/20140711120943_QBsF2.thumb.224_0.jpeg");
        //TODO 进行网络数据的加载

        for (int i=0;i<list.size();i++){

            CircleGridViewEntity cirEntity=new CircleGridViewEntity(getContext(),list.get(i));
            headerEntitiesList.add(cirEntity);
        }
        viewHolder.title.setText(circle.getTitle());
        viewHolder.describe.setText(circle.getDescribe());
//        viewHolder.author.setText(circle.getAuthor());
        viewHolder.views.setText(circle.getViews().replace(".0", ""));
        viewHolder.like.setText(circle.getLike().replace(".0", ""));
        viewHolder.comment.setText(circle.getComment().replace(".0", ""));
        BitmapUtils bitmapUtils=new BitmapUtils(getContext());
        bitmapUtils.display(viewHolder.im_head_pic, circle.getHead_pic_url());
        cGridAdapter = new C_CircleGridAdapter(getContext(),headerEntitiesList);
        viewHolder.selfGridView.setAdapter(cGridAdapter);
//        viewHolder.im_head_pic.setImageBitmap(circle.getHead_pic());

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
        public TextView title, describe, views, like, comment;
        public ImageView im_head_pic;
        public SelfGridView selfGridView;
    }
}
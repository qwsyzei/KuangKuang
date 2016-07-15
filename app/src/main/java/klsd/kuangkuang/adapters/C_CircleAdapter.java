package klsd.kuangkuang.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import klsd.kuangkuang.views.SelfGridView;

/**
 * 圈子的adapter
 * Created by qiwei on 2016/7/8.
 */
public class C_CircleAdapter extends ArrayAdapter<Circles> {

    private Context ctx;
    private ArrayList<String> list, list2, list3, list4, list5;//9宫格图片url
    private List<CircleGridViewEntity> headerEntitiesList, headerEntitiesList2, headerEntitiesList3, headerEntitiesList4, headerEntitiesList5;
    private ArrayList<CircleGridViewEntity> myList;
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
            viewHolder.views = (TextView) convertView.findViewById(R.id.item_circle_views);
            viewHolder.like = (TextView) convertView.findViewById(R.id.item_circle_like);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_circle_comment);
            viewHolder.im_head_pic = (ImageView) convertView.findViewById(R.id.item_circle_head_pic);
            viewHolder.selfGridView = (SelfGridView) convertView.findViewById(R.id.gridview_circle);
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_circle_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        myList = new ArrayList<>();
        list = new ArrayList<>();
        headerEntitiesList = new ArrayList<>();
        list.add(0,"http://img16.huitu.com/res/20150122/668569_20150122193914528200_2.jpg");
        list.add(1,"http://img16.huitu.com/res/20150122/668569_20150122193920998200_2.jpg");
        list.add(2,"http://file.youboy.com/d/153/4/52/7/967307s.jpg");
        list.add(3,"http://a0.att.hudong.com/28/70/01300534726964138511708002961_140.jpg");
        list.add(4,"http://img18.huitu.com/res/20150430/668569_20150430171713868200_2.jpg");
        list.add(5,"http://img17.huitu.com/res/20150206/668569_20150206131213140200_2.jpg");
        //TODO 进行网络数据的加载
        for (int i = 0; i < list.size(); i++) {
            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, list.get(i));
            headerEntitiesList.add(cirEntity);
        }

//
//        headerEntitiesList2 = new ArrayList<>();
//        list2 = new ArrayList<>();
//        list2.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/06/02/a194/920/43920194/logo140x1403212568469_src.jpg");
//        list2.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/07/20/a149/705/44705149/logo140x1407356792827_src.jpg");
//        list2.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/23/a848/115/43115848/logo140x1409774213542_src.jpg");
//        list2.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/25/a403/163/43163403/logo140x1409940428280_src.jpg");
//
//        //TODO 进行网络数据的加载
//        for (int i = 0; i < list2.size(); i++) {
//            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, list2.get(i));
//            headerEntitiesList2.add(cirEntity);
//        }
//
//
//        list3 = new ArrayList<>();
//        headerEntitiesList3 = new ArrayList<>();
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/06/02/a194/920/43920194/logo140x1403212568469_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/07/20/a149/705/44705149/logo140x1407356792827_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/23/a848/115/43115848/logo140x1409774213542_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/25/a403/163/43163403/logo140x1409940428280_src.jpg");
//
//        //TODO 进行网络数据的加载
//        for (int i = 0; i < list3.size(); i++) {
//            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, list3.get(i));
//            headerEntitiesList3.add(cirEntity);
//        }
//
//
//        list3 = new ArrayList<>();
//        headerEntitiesList4 = new ArrayList<>();
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/06/02/a194/920/43920194/logo140x1403212568469_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/07/20/a149/705/44705149/logo140x1407356792827_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/23/a848/115/43115848/logo140x1409774213542_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/25/a403/163/43163403/logo140x1409940428280_src.jpg");
//
//        //TODO 进行网络数据的加载
//        for (int i = 0; i < list3.size(); i++) {
//            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, list3.get(i));
//            headerEntitiesList4.add(cirEntity);
//        }
//
//
//        list3 = new ArrayList<>();
//        headerEntitiesList5 = new ArrayList<>();
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/06/02/a194/920/43920194/logo140x1403212568469_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/07/20/a149/705/44705149/logo140x1407356792827_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/23/a848/115/43115848/logo140x1409774213542_src.jpg");
//        list3.add("http://u5.mm-img.mmarket.com/rs/res2/21/2015/04/25/a403/163/43163403/logo140x1409940428280_src.jpg");
//
//        //TODO 进行网络数据的加载
//        for (int i = 0; i < list3.size(); i++) {
//            CircleGridViewEntity cirEntity = new CircleGridViewEntity(ctx, list3.get(i));
//            headerEntitiesList5.add(cirEntity);
//        }
//        myList=new ArrayList<>();
//        myList.add(headerEntitiesList);
//        for (int i=0;i<list.size();i++){
//
//            CircleGridViewEntity cirEntity=new CircleGridViewEntity(ctx);
//            headerEntitiesList.add(cirEntity);
//        }
        viewHolder.describe.setText(circles.getDescribe());
        viewHolder.author.setText(circles.getAuthor());
//        viewHolder.views.setText(circles.getViews().replace(".0", ""));
//        viewHolder.like.setText(circles.getLike().replace(".0", ""));
//        viewHolder.comment.setText(circles.getComment().replace(".0", ""));
        viewHolder.views.setText(circles.getViews().replace(".0", ""));
        viewHolder.like.setText(circles.getLike().replace(".0", ""));
        viewHolder.comment.setText(circles.getComment().replace(".0", ""));
//        BitmapUtils bitmapUtils=new BitmapUtils(getContext());
//        bitmapUtils.display(viewHolder.im_head_pic, circles.getHead_pic_url());
        viewHolder.im_head_pic.setImageBitmap(circles.getHead());//这个是假的，回头删了
        viewHolder.time.setText(circles.getCreated_at());
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
        public TextView describe, views, like, comment;
        public ImageView im_head_pic;
        public SelfGridView selfGridView;
        TextView author, time;
    }
}
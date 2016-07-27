package klsd.kuangkuang.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.utils.Consts;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 圈子9宫格adapter
 * Created by qiwei on 2016/7/8.
 */
public class C_CircleGridAdapter extends ArrayAdapter<CircleGridViewEntity> {
    private Context ctx ;
    private List<CircleGridViewEntity> list;

    public C_CircleGridAdapter(Context context, List<CircleGridViewEntity> list) {
        super(context,R.layout.item_gridview,list);
        this.ctx = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int ret = 0 ;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView != null) {
            ret= convertView;
        }else{
            ViewHolder holder = null;
            ret = LayoutInflater.from(ctx).inflate(R.layout.item_gridview,parent,false);
            holder = new ViewHolder();
            holder.bgIcon = (ImageView) ret.findViewById(R.id.im_item_girdvie);

            ret.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) ret.getTag();
        Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(),holder.bgIcon);


        return ret;
    }
    class ViewHolder {
        ImageView bgIcon;
    }

}

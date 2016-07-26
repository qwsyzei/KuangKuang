package klsd.kuangkuang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;
import klsd.kuangkuang.R;
import klsd.kuangkuang.models.CircleLike;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.views.CircleImageView;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 我的说说详情页赞头像列表adapter
 * Created by qiwei on 2016/7/26.
 */
public class M_DetailLikeAdapter extends ArrayAdapter<CircleLike> {
    private Context ctx ;
    private List<CircleLike> list;

    public M_DetailLikeAdapter(Context context, List<CircleLike> list) {
        super(context, R.layout.item_gridview,list);
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
            ret = LayoutInflater.from(ctx).inflate(R.layout.item_gridview_like_head,parent,false);
            holder = new ViewHolder();
            holder.bgIcon = (CircleImageView) ret.findViewById(R.id.im_item_girdview_like_head);

            ret.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) ret.getTag();
    Context context = ctx.getApplicationContext();
        initImageLoader(context);
        ImageLoader.getInstance().displayImage(Consts.host + "/" + list.get(position).getUser_head_url(), holder.bgIcon);
        return ret;
    }
    class ViewHolder {
        CircleImageView bgIcon;
    }

}

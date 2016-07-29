package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.views.ExitDialog;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        ImageLoader.getInstance().displayImage(list.get(position).getPicture_url(), holder.bgIcon);

        return ret;
    }

//
//    public static void saveImageToGallery(Context context, Bitmap bmp) {
//        // 首先保存图片
//        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        String fileName = System.currentTimeMillis() + ".jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
//    }
    class ViewHolder {
        ImageView bgIcon;
    }

}

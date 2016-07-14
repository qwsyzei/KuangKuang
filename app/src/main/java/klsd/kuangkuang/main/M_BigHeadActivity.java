package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

import klsd.kuangkuang.R;
import klsd.kuangkuang.photoview.PhotoViewAttacher;

/**
 * 大头像
 */
public class M_BigHeadActivity extends BaseActivity {
    private ImageView im_bighead;
String pic_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__big_head);
        setTitle(getString(R.string.head_pic));
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        pic_url=intent.getStringExtra("pic");
        im_bighead = (ImageView) findViewById(R.id.im_bighead);
        BitmapUtils bitmapUtils = new BitmapUtils(M_BigHeadActivity.this);
        bitmapUtils.display(im_bighead, pic_url);

        PhotoViewAttacher mAttacher = new PhotoViewAttacher(im_bighead);
        mAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}

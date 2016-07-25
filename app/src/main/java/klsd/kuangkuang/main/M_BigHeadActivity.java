package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import klsd.kuangkuang.R;
import klsd.kuangkuang.photoview.PhotoViewAttacher;
import klsd.kuangkuang.testpic.TestPicActivity;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.SelectPicDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 大头像
 */
public class M_BigHeadActivity extends BaseActivity implements View.OnClickListener {
    private ImageView im_bighead;
    String pic_url;
    private TextView tv_right;
    String photoStr;
    private SelectPicDialog selectPicDialog;
    private LinearLayout layout_from_album, layout_take_photo;
    private TextView tv_dialog_cancel;
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESULT = 3;// 结果

    public static final String IMAGE_UNSPECIFIED = "image/*";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__big_head);
        setTitle(getString(R.string.head_pic));
        Context context = getApplicationContext();
        initImageLoader(context);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        pic_url = intent.getStringExtra("pic");
        im_bighead = (ImageView) findViewById(R.id.im_bighead);
        tv_right = (TextView) findViewById(R.id.tv_title_right);
        tv_right.setText(getString(R.string.change_head_pic));
        tv_right.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(pic_url, im_bighead);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(im_bighead);
//        mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                initDialog();
                break;
            case R.id.layout_from_album:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent, PHOTOZOOM);
//                Intent intent = new Intent(M_BigHeadActivity.this,
//                        TestPicActivity.class);
//                startActivity(intent);
                selectPicDialog.dismiss();
                break;
            case R.id.layout_take_photo:
                Intent intent123 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent123.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory().getAbsolutePath()+ "/MedicalApplication/Camera/userImage/", "temp.jpg")));
                System.out.println("=============" + Environment.getExternalStorageDirectory());
                startActivityForResult(intent123, PHOTOHRAPH);
                selectPicDialog.dismiss();
                break;
            case R.id.tv_dialog_cancel:
                selectPicDialog.dismiss();
                break;
        }
    }

    MyHTTP http;

    /**
     * 上传头像
     */
    private void updateHead() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("picture", photoStr);
        Log.d("头像的16进制", "updateData() returned: " + photoStr);
        if (http == null) http = new MyHTTP(M_BigHeadActivity.this);
        http.baseRequest(Consts.memberUpdateHeadApi, JSONHandler.JTYPE_MEMBER_UPDATE_HEAD, HttpRequest.HttpMethod.POST,
                params, getHandler());
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_MEMBER_UPDATE_HEAD)) {
            Log.d("头像上传成功", "updateData() returned: " + "");
        }

    }

    //创建dialog
    private void initDialog() {
        selectPicDialog = new SelectPicDialog(M_BigHeadActivity.this, R.style.dialog123);//创建Dialog并设置样式主题
        Window window = selectPicDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        selectPicDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        selectPicDialog.show();
        dialog_initView();//一定要写在show之后
    }

    //dialog的初始化
    private void dialog_initView() {
        layout_from_album = (LinearLayout) selectPicDialog.findViewById(R.id.layout_from_album);
        layout_take_photo = (LinearLayout) selectPicDialog.findViewById(R.id.layout_take_photo);
        tv_dialog_cancel = (TextView) selectPicDialog.findViewById(R.id.tv_dialog_cancel);
        layout_from_album.setOnClickListener(this);
        layout_take_photo.setOnClickListener(this);
        tv_dialog_cancel.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            // 设置文件保存路径这里放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/MedicalApplication/Camera/userImage/", "temp.jpg");
            System.out.println("------------------------" + picture.getPath());
            startPhotoZoom(Uri.fromFile(picture));
//            startPhotoZoom(photoUri);
        }

        if (data == null)
            return;

        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTORESULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 -
                // 100)压缩文件
                byte[] bt = stream.toByteArray();//为了转成16进制
                photoStr = byte2hex(bt);//
                im_bighead.setImageBitmap(photo);
                updateHead();
            }
//            String dir = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + "/MedicalApplication/Camera/userImage/";

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 二进制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESULT);
    }
}

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.qqtheme.framework.picker.CityPicker;
import klsd.kuangkuang.R;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import klsd.kuangkuang.models.Documents;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.CircleImageView;
import klsd.kuangkuang.views.ContainsEmojiEditText;
import klsd.kuangkuang.views.SelectPicDialog;

import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 个人资料
 */
public class M_PersonalDataActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView tv_birthday, tv_city;
    private Spinner spinner_sex;
    private ContainsEmojiEditText edit_per_nickname, edit_per_signature;
    private CircleImageView im_head;
    private TextView tv_save, tv_change_head;
    String sex;
    String head_url;
    private Documents documents;
    ArrayList<String> imageUrlsList;//9宫格URL列表(final必须加，目前不知道为什么)
    String photoStr;
    private SelectPicDialog selectPicDialog;
    private LinearLayout layout_from_album, layout_take_photo;
    private TextView tv_dialog_cancel;
    public static final int PHOTOHRAPH = 1;// 拍照
    MyHTTP http;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_personal_data);
        setTitle(getString(R.string.personal_data));
        setTitleRight(getString(R.string.save));
        Context context = getApplicationContext();
        initImageLoader(context);
        initView();
    }

    private void initView() {
        tv_save = (TextView) findViewById(R.id.tv_title_right);
        im_head = (CircleImageView) findViewById(R.id.im_head_pic);
        tv_change_head = (TextView) findViewById(R.id.tv_personal_data_change_head);
        edit_per_nickname = (ContainsEmojiEditText) findViewById(R.id.per_nickname);
        edit_per_signature = (ContainsEmojiEditText) findViewById(R.id.per_edit_signature);
        EditTListener(edit_per_nickname);
        EditTListener(edit_per_signature);
        edit_per_nickname.setOnFocusChangeListener(this);//用于判断焦点
        edit_per_signature.setOnFocusChangeListener(this);
        spinner_sex = (Spinner) findViewById(R.id.per_spinner_sex);
        tv_birthday = (TextView) findViewById(R.id.per_birthday);
        tv_city = (TextView) findViewById(R.id.per_city);
        String[] mItems = {"男", "女"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.myspinner, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sex.setAdapter(adapter);
        tv_change_head.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        im_head.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        getData();
        documents = new Documents();

    }

    /**
     * 获取个人资料
     */
    private void getData() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        if (http == null) http = new MyHTTP(M_PersonalDataActivity.this);
        http.baseRequest(Consts.memberDocumentsApi, JSONHandler.JTYPE_MEMBER_DOCUMENTS, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    /**
     * 更新个人资料
     */
    private void updateInfo() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("nickname", edit_per_nickname.getText().toString());
        params.addQueryStringParameter("birth_date", tv_birthday.getText().toString());
        params.addQueryStringParameter("city", tv_city.getText().toString());
        params.addQueryStringParameter("sex", spinner_sex.getSelectedItemPosition() == 0 ? "male" : "female");
        params.addQueryStringParameter("note", edit_per_signature.getText().toString());

        if (http == null) http = new MyHTTP(M_PersonalDataActivity.this);
        http.baseRequest(Consts.memberUpdateDocumentsApi, JSONHandler.JTYPE_MEMBER_UPDATE_DOCUMENTS, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_MEMBER_DOCUMENTS)) {
            documents = (Documents) handlerBundler.getSerializable("documents");
            if (documents.getBirthday().equals("null")) {
                tv_birthday.setText(getString(R.string.not_choose));
            } else {
                tv_birthday.setText(documents.getBirthday());
            }

            if (documents.getCity().equals("null")) {
                tv_city.setText(getString(R.string.not_set));
            } else {
                tv_city.setText(documents.getCity());
            }
            if (documents.getName().equals("null")) {
                edit_per_nickname.setText("k" + getMember().getPhone_number());
            } else {
                edit_per_nickname.setText(documents.getName());
            }
            if (documents.getSignature().equals("null")) {
                edit_per_signature.setText("");
            } else {
                edit_per_signature.setText(documents.getSignature());
            }
            sex = documents.getSex();
            if (sex.equals("male")) {
                spinner_sex.setSelection(0);
            } else {
                spinner_sex.setSelection(1);
            }
            head_url = Consts.host + "/" + documents.getPicture();

            imageUrlsList = new ArrayList<>();
            imageUrlsList.add(head_url);

            if (head_url.equals("null")) {
                im_head.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang03));
            } else {
                ImageLoader.getInstance().displayImage(head_url, im_head);
            }
        } else if (jtype.equals(JSONHandler.JTYPE_MEMBER_UPDATE_DOCUMENTS)) {
            ToastUtil.show(M_PersonalDataActivity.this, getString(R.string.save_success));
            Log.d("个人资料上传成功", "updateData() returned: " + "");
        } else if (jtype.equals(JSONHandler.JTYPE_MEMBER_UPDATE_HEAD)) {
            ToastUtil.show(M_PersonalDataActivity.this, getString(R.string.head_pic_upload_success));
            Log.d("头像上传成功", "updateData() returned: " + "");
                   //清理头像的缓存
            imageLoader.getInstance().clearDiskCache();
            imageLoader.getInstance().clearMemoryCache();
        }

    }

    /**
     * 时间选择器
     */
    public void onDatePicker() {
        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.YEAR_MONTH_DAY);
        picker.setRange(1950, 2020);
        picker.setSelectedDate(1980, 01, 01);//初始时间
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Date>() {
            @Override
            public void onSubmit(Date result) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                int i = result.compareTo(new Date());
                if (i > 0) {
                    ToastUtil.show(M_PersonalDataActivity.this, getString(R.string.date_wrong));
                } else {
                    tv_birthday.setText(format.format(result));
                }
            }
        });
        picker.showAtBottom();
    }

    /**
     * 初始化省市县的Picker
     */
    private void initCityPicker() {
        CityPicker picker = new CityPicker(M_PersonalDataActivity.this);
        picker.setSelectedCity("北京", "北京", "东城区");
        picker.setOnCityPickListener(new CityPicker.OnCityPickListener() {
            @Override
            public void onCityPicked(String province, String city, String county) {
                String address = province + "-" + city + "-" + county;
                tv_city.setText(address);
            }
        });
        picker.showAtBottom();

    }

    /**
     * 打开图片查看器
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(M_PersonalDataActivity.this, ImagePager123Activity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_head_pic:
                imageBrower(0, imageUrlsList);
                break;
            case R.id.per_birthday:
                onDatePicker();
                break;
            case R.id.per_city:
                initCityPicker();
                break;
            case R.id.tv_title_right:
                updateInfo();
                break;
            case R.id.tv_personal_data_change_head:
                initDialog();
                break;
            case R.id.layout_from_album:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent, Crop.REQUEST_PICK);
                selectPicDialog.dismiss();
                break;
            case R.id.layout_take_photo:
                Intent intent123 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent123.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory().getAbsolutePath(), "temp.jpg")));
                startActivityForResult(intent123, PHOTOHRAPH);
                selectPicDialog.dismiss();
                break;
            case R.id.tv_dialog_cancel:
                selectPicDialog.dismiss();
                break;
        }
        edit_per_nickname.clearFocus();
        edit_per_signature.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_per_nickname.getWindowToken(), 0); //强制隐藏键盘
        imm.hideSoftInputFromWindow(edit_per_signature.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 上传头像
     */
    private void updateHead() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("picture", photoStr);
        if (http == null) http = new MyHTTP(M_PersonalDataActivity.this);
        http.baseRequest(Consts.memberUpdateHeadApi, JSONHandler.JTYPE_MEMBER_UPDATE_HEAD, HttpRequest.HttpMethod.POST,
                params, getHandler());
    }

    //创建dialog
    private void initDialog() {
        selectPicDialog = new SelectPicDialog(M_PersonalDataActivity.this, R.style.dialog123);//创建Dialog并设置样式主题
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {//相册
            beginCrop(data.getData());
        } else if (requestCode == PHOTOHRAPH && resultCode == RESULT_OK) {//拍照
            File picture = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"temp.jpg");
            beginCrop(Uri.fromFile(picture));
        }else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select) {
            im_head.setImageDrawable(null);
            Crop.pickImage(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
           Bitmap photo= getBitmapFromUri(Crop.getOutput(result));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 70, stream);// (0 -
                // 100)压缩文件
                byte[] bt = stream.toByteArray();//为了转成16进制
                photoStr = byte2hex(bt);//
                im_head.setImageBitmap(photo);
                updateHead();
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * URI转bitmap
     * @param uri
     * @return
     */
    private Bitmap getBitmapFromUri(Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 二进制转字符串
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

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            edit_per_nickname.setCursorVisible(true);
            edit_per_signature.setCursorVisible(true);
        } else {
            edit_per_nickname.setCursorVisible(false);
            edit_per_signature.setCursorVisible(false);
        }
    }


}

package klsd.kuangkuang.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.testpic.Bimp;
import klsd.kuangkuang.testpic.FileUtils;
import klsd.kuangkuang.testpic.ImageItem;
import klsd.kuangkuang.testpic.PhotoActivity;
import klsd.kuangkuang.testpic.TestPicActivity;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.UploadDialog;

/**
 * 发表说说
 */
public class C_ReleaseWordActivity extends BaseActivity implements View.OnClickListener {

    private GridView noScrollgridview;
    private GridAdapter adapter;
    private TextView tv_release;
    private RelativeLayout layout;
    private EditText edit;
    String photostr[] = new String[]{"", "", "", "", "", "", "", "", ""};
    String id, url;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_release_word);
        setTitle(getString(R.string.release_word));
        initView();
    }

    private void initView() {
        dialog = new UploadDialog(this, R.style.UploadDialog, R.string.upload_dialog_textView);
        dialog.setCanceledOnTouchOutside(false);
        edit = (EditText) findViewById(R.id.release_edit);
        layout = (RelativeLayout) findViewById(R.id.layout_release_word_open_pop);
        layout.setOnClickListener(this);
        tv_release = (TextView) findViewById(R.id.tv_title_right);
        tv_release.setText(getString(R.string.send));
        tv_release.setOnClickListener(this);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit.getWindowToken(), 0); //强制隐藏键盘//清空数据并让它失去焦点
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(C_ReleaseWordActivity.this, noScrollgridview);
                } else {
                    Intent intent = new Intent(C_ReleaseWordActivity.this,
                            PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_release_word_open_pop:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit.getWindowToken(), 0); //强制隐藏键盘//清空数据并让它失去焦点
                new PopupWindows(C_ReleaseWordActivity.this, noScrollgridview);
                break;
            case R.id.tv_title_right:
                if (edit.getText().toString().equals("")) {
                    ToastUtil.show(C_ReleaseWordActivity.this, "您还未输入文字");
                } else {
                    send_pro();
                }
                break;
        }
    }

    /**
     * 上传之前的准备
     */
    private void send_pro() {
        if (dialog != null) {
            dialog.show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<String> list = new ArrayList<String>();
                for (int i = 0; i < Bimp.drr.size(); i++) {
                    String Str = Bimp.drr.get(i).substring(
                            Bimp.drr.get(i).lastIndexOf("/") + 1,
                            Bimp.drr.get(i).lastIndexOf("."));
                    list.add(FileUtils.SDPATH + Str + ".JPEG");
                    Log.d("地址是", "onClick() returned: " + FileUtils.SDPATH + Str + ".JPEG");
                }
                // 高清的压缩图片全部就在  list 路径里面了

                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面

                for (int i = 0; i < Bimp.bmp.size(); i++) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();//这句话必须写在循环里面
                    Bimp.bmp.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 -
                    // 100)压缩文件
                    byte[] bt = stream.toByteArray();//为了转成16进制
                    photostr[i] = byte2hex(bt);//
                }
                release_word();
            }
        }).start();

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

    MyHTTP http;

    /**
     * 发表说说
     */
    private void release_word() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("member_id", DataCenter.getMember_id());
                params.addQueryStringParameter("content", edit.getText().toString());
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.createMicropostsApi, JSONHandler.JTYPE_CREATE_WORDS, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();
    }

    /**
     * Picture1
     */
    private void picture1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[0]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture1Api, JSONHandler.JTYPE_PICTURE1, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture2
     */
    private void picture2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[1]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture2Api, JSONHandler.JTYPE_PICTURE2, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture3
     */
    private void picture3() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[2]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture3Api, JSONHandler.JTYPE_PICTURE3, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture4
     */
    private void picture4() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[3]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture4Api, JSONHandler.JTYPE_PICTURE4, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture5
     */
    private void picture5() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[4]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture5Api, JSONHandler.JTYPE_PICTURE5, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture6
     */
    private void picture6() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[5]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture6Api, JSONHandler.JTYPE_PICTURE6, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture7
     */
    private void picture7() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[6]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture7Api, JSONHandler.JTYPE_PICTURE7, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture8
     */
    private void picture8() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[7]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture8Api, JSONHandler.JTYPE_PICTURE8, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * Picture9
     */
    private void picture9() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("id", id);
                params.addQueryStringParameter("url", url);
                params.addQueryStringParameter("picture", photostr[8]);
                if (http == null) http = new MyHTTP(C_ReleaseWordActivity.this);
                http.baseRequest(Consts.picture9Api, JSONHandler.JTYPE_PICTURE9, HttpRequest.HttpMethod.POST,
                        params, getHandler());
            }
        }).start();

    }

    /**
     * 发表成功后跳转到MAIN的“我”界面
     */
    private void Intentstyle() {
        Intent intent = new Intent(C_ReleaseWordActivity.this, MainActivity.class);
        intent.putExtra("release", "123");
        startActivity(intent);
        finish();
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_CREATE_WORDS)) {
            id = (String) handlerBundler.getString("id");
            url = handlerBundler.getString("url");
            Log.d("文字发表成功了哟", "updateData() returned: " + "");
            if (Bimp.drr.size() > 0) {
                picture1();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }

        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE1)) {
            Log.d("图片111成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 1) {
                picture2();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE2)) {
            Log.d("图片222成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 2) {
                picture3();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE3)) {
            Log.d("图片333成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 3) {
                picture4();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE4)) {

            Log.d("图片444成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 4) {
                picture5();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE5)) {

            Log.d("图片555成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 5) {
                picture6();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE6)) {
            Log.d("图片666成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 6) {
                picture7();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE7)) {
            Log.d("图片777成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 7) {
                picture8();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE8)) {
            Log.d("图片888成功", "updateData() returned: " + "");
            if (Bimp.drr.size() > 8) {
                picture9();
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
                Intentstyle();
                // 完成上传服务器后 .........
                FileUtils.deleteDir();
            }
        } else if (jtype.equals(JSONHandler.JTYPE_PICTURE9)) {
            Log.d("图片888成功", "updateData() returned: " + "");
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            ToastUtil.show(C_ReleaseWordActivity.this, "发表成功！");
            Intentstyle();
            // 完成上传服务器后 .........
            FileUtils.deleteDir();
        }

    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
            loading();
        }

        public int getCount() {
            return (Bimp.bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.bmp.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.icon_addpic_unfocused));
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.bmp.get(position));
            }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.drr.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            try {
                                String path = Bimp.drr.get(Bimp.max);
                                Bitmap bm = Bimp.revitionImageSize(path);
                                Bimp.bmp.add(bm);
                                String newStr = path.substring(
                                        path.lastIndexOf("/") + 1,
                                        path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm, "" + newStr);
                                Bimp.max += 1;
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(C_ReleaseWordActivity.this,
                            TestPicActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        StringBuffer sDir = new StringBuffer();
        if (hasSDcard()) {
            sDir.append(Environment.getExternalStorageDirectory() + "/MyPicture/");
        } else {
            String dataPath = Environment.getRootDirectory().getPath();
            sDir.append(dataPath + "/MyPicture/");
        }

        File fileDir = new File(sDir.toString());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, String.valueOf(System.currentTimeMillis()) + ".jpg");

        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    public static boolean hasSDcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    ImageItem item = new ImageItem();
                    item.imagePath = path;
                    Bimp.drr.add(path);
                }
                break;
        }
    }

}


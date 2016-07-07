package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qqtheme.framework.picker.CityPicker;
import klsd.kuangkuang.R;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.WheelPicker;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.SelectPicDialog;

/**
 * 个人资料
 */
public class M_PersonalDataActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView tv_birthday, tv_city;
    private Spinner spinner_sex,spinner_profession;
    private EditText edit_per_nickname, edit_per_signature;
    private RelativeLayout relativeLayout;
private SelectPicDialog selectPicDialog;
    private LinearLayout layout_from_album,layout_take_photo;
    private TextView tv_dialog_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_personal_data);
        setTitle(getString(R.string.personal_data));
        setTitleRight(getString(R.string.save));
        initView();
    }

    private void initView() {
        relativeLayout= (RelativeLayout) findViewById(R.id.per_change_head_pic);
        edit_per_nickname = (EditText) findViewById(R.id.per_nickname);
        edit_per_signature = (EditText) findViewById(R.id.per_edit_signature);
        edit_per_nickname.setOnFocusChangeListener(this);//用于判断焦点
        edit_per_signature.setOnFocusChangeListener(this);
        spinner_sex = (Spinner) findViewById(R.id.per_spinner_sex);
        tv_birthday = (TextView) findViewById(R.id.per_birthday);
        tv_city = (TextView) findViewById(R.id.per_city);
        spinner_profession = (Spinner) findViewById(R.id.per_profession);
        String[] mItems = {"男", "女"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.myspinner, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sex.setAdapter(adapter);
        String [] profession={"销售","IT","房产","金融","传媒","财务"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.myspinner, profession);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_profession.setAdapter(adapter1);
        relativeLayout.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_city.setOnClickListener(this);

    }

    /**
     * 时间选择器
     */
    public void onDatePicker() {

        DateTimePicker picker = new DateTimePicker(this);
        picker.setMode(DateTimePicker.Mode.YEAR_MONTH_DAY);
        picker.setRange(1940, 2020);
        picker.setSelectedDate(1993, 01, 01);//初始时间
        picker.setOnWheelListener(new WheelPicker.OnWheelListener<Date>() {
            @Override
            public void onSubmit(Date result) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                int i = result.compareTo(new Date());
                if (i > 0) {
                    ToastUtil.show(M_PersonalDataActivity.this, "您选择的日期大于当前日期，请重新选择");
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
        Log.d("进入到城市的方法了", "initCityPicker() returned: " + "");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.per_change_head_pic:
                initDialog();
                break;
            case R.id.per_birthday:
                onDatePicker();
                break;
            case R.id.per_city:
                initCityPicker();
                break;
            case R.id.per_profession:
                ToastUtil.show(M_PersonalDataActivity.this, "这些职业写成spinner选择，还是让用户自己填");
                break;
            case R.id.layout_from_album:
//                Intent intent2 = new Intent(Intent.ACTION_VIEW);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
//
//                intent.setDataAndType(uri, "image/*");
//                startActivity(intent2);

                selectPicDialog.dismiss();
                break;
            case R.id.layout_take_photo:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, 100);
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

}

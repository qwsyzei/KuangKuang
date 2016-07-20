package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

/**
 * 个人资料
 */
public class M_PersonalDataActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView tv_birthday, tv_city;
    private Spinner spinner_sex;
    private EditText edit_per_nickname, edit_per_signature;
    private CircleImageView im_head;
    private TextView tv_save;
    String sex;
    String head_url;
    private Documents documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_personal_data);
        setTitle(getString(R.string.personal_data));
        setTitleRight(getString(R.string.save));
        initView();
    }

    private void initView() {
        tv_save = (TextView) findViewById(R.id.tv_title_right);
        im_head = (CircleImageView) findViewById(R.id.im_head_pic);
        edit_per_nickname = (EditText) findViewById(R.id.per_nickname);
        edit_per_signature = (EditText) findViewById(R.id.per_edit_signature);
        edit_per_nickname.setOnFocusChangeListener(this);//用于判断焦点
        edit_per_signature.setOnFocusChangeListener(this);
        spinner_sex = (Spinner) findViewById(R.id.per_spinner_sex);
        tv_birthday = (TextView) findViewById(R.id.per_birthday);
        tv_city = (TextView) findViewById(R.id.per_city);
        String[] mItems = {"男", "女"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.myspinner, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sex.setAdapter(adapter);
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
    MyHTTP http;

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
        Log.d("性别", "updateInfo() returned: " + spinner_sex.getSelectedItemId());
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("nickname", edit_per_nickname.getText().toString());
        params.addQueryStringParameter("birth_date", tv_birthday.getText().toString());
        params.addQueryStringParameter("city", tv_city.getText().toString());
        params.addQueryStringParameter("sex", spinner_sex.getSelectedItemPosition()==0?"male":"female");
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
                tv_birthday.setText("未选择");
            } else {
                tv_birthday.setText(documents.getBirthday());
            }

            if (documents.getCity().equals("null")) {
                tv_city.setText("未设置");
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
            head_url =Consts.host + "/" + documents.getPicture();
            Log.d("头像的地址是", "updateData() returned: " + head_url);
            if (head_url.equals("null")) {
                im_head.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.touxiang03));
            } else {
                BitmapUtils bitmapUtils = new BitmapUtils(M_PersonalDataActivity.this);
                bitmapUtils.display(im_head,  head_url);
            }

            ToastUtil.show(M_PersonalDataActivity.this, "已获取到个人资料");
        }  else if (jtype.equals(JSONHandler.JTYPE_MEMBER_UPDATE_DOCUMENTS)) {
            ToastUtil.show(M_PersonalDataActivity.this,"保存成功");
            Log.d("个人资料上传成功", "updateData() returned: " + "");
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
            case R.id.im_head_pic:
                Intent intent_pic = new Intent(M_PersonalDataActivity.this, M_BigHeadActivity.class);
                intent_pic.putExtra("pic", head_url);
                startActivity(intent_pic);
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






}

package yksg.kuangkuang.main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;

import yksg.kuangkuang.R;
import yksg.kuangkuang.utils.Consts;
import yksg.kuangkuang.utils.JSONHandler;
import yksg.kuangkuang.utils.KelaParams;
import yksg.kuangkuang.utils.MyHTTP;
import yksg.kuangkuang.utils.ToastUtil;
import yksg.kuangkuang.utils.UIutils;
import yksg.kuangkuang.views.ExitDialog;
import yksg.kuangkuang.views.MyRadioGroup;

/**
 * 计算器
 */
public class T_CalculatorActivity extends BaseActivity implements View.OnClickListener {
    private MyRadioGroup rg_color, rg_clarity;
    private RadioGroup rg_shape;
    private RadioButton rb_shape, rb_color, rb_clarity;
    private String shape = "ROUNDS", color = "D", clarity = "IF";
    private ImageView im_calculator, im_formula;
    private EditText editText;
    private TextView tv_cny, tv_usd, tv_rate;
    private ExitDialog exitDialog;
private LinearLayout layout_result;//隐藏的
    private TextView tv_rate_text;//隐藏的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_calculator);
        setTitle(getString(R.string.calculator));
        initView();
    }

    private void initView() {
        rg_shape = (RadioGroup) findViewById(R.id.tool_radiogroup_shape);
        rg_color = (MyRadioGroup) findViewById(R.id.tool_radiogroup_color);
        rg_clarity = (MyRadioGroup) findViewById(R.id.tool_radiogroup_clarity);
        rb_shape = (RadioButton) findViewById(R.id.rb_calculator_round);
        rb_color = (RadioButton) findViewById(R.id.rb_calculator_d);
        rb_clarity = (RadioButton) findViewById(R.id.rb_calculator_if);
        im_calculator = (ImageView) findViewById(R.id.calculator_result_imageview);
        im_formula = (ImageView) findViewById(R.id.calculator_result_image_formula);
        editText = (EditText) findViewById(R.id.calculator_weight_edit);
        tv_cny = (TextView) findViewById(R.id.calculator_result_money_yuan);
        tv_usd = (TextView) findViewById(R.id.calculator_result_money_dollar);
        tv_rate = (TextView) findViewById(R.id.calculator_result_rate);

        layout_result= (LinearLayout) findViewById(R.id.layout_c_calculator_result_result);
        tv_rate_text= (TextView) findViewById(R.id.calculator_result_rate_text);
        im_calculator.setOnClickListener(this);
        im_formula.setOnClickListener(this);
        rb_shape.setChecked(true);
        rb_color.setChecked(true);
        rb_clarity.setChecked(true);
        rg_shape.setOnCheckedChangeListener(rgshape);
        rg_color.setOnCheckedChangeListener(rgcolor);
        rg_clarity.setOnCheckedChangeListener(rgclarity);
    }

    private RadioGroup.OnCheckedChangeListener rgshape = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i) {
                case R.id.rb_calculator_round:
                    shape = "ROUNDS";
                    break;
                case R.id.rb_calculator_spec:
                    shape = "PEARS";
                    break;
            }
        }
    };
    private MyRadioGroup.OnCheckedChangeListener rgcolor = new MyRadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(MyRadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_calculator_d:
                    color = "D";
                    break;
                case R.id.rb_calculator_e:
                    color = "E";
                    break;
                case R.id.rb_calculator_f:
                    color = "F";
                    break;
                case R.id.rb_calculator_g:
                    color = "G";
                    break;
                case R.id.rb_calculator_h:
                    color = "H";
                    break;
                case R.id.rb_calculator_i:
                    color = "I";
                    break;
                case R.id.rb_calculator_j:
                    color = "J";
                    break;
                case R.id.rb_calculator_k:
                    color = "K";
                    break;
                case R.id.rb_calculator_l:
                    color = "L";
                    break;
                case R.id.rb_calculator_m:
                    color = "M";
                    break;

            }
        }
    };
    private MyRadioGroup.OnCheckedChangeListener rgclarity = new MyRadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(MyRadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_calculator_if:
                    clarity = "IF";
                    break;
                case R.id.rb_calculator_vvs1:
                    clarity = "VVS1";
                    break;
                case R.id.rb_calculator_vvs2:
                    clarity = "VVS2";
                    break;
                case R.id.rb_calculator_vs1:
                    clarity = "VS1";
                    break;
                case R.id.rb_calculator_vs2:
                    clarity = "VS2";
                    break;
                case R.id.rb_calculator_si1:
                    clarity = "SI1";
                    break;
                case R.id.rb_calculator_si2:
                    clarity = "SI2";
                    break;
                case R.id.rb_calculator_si3:
                    clarity = "SI3";
                    break;
                case R.id.rb_calculator_i1:
                    clarity = "I1";
                    break;
                case R.id.rb_calculator_i2:
                    clarity = "I2";
                    break;
                case R.id.rb_calculator_i3:
                    clarity = "I3";
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calculator_result_imageview:
                if (!editText.getText().toString().equals("")) {
                    double weight = Double.parseDouble(editText.getText().toString());
                    if (weight < 11) {
                        UIutils.showCalculating(T_CalculatorActivity.this);
                        gotoCalculator();
                    } else {
                        ToastUtil.show(T_CalculatorActivity.this, getString(R.string.weight_error));
                    }
                } else {
                    ToastUtil.show(T_CalculatorActivity.this, getString(R.string.input_weight_please));
                }
                break;
            case R.id.calculator_result_image_formula:
                dialog_formula();
                break;
        }
    }

    /**
     * 公式窗口
     */
    private void dialog_formula() {
        exitDialog = new ExitDialog(T_CalculatorActivity.this, R.style.MyDialogStyle, R.layout.dialog_formula);
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.show();

    }

    private void gotoCalculator() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("shape", shape);
        params.addQueryStringParameter("weight", editText.getText().toString());
        params.addQueryStringParameter("color", color);
        params.addQueryStringParameter("clarity", clarity);
        params = KelaParams.generateSignParam("GET", Consts.caratcalculatorApi, params);
        new MyHTTP(this).baseRequest(Consts.caratcalculatorApi, JSONHandler.JTYPE_CALCULATOR, HttpRequest.HttpMethod.GET, params, getHandler());
    }

    @Override
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_CALCULATOR)) {
            UIutils.cancelLoading();
            String cny = handlerBundler.getString("cny");
            String usd = handlerBundler.getString("usd");
            String rate = handlerBundler.getString("rate");
            layout_result.setVisibility(View.VISIBLE);
            tv_rate_text.setVisibility(View.VISIBLE);
            tv_cny.setText(cny);
            tv_usd.setText(usd);
            tv_rate.setText(rate);
        }
    }
}

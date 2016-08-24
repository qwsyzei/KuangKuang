package klsd.kuangkuang.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.models.Certificate;

/**
 * 证书结果
 */
public class T_CertificateResultActivity extends BaseActivity {


private TextView tv_gia_number,tv_date;//证书标号     颁发日期
    private TextView tv_shape;//形状
    private TextView tv_size,tv_weight,tv_color,tv_clarity,tv_cut_grade;
    private TextView tv_depth,tv_table,tv_crown_angle,tv_crown_height,tv_pavilion_angle,tv_pavilion_depth,tv_star_length,tv_lower_half,tv_girdle,tv_culet;
  private TextView tv_polish,tv_symmetry;//抛光  对称
    private TextView tv_fluorescence;//荧光
    private TextView tv_clarity_characteristics;//净度特征
    private TextView tv_inscription;//腰码
    public static Certificate cer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_c_result);
        setTitle(getString(R.string.certificate_result));
        initView();
    }

    private void initView() {
        Intent intent=getIntent();
      String gia_number=intent.getStringExtra("number");
        Log.d("接到了吗", "initView() returned: " + cer.getStar_length());

        tv_gia_number= (TextView) findViewById(R.id.c_result_gia_number);
        tv_date= (TextView) findViewById(R.id.c_result_date);
        tv_shape= (TextView) findViewById(R.id.c_result_shape);
        tv_size= (TextView) findViewById(R.id.c_result_measurement);
        tv_weight= (TextView) findViewById(R.id.c_result_weight);
        tv_color= (TextView) findViewById(R.id.c_result_color);
        tv_clarity= (TextView) findViewById(R.id.c_result_clarity);
        tv_cut_grade= (TextView) findViewById(R.id.c_result_cut_grade);

        tv_depth= (TextView) findViewById(R.id.c_result_depth);
        tv_table= (TextView) findViewById(R.id.c_result_table);
        tv_crown_angle= (TextView) findViewById(R.id.c_result_crown_angle);
        tv_crown_height= (TextView) findViewById(R.id.c_result_crown_height);
        tv_pavilion_angle= (TextView) findViewById(R.id.c_result_pavilion_angle);
        tv_pavilion_depth= (TextView) findViewById(R.id.c_result_pavilion_depth);
        tv_star_length= (TextView) findViewById(R.id.c_result_star_length);
        tv_lower_half= (TextView) findViewById(R.id.c_result_lower_half);
        tv_girdle= (TextView) findViewById(R.id.c_result_girdle);
        tv_culet= (TextView) findViewById(R.id.c_result_culet);

        tv_polish= (TextView) findViewById(R.id.c_result_polish);
        tv_symmetry= (TextView) findViewById(R.id.c_result_symmetry);
        tv_fluorescence= (TextView) findViewById(R.id.c_result_fluorescence);
        tv_clarity_characteristics= (TextView) findViewById(R.id.c_result_clarity_characteristics);
        tv_inscription= (TextView) findViewById(R.id.c_result_inscription);

        //            tv_shape.setText(Shape());
        tv_date.setText(cer.getDate_of_issue());
        tv_gia_number.setText(gia_number);
        tv_size.setText(cer.getMeasurement());
        tv_weight.setText(cer.getCarat_weitht());
        tv_color.setText(cer.getColor_grade());
        tv_clarity.setText(cer.getClarity_grade());
        tv_cut_grade.setText(cer.getCut_grade());

        tv_depth.setText(cer.getDepth());
        tv_table.setText(cer.getTable());
        tv_crown_angle.setText(cer.getCrown_angle());
        tv_crown_height.setText(cer.getCrown_height());
        tv_pavilion_angle.setText(cer.getPavilion_angle());
        tv_pavilion_depth.setText(cer.getPavilion_depth());
        tv_star_length.setText(cer.getStar_length());
        tv_lower_half.setText(cer.getLower_half());
        tv_girdle.setText(cer.getGirdle());
        tv_culet.setText(cer.getCulet());

        tv_polish.setText(cer.getPolish());
        tv_symmetry.setText(cer.getSymmetry());
        tv_fluorescence.setText(cer.getFluorescence());
        tv_clarity_characteristics.setText(cer.getClarity_characteristics());
        tv_inscription.setText(cer.getInscription());
    }

}

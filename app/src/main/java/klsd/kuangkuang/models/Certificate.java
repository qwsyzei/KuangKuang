package klsd.kuangkuang.models;

import android.content.Context;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * 证书
 * Created by qiwei on 2016/8/22.
 */
public class Certificate implements Serializable {
    String shape,cut_grade;
    String date_of_issue, measurement, carat_weitht, color_grade, clarity_grade, depth, table;
    String crown_angle, crown_height, pavilion_angle, pavilion_depth, star_length, lower_half;
    String girdle, culet, polish, symmetry, fluorescence, clarity_characteristics, inscription;
    private Context context;

    public Certificate(Context context) {
        this.context = context;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getCut_grade() {
        return cut_grade;
    }

    public void setCut_grade(String cut_grade) {
        this.cut_grade = cut_grade;
    }

    public String getDate_of_issue() {
        return date_of_issue;
    }

    public void setDate_of_issue(String date_of_issue) {
        this.date_of_issue = date_of_issue;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getCarat_weitht() {
        return carat_weitht;
    }

    public void setCarat_weitht(String carat_weitht) {
        this.carat_weitht = carat_weitht;
    }

    public String getColor_grade() {
        return color_grade;
    }

    public void setColor_grade(String color_grade) {
        this.color_grade = color_grade;
    }

    public String getClarity_grade() {
        return clarity_grade;
    }

    public void setClarity_grade(String clarity_grade) {
        this.clarity_grade = clarity_grade;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getCrown_angle() {
        return crown_angle;
    }

    public void setCrown_angle(String crown_angle) {
        this.crown_angle = crown_angle;
    }

    public String getCrown_height() {
        return crown_height;
    }

    public void setCrown_height(String crown_height) {
        this.crown_height = crown_height;
    }

    public String getPavilion_angle() {
        return pavilion_angle;
    }

    public void setPavilion_angle(String pavilion_angle) {
        this.pavilion_angle = pavilion_angle;
    }

    public String getPavilion_depth() {
        return pavilion_depth;
    }

    public void setPavilion_depth(String pavilion_depth) {
        this.pavilion_depth = pavilion_depth;
    }

    public String getStar_length() {
        return star_length;
    }

    public void setStar_length(String star_length) {
        this.star_length = star_length;
    }

    public String getLower_half() {
        return lower_half;
    }

    public void setLower_half(String lower_half) {
        this.lower_half = lower_half;
    }

    public String getGirdle() {
        return girdle;
    }

    public void setGirdle(String girdle) {
        this.girdle = girdle;
    }

    public String getCulet() {
        return culet;
    }

    public void setCulet(String culet) {
        this.culet = culet;
    }

    public String getPolish() {
        return polish;
    }

    public void setPolish(String polish) {
        this.polish = polish;
    }

    public String getSymmetry() {
        return symmetry;
    }

    public void setSymmetry(String symmetry) {
        this.symmetry = symmetry;
    }

    public String getFluorescence() {
        return fluorescence;
    }

    public void setFluorescence(String fluorescence) {
        this.fluorescence = fluorescence;
    }

    public String getClarity_characteristics() {
        return clarity_characteristics;
    }

    public void setClarity_characteristics(String clarity_characteristics) {
        this.clarity_characteristics = clarity_characteristics;
    }

    public String getInscription() {
        return inscription;
    }

    public void setInscription(String inscription) {
        this.inscription = inscription;
    }

    public void getFromJSONObject(JSONObject object) {
        try {
            setShape(object.getString("shape"));

            setDate_of_issue(object.getString("Date_Of_Issue"));
            setMeasurement(object.getString("Measurement"));
            setCarat_weitht(object.getString("Carat_Weight"));
            setColor_grade(object.getString("Color_Grade"));
            setClarity_grade(object.getString("Clarity_Grade"));
            setDepth(object.getString("Depth"));
            setTable(object.getString("Table"));

            setGirdle(object.getString("Girdle"));
            setCulet(object.getString("Culet"));
            setPolish(object.getString("Polish"));
            setSymmetry(object.getString("Symmetry"));
            setFluorescence(object.getString("Fluorescence"));
            setClarity_characteristics(object.getString("Clarity_Characteristics"));
            setInscription(object.getString("Inscription"));
            setCut_grade(object.getString("Cut_Grade"));
            setCrown_angle(object.getString("Crown_Angle"));
            setCrown_height(object.getString("Crown_Height"));
            setPavilion_angle(object.getString("Pavilion_Angle"));
            setPavilion_depth(object.getString("Pavilion_Depth"));
            setStar_length(object.getString("Star_Length"));
            setLower_half(object.getString("Lower_Half"));
        } catch (Exception e) {
        }
    }
}

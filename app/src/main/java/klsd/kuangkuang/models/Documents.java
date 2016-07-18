package klsd.kuangkuang.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 个人资料的实体类
 * Created by qiwei on 2016/7/13.
 */
public class Documents implements Serializable {

    private String name, birthday, address, sex, signature, aasm_state, zipcode, city;
    private String picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAasm_state() {
        return aasm_state;
    }

    public void setAasm_state(String aasm_state) {
        this.aasm_state = aasm_state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void getFromJSONObject(JSONObject object) {
        try {
            setName(object.getString("nickname"));
            setBirthday(object.getString("birth_date"));
            setAddress(object.getString("address"));
            setSex(object.getString("sex"));
            setSignature(object.getString("signature"));
            setCity(object.getString("city"));
            setPicture(object.getString("picture"));
        } catch (Exception e) {
        }
    }
}
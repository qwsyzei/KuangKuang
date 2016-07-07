package klsd.kuangkuang.models;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 提现记录的实体类
 * Created by qiwei on 2016/2/2.
 */
public class Deposits implements Serializable {

    private String id,currency,amount,fee,fund_uid,fund_extra,txid,created_at,state,sum;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFund_uid() {
        return fund_uid;
    }

    public void setFund_uid(String fund_uid) {
        this.fund_uid = fund_uid;
    }

    public String getFund_extra() {
        return fund_extra;
    }

    public void setFund_extra(String fund_extra) {
        this.fund_extra = fund_extra;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
    public String getFundSourceNum() {
        return "**** **** **** " + getLast4Num();
    }

    public String getLast4Num() {
        if (fund_uid.length() < 4)
            return fund_uid;
        return fund_uid.substring(fund_uid.length() - 4);
    }
    public String getStateName(){
        getState();
        String statename="";
        if (getState().equals("submitting")) {
           statename="提交中";
        }else if(getState().equals("accepted")){
            statename="已完成";
        }else if(getState().equals("cancelled")){
              statename="已取消";
//        }else if(getState().equals("done")){
//            statename="已完成";
        }else  if(getState().equals("suspect")){
            statename="待确认";
        }

  return statename;

    }
    public String getCreatedTimeName() {
//		return getCreated_at().split("\\+")[0].replace("T", " ").replace("Z", "");
        return getCreated_at().replace("T", " ").replace("Z", "").replace("+08:00", "");
    }

    public void getFromJSONObject(JSONObject object) {
        try {
            setId(object.getString("id"));
            setCurrency(object.getString("currency"));
            setAmount(object.getString("amount"));
            setFee(object.getString("fee"));
            setFund_uid(object.getString("fund_uid"));
            setFund_extra(object.getString("fund_extra"));
            setTxid(object.getString("txid"));
            setCreated_at(object.getString("created_at"));
            setState(object.getString("state"));
            if (object.has("sum"))
                setSum(object.getString("sum"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

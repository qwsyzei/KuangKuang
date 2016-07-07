package klsd.kuangkuang.models;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class AccountVersion implements Serializable {
	
	private static final long serialVersionUID = 2034501719441803904L;
	private String id,account_id,modifiable_id;
	private String reason,balance,locked,fee,amount,modifiable_type,created_at,currency,fun,serial_number;
	public static HashMap<String, String> ModifyTypeNames = new HashMap<String, String>();

	static{

		ModifyTypeNames.put("deposit", "充值");
		ModifyTypeNames.put("withdraw", "提现");
		ModifyTypeNames.put("strike_add", "卖出");
		ModifyTypeNames.put("strike_sub", "买入");
	}


	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getModifiable_id() {
		return modifiable_id;
	}

	public void setModifiable_id(String modifiable_id) {
		this.modifiable_id = modifiable_id;
	}

	public String getModifiable_type() {
		return modifiable_type;
	}
	
	public String getModifiable_typeName() {
		return ModifyTypeNames.get(reason);
	}

	public void setModifiable_type(String modifiable_type) {
		this.modifiable_type = modifiable_type;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}


	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFun() {
		return fun;
	}

	public void setFun(String fun) {
		this.fun = fun;
	}

	public void getFromJSONObject(JSONObject object) {
		try {
			setSerial_number(object.getString("serial_number"));
			setId(object.getString("id"));
			setAccount_id(object.getString("account_id"));
			setReason(object.getString("reason"));
			setBalance(object.getString("balance"));
			setLocked(object.getString("locked"));
			setFee(object.getString("fee"));
			setAmount(object.getString("amount"));
			setModifiable_id(object.getString("modifiable_id"));
			setModifiable_type(object.getString("modifiable_type"));
			setCreated_at(object.getString("created_at"));
			setFun(object.getString("fun"));
			setCurrency(object.getString("currency"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

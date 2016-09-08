package yksg.kuangkuang.models;

import org.json.JSONObject;

public class Account {
	String currency,balance,locked;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
	
	public void getFromJSONObject(JSONObject object) {
		try {
			setCurrency(object.getString("currency"));
			setBalance(object.getString("balance"));
			setLocked(object.getString("locked"));
		} catch (Exception e) {
		}
	}
}

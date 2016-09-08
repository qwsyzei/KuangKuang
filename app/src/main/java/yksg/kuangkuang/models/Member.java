package yksg.kuangkuang.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {

	private static final long serialVersionUID = -2415624110939646059L;

	private String sn,name,activated,phone_number;
	private List<Account> accounts;
private ArrayList<AllComment> allCommentList;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String p) {
		this.phone_number = p;
	}

	public String getActivated() {
		return activated;
	}

	public void setActivated(String activated) {
		this.activated = activated;
	}

	public void getFromJSONObject(JSONObject object) {
		try {
			setSn(object.getString("sn"));
			setActivated(object.getString("activated"));
			setName(object.getString("name"));
			setPhone_number(object.getString("phone_number"));
			JSONArray as = object.getJSONArray("accounts");
			accounts = new ArrayList<Account>();
			BigDecimal n = new BigDecimal("0");
			BigDecimal btc = new BigDecimal("0");
			BigDecimal locked = new BigDecimal("0");
			for (int i = 0; i < as.length(); i++) {
				JSONObject o = as.optJSONObject(i);
				Account a = new Account();
				a.getFromJSONObject(o);
				if (a.getCurrency().equals("cny")) {
					n = n.add(new BigDecimal(a.getBalance()));
					locked = locked.add(new BigDecimal(a.getLocked()));
				} else if (a.getCurrency().equals("btc")) {
					btc = btc.add(new BigDecimal(a.getBalance()));
				}
//				locked = locked.add(new BigDecimal(a.getLocked()));          //将它移动到if里面了
				accounts.add(a);
			}

			setAccounts(accounts);
		} catch (Exception e) {
		}
	}

	public ArrayList<AllComment> getAllCommentList() {
		return allCommentList;
	}

	public void setAllCommentList(ArrayList<AllComment> allCommentList) {
		this.allCommentList = allCommentList;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

}

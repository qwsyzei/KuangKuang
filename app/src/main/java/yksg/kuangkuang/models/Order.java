package yksg.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

import yksg.kuangkuang.utils.MyDate;

public class Order  implements Serializable {
	
	private static final long serialVersionUID = 2457956410936820241L;
	private String id,side,ord_type,price,avg_price,state,market,created_at,volume,remaining_volume,executed_volume,trades_count;

	
	private Context ctx;
	
	public Order() {
		
	}
	public Order(Context ctx) {
		this.ctx = ctx;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getOrd_type() {
		return ord_type;
	}

	public void setOrd_type(String ord_type) {
		this.ord_type = ord_type;
	}

	public String getPrice() {
		return price;
	}

	public double getPriceDoubleValue() {
		return Double.parseDouble(price);
	}
	
	public String getHourTime() {
		String timeString = created_at;
		try {
			if (created_at != null && created_at.contains(" ")) {
				timeString = created_at.split(" ")[1];
			} else {
				timeString = created_at.split("T")[1].split("\\+")[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeString;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}

	public String getAvg_price() {
		return avg_price;
	}

	public void setAvg_price(String avg_price) {
		this.avg_price = avg_price;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getOriginVolume() {
		return volume;
	}

	public String getVolumeNormal() {
		return volume;
	}
	
	public double getVolumeDoubleValue() {
		return Double.parseDouble(volume);
	}
	
	public String getRemainingVolumeNormal() {
		return remaining_volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getRemaining_volume() {
		return remaining_volume;
	}

	public void setRemaining_volume(String remaining_volume) {
		this.remaining_volume = remaining_volume;
	}


	public String getExecutedVolumeNormal() {
		return executed_volume;
	}

	public void setExecuted_volume(String executed_volume) {
		this.executed_volume = executed_volume;
	}

	public String getTrades_count() {
		return trades_count;
	}

	public void setTrades_count(String trades_count) {
		this.trades_count = trades_count;
	}
	
	public boolean canChexiao() {
		if (state.equals("wait")) return true;
		return false;
	}



	public String getOrderTime() {
		String timeString = created_at;
		try {
			if (created_at != null && created_at.contains("Z")) {
				String mytime = created_at.replace("T", " ").replace("Z", "");
				timeString = MyDate.formatTimeEight(mytime) ;
			} else {
				String mytime = created_at.replace("T", " ").replace("+08:00", "");
				timeString =mytime;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeString;
	}
	
	public Order getFromJSONObject(JSONObject object) {
		try {
			setId(object.getString("id"));
			setSide(object.getString("side"));
			setOrd_type(object.getString("ord_type"));
			setPrice(object.getString("price"));
			setAvg_price(object.getString("avg_price"));
			setState(object.getString("state"));
			setMarket(object.getString("market"));
			setCreated_at(object.getString("created_at"));
			setVolume(object.getString("volume"));
			setExecuted_volume(object.getString("executed_volume"));
			setRemaining_volume(object.getString("remaining_volume"));
			setTrades_count(object.getString("trades_count"));
//			JSONArray ts = object.getJSONArray("trades");
//			trades = new ArrayList<Trade>();
//			for (int i = 0; i < ts.length(); i++) {
//				JSONObject o = ts.optJSONObject(i);
//				Trade trade = new Trade(ctx);
//				trade.getFromJSONObject(o);
//				trades.add(trade);
//			}
//			setTrades(trades);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public Order getFromJSONObjectItem(JSONObject object) {
		try {
			setId(object.getString("id"));
			setSide(object.getString("side"));
			setOrd_type(object.getString("ord_type"));
			setPrice(object.getString("price"));
			setAvg_price(object.getString("avg_price"));
			setState(object.getString("state"));
			setMarket(object.getString("market"));
			setCreated_at(object.getString("created_at"));
			setVolume(object.getString("volume"));
			setExecuted_volume(object.getString("executed_volume"));
			setRemaining_volume(object.getString("remaining_volume"));
			setTrades_count(object.getString("trades_count"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	public void getFromJSONObjectFromPusher(JSONObject object) {
		try {
        	setId(object.getString("id"));
        	setSide(object.getString("kind"));
        	setPrice(object.getString("price"));
        	setAvg_price(object.getString("avg_price"));
        	setState(object.getString("state"));
        	setRemaining_volume(object.getString("volume"));
        	setCreated_at(MyDate.intToString(object.getLong("at")));
        	String originVolumnString = object.getString("origin_volume");
			setVolume(originVolumnString);
			setExecuted_volume((Double.parseDouble(originVolumnString) -  Double.parseDouble(object.getString("volume"))) + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

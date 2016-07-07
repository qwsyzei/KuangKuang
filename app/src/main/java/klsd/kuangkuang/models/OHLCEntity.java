package klsd.kuangkuang.models;


import klsd.kuangkuang.utils.MyDate;

public class OHLCEntity {

	private double open;// 开盘价
	private double high;// 最高价
	private double low;// 最低价
	private double close;// 收盘价
	private double vol;

	private long intTime;// int时间，如：14500000000
//	private String datetime;// 日期，如：2013-09-18 00:00:00

	public OHLCEntity() {
		super();
	}

	public OHLCEntity(double open, double high, double low, double close, long time, double vol) {
		super();
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.intTime = time;
		this.vol = vol;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public String getDateTime() {
		return MyDate.intToString(intTime).substring(0, 16);
	}
	
	public String getHourMinute() {
		return getDateTime().split(" ")[1].substring(0,5);
	}

	public String getMonthDay() {
		// "0000-00-00"
		return getDateTime().split(" ")[0].substring(5,10);
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public long getIntTime() {
		return intTime;
	}

	public void setIntTime(int intTime) {
		this.intTime = intTime;
	}
}

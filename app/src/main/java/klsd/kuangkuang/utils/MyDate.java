package klsd.kuangkuang.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import klsd.kuangkuang.main.EnterActivity;

@SuppressLint({ "UseValueOf", "SimpleDateFormat" }) 
public class MyDate {
	@SuppressLint("SimpleDateFormat")
	public static String intToString(long date) {
		DateTime dTime = new DateTime();
		dTime.setTimeInMilliSeconds(date * 1000);
		return dTime.toDateTimeString();
	}

	public static String timeStringToUnixTime(String datetime, String formater) {
		Date time = new Date();
		try {
			time = new SimpleDateFormat(formater).parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int t = (int)(time.getTime() / 1000);
		return  t + "";
	}
	
	public static String timeStringToUnixTime(String datetime) {
		return timeStringToUnixTime(datetime, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String railsTimeToUnixTime(String datetime) {
		return timeStringToUnixTime(datetime);
	}
     //获取系统时间
	public static long getSystemTime() {
		long time = System.currentTimeMillis();
		return time;
	}
	//下面是系统时间+与服务器的差
	public static long getTonceInt() {

		long time = EnterActivity.server_time_cha+System.currentTimeMillis();

		return time;
	}
	
	public static String nowString() {
		return intToString(getTonceInt()/1000);
	}
	
	public static String toKTimeString(long time) {
		String timeString = intToString(time);
		timeString = timeString.split(" ")[0].replace("-", "");
		return timeString;
	}
	
	public static String formatCreatedAt(String time) {
		return time.replace("T", " ").replace("Z", "").replace("+08:00", "");
	}
	//下面是把时间加8小时
	public static String formatTimeEight(String time) throws Exception {
		Date d = null;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		d = sd.parse(time);
		long rightTime = (long) (d.getTime() + 8 * 60 * 60 * 1000);
		String newtime = sd.format(rightTime);
		return newtime;
	}
}

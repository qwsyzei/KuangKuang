package klsd.kuangkuang.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	public static String timeStringToUnixTime123(String datetime, String formater) {
		Date time = new Date();
		try {
			time = new SimpleDateFormat(formater).parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long t = time.getTime( );
		return  t + "";
	}
	//通过日期格式字符串得到秒数
	public static String timeStringToUnixTime(String datetime) {
		return timeStringToUnixTime(datetime, "yyyy-MM-dd HH:mm:ss");
	}
	//通过日期格式字符串得到毫秒数
	public static String timeStringToUnixTime123(String datetime) {
		return timeStringToUnixTime123(datetime, "yyyy-MM-dd HH:mm:ss");
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

	/**
	 * 计算发布在几天前
	 * @param dateStr
	 * @return
	 */
	public static String timeLogic(String dateStr) {
		String the_time=timeStringToUnixTime123(dateStr);
		Log.d("它的毫秒是", "timeLogic() returned: " + the_time);
		Log.d("当前时间的毫秒是", "timeLogic() returned: " + getSystemTime()+"");
		// 相差的秒数
		long time = (getSystemTime() - Long.parseLong(the_time)) / 1000;
		Log.d("毫秒差是", "timeLogic() returned: " + time);
		StringBuffer sb = new StringBuffer();
		if (time > 0 && time < 60) { // 1小时内
			return sb.append(time + "秒前").toString();
		} else if (time > 60 && time < 3600) {
			return sb.append(time / 60+"分钟前").toString();
		} else if (time >= 3600 && time < 3600 * 24) {
			return sb.append(time / 3600 +"小时前").toString();
		}else if (time >= 3600 * 24 && time < 3600 * 48) {
			return sb.append("1天前").toString();
		}else if (time >= 3600 * 48 && time < 3600 * 72) {
			return sb.append("2天前").toString();
		}else if (time >= 3600 * 72) {
			return sb.append("3天前").toString();
		}
		return "就是这一天";
	}
}

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

	/**
	 * 获取当前日期(2016-03-06)
	 * @return
	 */
	public static String todayDate() {
		String todaytoday = null;
		Date today=new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			 todaytoday=sd.format(today);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String today_str=(todaytoday).substring(0,10);
		return today_str;
	}
	/**
	 * 时间2016-07-25T16:34:36.000+08:00转成2016-07-25
	 * @param time
	 * @return
	 */
	public static String yearmonthDay(String time) {

		return time.substring(0, 10);
	}
	/**
	 * 时间2016-07-25T16:34:36.000+08:00转成07-25 16：34
	 * @param time
	 * @return
	 */
	public static String monthDay(String time) {

		return time.substring(5, 16).replace("T", " ");
	}
	/**
	 * 时间2016-07-25T16:34:36.000+08:00转成只有月   07
	 * @param time
	 * @return
	 */
	public static String month(String time) {
String the_time=time.substring(5, 7);
		String month;
		if (Integer.parseInt(the_time)<10){
			month=the_time.replace("0","");
		}else{
			month=the_time;
		}
		return month;
	}
	/**
	 * 时间2016-07-25T16:34:36.000+08:00转成  只有日   25
	 * @param time
	 * @return
	 */
	public static String Day(String time) {

		return time.substring(8, 10);
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
		//我知道我写的特别low，但时间太紧，不爱动脑啊，先这样吧，以后有时间再优化
		int year=Integer.parseInt(dateStr.substring(0,4));//数据的年份
		int this_year=Calendar.getInstance().get(Calendar.YEAR);//当前年份
		StringBuffer sb = new StringBuffer();
		if (year==this_year){
			int ct=3600*24;
			String the_time=timeStringToUnixTime123(dateStr);
			// 相差的秒数
			long time = (getSystemTime() - Long.parseLong(the_time)) / 1000;
			Log.d("毫秒差是", "timeLogic() returned: " + time);

			if (time > 0 && time < 60) { // 1小时内
				return sb.append(time + "秒前").toString();
			} else if (time > 60 && time < 3600) {
				return sb.append(time / 60+"分钟前").toString();
			} else if (time >= 3600 && time < 3600 * 24) {
				return sb.append(time / 3600 +"小时前").toString();
			}else if (time >= ct && time < ct*2) {
				return sb.append("1天前").toString();
			}else if (time >= ct*2 && time < ct*3) {
				return sb.append("2天前").toString();
			}else if (time >= ct*3&&time<ct*4) {
				return sb.append("3天前").toString();
			}else if (time >= ct*4 && time < ct*5) {
				return sb.append("4天前").toString();
			}else if (time >= ct*5&&time<ct*6) {
				return sb.append("5天前").toString();
			}
			else if (time >= ct*6 && time < ct*7) {
				return sb.append("6天前").toString();
			}else if (time >= ct*7&&time<ct*8) {
				return sb.append("7天前").toString();
			}
			else if (time >= ct*8 && time < ct*9) {
				return sb.append("8天前").toString();
			}else if (time >= ct*9&&time<ct*10) {
				return sb.append("9天前").toString();
			}
			else if (time >= ct*10 && time < ct*11) {
				return sb.append("10天前").toString();
			}else if (time >= ct*11&&time<ct*12) {
				return sb.append("11天前").toString();
			}
			else if (time >= ct*12 && time < ct*13) {
				return sb.append("12天前").toString();
			}else if (time >= ct*13&&time<ct*14) {
				return sb.append("13天前").toString();
			}
			else if (time >= ct*14 && time < ct*15) {
				return sb.append("14天前").toString();
			}else if (time >= ct*15&&time<ct*16) {
				return sb.append("15天前").toString();
			}else if (time >= ct*16 && time < ct*17) {
				return sb.append("16天前").toString();
			}else if (time >= ct*17&&time<ct*18) {
				return sb.append("17天前").toString();
			}else if (time >= ct*18 && time < ct*19) {
				return sb.append("18天前").toString();
			}else if (time >= ct*19&&time<ct*20) {
				return sb.append("19天前").toString();
			}else if (time >= ct*20 && time < ct*21) {
				return sb.append("20天前").toString();
			}else if (time >= ct*21&&time<ct*22) {
				return sb.append("21天前").toString();
			}
			else if (time >= ct*22 && time < ct*23) {
				return sb.append("22天前").toString();
			}else if (time >= ct*23&&time<ct*24) {
				return sb.append("23天前").toString();
			}else if (time >= ct*24 && time < ct*25) {
				return sb.append("24天前").toString();
			}else if (time >= ct*25&&time<ct*26) {
				return sb.append("25天前").toString();
			}else if (time >= ct*26 && time < ct*27) {
				return sb.append("26天前").toString();
			}else if (time >= ct*27&&time<ct*28) {
				return sb.append("27天前").toString();
			}else if (time >= ct*28 && time < ct*29) {
				return sb.append("28天前").toString();
			}else if (time >= ct*29&&time<ct*30) {
				return sb.append("29天前").toString();
			}else if (time >= ct*30 && time < ct*30*2) {
				return sb.append("1个月前").toString();
			}else if (time >=  ct*30*2 && time < ct*30*3) {
				return sb.append("2个月前").toString();
			}else if (time >= ct*30*3 && time < ct*30*4) {
				return sb.append("3个月前").toString();
			}else if (time >= ct*30*4 && time < ct*30*5) {
				return sb.append("4个月前").toString();
			}else if (time >= ct*30*5 && time < ct*30*6) {
				return sb.append("5个月前").toString();
			}else if (time >= ct*30*6 && time < ct*30*7) {
				return sb.append("半年前").toString();
			}else if (time >= ct*30*7 && time < ct*30*8) {
				return sb.append("7个月前").toString();
			}else if (time >= ct*30*8 && time < ct*30*9) {
				return sb.append("8个月前").toString();
			}else if (time >= ct*30*9 && time < ct*30*10) {
				return sb.append("9个月前").toString();
			}else if (time >= ct*30*10 && time < ct*30*11) {
				return sb.append("10个月前").toString();
			}else if (time >= ct*30*11 && time < ct*30*12) {
				return sb.append("11个月前").toString();
			}

		}else if (year-this_year==-1){
			return sb.append("1年前").toString();
		}else if (year-this_year==-2){
			return sb.append("2年前").toString();
		}

		return "1天前";
	}
}

package yksg.kuangkuang.main.common;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.WindowManager;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 公共方法类
 * @author zcw
 *
 */
public class CommonUtils {
	private static Toast toast=null;

	/**
	 * 获取当前时间
	 * @param format 获取的时间格式，比如yyyy-MM-dd HH:mm:ss
	 * @return	返回当前时间的字符串
	 */
	public static String GetCurrentTime(String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);

		return str;
	}

	/**
	 * 检查设备是否连接网络
	 * @return	已经连接网络返回true，否则返回false
	 * @最后更新日期 2015-04-22
	 */
	public static boolean CheckNetwork(Activity activity){
		ConnectivityManager con=(ConnectivityManager)activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		return wifi | internet;
	}

	/**
	 * 避免Toast重复显示
	 * @param context
	 * @param str	要显示的内容
	 * @最后更新日期 2015-04-22
	 */
	public static void ShowToast(Context context,String str){
		if (toast == null) {
			toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		} else {
			toast.setText(str);
		}
		toast.show();
	}

	/**
	 * 避免Toast重复显示
	 * @param context
	 * @param resId	要显示的Id,如果resId为-1，则不做任何操作，直接返回
	 * @最后更新日期 2015-05-25
	 */
	public static void ShowToast(Context context,int resId){
		if(-1==resId){
			return ;
		}

		if (toast == null) {
			toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		} else {
			toast.setText(resId);
		}
		toast.show();
	}

	/**
	 * 获取屏幕尺寸
	 * @param context
	 * @return	返回屏幕的宽度和高度
	 */
	public static int[] getDisplaySize(Context context){
		int[] displaySize={-1,-1};

		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		displaySize[0]= wm.getDefaultDisplay().getWidth();
		displaySize[1]= wm.getDefaultDisplay().getHeight();

		return displaySize;
	}
}

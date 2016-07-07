package klsd.kuangkuang.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 用于将金额规范化，如###.00
 */
public class KelaNumberUtils {
	
	public static String toThousands(String s){
		return toThousands(s,",###.00");
	}
	
	public static String toThousandsInt(String s){
		return toThousands(s,",###");
	}
	
	public static String toThousands(String s, String format) {
		BigDecimal b = new BigDecimal(s);
		DecimalFormat f = new DecimalFormat(format);
		return f.format(b);
	}
	
	public static String getIncreNum(double close, double open) {
		double n = getIncreNumDouble(close, open);
		String pre = n >= 0 ? "+" : "";
		return pre + formatDouble(n);
	}
	
	public static String getIncrePercent(double close, double open) {
		double n = getIncreNumDouble(close, open);
		String pre = n >= 0 ? "+" : "";
		return pre + formatDouble(n / open * 100) + "%";
	}

	public static double getIncreNumDouble(double close, double open) {
		return close - open;
	}
	
	private static String formatDouble(double d) {
	  DecimalFormat df = new DecimalFormat("#.##");
	  return df.format(d);
	}
}

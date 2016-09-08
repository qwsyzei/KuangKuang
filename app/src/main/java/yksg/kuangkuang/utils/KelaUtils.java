package yksg.kuangkuang.utils;

import android.content.Context;

public class KelaUtils {

	public static String joinStr(Context ctx, int s, String lastValue) {
		return ctx.getString(s) + ": " + lastValue;
	}
}

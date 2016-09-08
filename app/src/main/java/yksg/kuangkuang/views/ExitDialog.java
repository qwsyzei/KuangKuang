package yksg.kuangkuang.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


/**
 * 退出的窗口
 */
public class ExitDialog extends Dialog {

	public static boolean falg = false;

	int layoutRes;
	Context context;

	public ExitDialog(Context context) {
		super(context);
		this.context = context;
	}

	public ExitDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	public ExitDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(layoutRes);


	}

}
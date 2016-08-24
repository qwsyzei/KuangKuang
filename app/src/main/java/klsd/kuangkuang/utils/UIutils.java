package klsd.kuangkuang.utils;

import android.app.ProgressDialog;
import android.content.Context;

import klsd.kuangkuang.R;


/**
 * Created by zhimengsun on 2/18/16.
 */
public class UIutils {

    private static ProgressDialog progress;
    public static void showLoading(Context ctx) {
//        if (progress == null && ctx != null){
        progress = new ProgressDialog(ctx);
        progress.setMessage(ctx.getString(R.string.loading));
        progress.setProgressStyle(ProgressDialog.BUTTON_POSITIVE);
//        }
        progress.show();
        progress.setCanceledOnTouchOutside(false);
    }
    public static void showCalculating(Context ctx) {
//        if (progress == null && ctx != null){
        progress = new ProgressDialog(ctx);
        progress.setMessage(ctx.getString(R.string.calculating));
        progress.setProgressStyle(ProgressDialog.BUTTON_POSITIVE);
//        }
        progress.show();
        progress.setCanceledOnTouchOutside(false);
    }
    public static void showDemanding(Context ctx) {
//        if (progress == null && ctx != null){
        progress = new ProgressDialog(ctx);
        progress.setMessage(ctx.getString(R.string.demanding));
        progress.setProgressStyle(ProgressDialog.BUTTON_POSITIVE);
//        }
        progress.show();
        progress.setCanceledOnTouchOutside(false);
    }
    public static void cancelLoading(){
        if (progress != null) progress.cancel();
    }
}

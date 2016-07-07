package klsd.kuangkuang.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


/**
 * 清理缓存
 * Created by qiwei on 2016/7/5.
 */
public class CleanCacheDialog extends Dialog {

    public static boolean falg = false;

    int layoutRes;
    Context context;

    public CleanCacheDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CleanCacheDialog(Context context, int resLayout) {
        super(context);
        this.context = context;
        this.layoutRes = resLayout;
    }

    public CleanCacheDialog(Context context, int theme, int resLayout) {
        super(context, theme);
        this.context = context;
        this.layoutRes = resLayout;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(layoutRes);


    }
}


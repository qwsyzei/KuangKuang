package yksg.kuangkuang.views;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import yksg.kuangkuang.R;

/**
 * 更多   黑名单   举报
 * Created by qiwei on 2016/8/4.
 */
public class MoreDialog extends AlertDialog {


    public MoreDialog(Context context) {
        super(context);
    }

    public MoreDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);

    }


}

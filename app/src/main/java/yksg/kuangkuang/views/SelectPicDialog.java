package yksg.kuangkuang.views;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import yksg.kuangkuang.R;


/**
 * 自定义窗口，用于选择更换头像的方式.
 */
public class SelectPicDialog extends AlertDialog {


    public SelectPicDialog(Context context) {
        super(context);
    }

    public SelectPicDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_select_head_pic);

    }


}

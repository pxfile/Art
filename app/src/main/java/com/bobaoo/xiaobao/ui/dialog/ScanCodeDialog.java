package com.bobaoo.xiaobao.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.bobaoo.xiaobao.R;

/**
 * Created by Administrator on 2015/7/22.
 */
public class ScanCodeDialog extends Dialog {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public ScanCodeDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_scan_code);
    }

}

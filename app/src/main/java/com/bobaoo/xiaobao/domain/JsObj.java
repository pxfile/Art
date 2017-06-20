package com.bobaoo.xiaobao.domain;

import android.content.Context;

import com.bobaoo.xiaobao.utils.DialogUtils;

/**
 * Created by kakaxicm on 2015/12/16.
 */
abstract public class JsObj {
    private Context mContext;

    public JsObj(Context con) {
        this.mContext = con;
    }

    public abstract void share();

    public abstract void share(String s, String title);
}

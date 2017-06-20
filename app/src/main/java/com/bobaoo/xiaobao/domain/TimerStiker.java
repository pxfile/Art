package com.bobaoo.xiaobao.domain;

import com.bobaoo.xiaobao.listener.TimerCountCallback;

/**
 * Created by kakaxicm on 2015/12/7.
 */
public class TimerStiker {
    private long time;
    private TimerCountCallback callback;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public TimerCountCallback getCallBack() {
        return callback;
    }

    public void setCallBack(TimerCountCallback callBack) {
        this.callback = callBack;
    }
}

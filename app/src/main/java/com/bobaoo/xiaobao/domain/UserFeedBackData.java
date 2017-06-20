package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/6/4.
 */
public class UserFeedBackData {

    /**
     * data : ok
     * error : false
     * message : 395
     */
    private String data;
    private boolean error;
    private int message;

    public void setData(String data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public int getMessage() {
        return message;
    }
}

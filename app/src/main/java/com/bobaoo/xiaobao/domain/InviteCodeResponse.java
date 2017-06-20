package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/7/30.
 */
public class InviteCodeResponse {

    /**
     * data :
     * error : true
     * message : 自己不能邀请自己！
     */
    private String data;
    private boolean error;
    private String message;

    public void setData(String data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}

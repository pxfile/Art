package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/6/8.
 */
public class UserPayData {
    /**
     * error : false
     * data : 支付完成！
     * message :
     */

    private boolean error;
    private String data;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

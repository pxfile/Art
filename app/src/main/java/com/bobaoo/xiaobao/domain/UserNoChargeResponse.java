package com.bobaoo.xiaobao.domain;

/**
 * Created by kakaxicm on 2015/11/17.
 */
public class UserNoChargeResponse {
    /**
     * error : false
     * data : 155
     * message :
     */

    private boolean error;
    private int data;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public int getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}

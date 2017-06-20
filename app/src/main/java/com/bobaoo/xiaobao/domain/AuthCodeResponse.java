package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 15/6/29.
 */
public class AuthCodeResponse {
    /**
     * data : null
     * error : false
     * message : OK
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

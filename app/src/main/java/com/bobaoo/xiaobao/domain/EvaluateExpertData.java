package com.bobaoo.xiaobao.domain;

/**
 * Created by Administrator on 2015/9/29.
 */
public class EvaluateExpertData {

    /**
     * data : ok
     * error : false
     * message : 123
     */
    public String data;
    public boolean error;
    public String message;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/6/1.
 */
public class UserInfoEditCommitResponse {

    /**
     * data : ok
     * error : false
     */
    private String data;
    private boolean error;

    public void setData(String data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }
}

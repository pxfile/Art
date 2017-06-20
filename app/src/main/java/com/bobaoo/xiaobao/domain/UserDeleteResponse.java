package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 15/7/2.
 */
public class UserDeleteResponse {
    /**
     * data : 删除成功！
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

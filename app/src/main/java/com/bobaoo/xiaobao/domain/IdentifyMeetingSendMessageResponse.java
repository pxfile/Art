package com.bobaoo.xiaobao.domain;

/**
 * Created by Fang on 2016/3/24.
 */
public class IdentifyMeetingSendMessageResponse {
    /**
     * error : false
     * data : {"send":"OK","count":2}
     * message :
     */

    private boolean error;
    /**
     * send : OK
     * count : 2
     */

    private DataEntity data;
    private String message;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataEntity {
        private String send;
        private int count;

        public String getSend() {
            return send;
        }

        public void setSend(String send) {
            this.send = send;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

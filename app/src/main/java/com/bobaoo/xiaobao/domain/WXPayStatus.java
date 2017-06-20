package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 16/3/23.
 */
public class WXPayStatus {

    /**
     * error : false
     * data : {"charged":"1","isPay":0,"timeout":0}
     * message :
     */

    private boolean error;
    /**
     * charged : 1
     * isPay : 0
     * timeout : 0
     */

    private DataEntity data;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class DataEntity {
        private String charged;
        private int isPay;
        private int timeout;

        public void setCharged(String charged) {
            this.charged = charged;
        }

        public void setIsPay(int isPay) {
            this.isPay = isPay;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public String getCharged() {
            return charged;
        }

        public int getIsPay() {
            return isPay;
        }

        public int getTimeout() {
            return timeout;
        }
    }
}

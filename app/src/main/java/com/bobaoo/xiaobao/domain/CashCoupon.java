package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 15/9/21.
 */
public class CashCoupon {
    /**
     * error : false
     * data : {"status":"ok","amount":"200"}
     * message :
     */

    private boolean error;
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

    public boolean getError() {
        return error;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static class DataEntity {
        /**
         * status : ok
         * amount : 200
         */

        private String status;
        private String amount;

        public void setStatus(String status) {
            this.status = status;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public String getAmount() {
            return amount;
        }
    }
}

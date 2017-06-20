package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 15/6/29.
 */
public class LoginStepOneResponse {
    /**
     * data : {"uid":883722,"username":"artxun_15968299","token":"6b59RNcNgW2X7C7ZlFX8ctks6z9d8DSt9FRrDsWq2ECfGxCgGBbLIGZMLVAQNpXma1nz"}
     * error : false
     * message : OK
     */
    private DataEntity data;
    private boolean error;
    private String message;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataEntity getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public class DataEntity {
        /**
         * uid : 883722
         * username : artxun_15968299
         * token : 6b59RNcNgW2X7C7ZlFX8ctks6z9d8DSt9FRrDsWq2ECfGxCgGBbLIGZMLVAQNpXma1nz
         */
        private int uid;
        private String username;
        private String token;

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUid() {
            return uid;
        }

        public String getUsername() {
            return username;
        }

        public String getToken() {
            return token;
        }
    }
}

package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/6/3.
 */
public class AttentionCollectionResponse {

    /**
     * data : {"data":"取消收藏成功！","collect":false}
     * error : false
     * message :
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
         * data : 取消收藏成功！
         * collect : false
         */
        private String data;
        private boolean collect;

        public void setData(String data) {
            this.data = data;
        }

        public void setCollect(boolean collect) {
            this.collect = collect;
        }

        public String getData() {
            return data;
        }

        public boolean isCollect() {
            return collect;
        }
    }
}

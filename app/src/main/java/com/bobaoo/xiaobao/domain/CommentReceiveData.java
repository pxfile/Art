package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/18.
 */
public class CommentReceiveData {

    /**
     * data : [{"lou":"3楼","addtime":"2015-06-17","user_name":"guo","head_img":"","report":"asdfasfsadf品的市场参考","photo":"","id":"490520","title":"晒单：存疑","content":"asdfasdfafdasf"}]
     * error : false
     * message : 3
     */
    private List<DataEntity> data;
    private boolean error;
    private String message;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataEntity> getData() {
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
         * lou : 3楼
         * addtime : 2015-06-17
         * user_name : guo
         * head_img :
         * report : asdfasfsadf品的市场参考
         * photo :
         * id : 490520
         * title : 晒单：存疑
         * content : asdfasdfafdasf
         */
        private String lou;
        private String addtime;
        private String user_name;
        private String head_img;
        private String report;
        private String photo;
        private String id;
        private String title;
        private String content;

        public void setLou(String lou) {
            this.lou = lou;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLou() {
            return lou;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getHead_img() {
            return head_img;
        }

        public String getReport() {
            return report;
        }

        public String getPhoto() {
            return photo;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}

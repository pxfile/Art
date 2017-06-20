package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/2.
 */
public class CommentSendData {

    /**
     * data : [{"img":"","addtime":"2015-06-17","id":"7867","oid":123,"title":"晒单：存疑","type":1,"content":"asdfasdfafdasf"}]
     * error : false
     * message : 101
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
         * img :
         * addtime : 2015-06-17
         * id : 7867
         * oid : 123
         * title : 晒单：存疑
         * type : 1
         * content : asdfasdfafdasf
         */
        private String img;
        private String addtime;
        private String id;
        private int oid;
        private String title;
        private int type;
        private String content;

        public void setImg(String img) {
            this.img = img;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setOid(int oid) {
            this.oid = oid;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getId() {
            return id;
        }

        public int getOid() {
            return oid;
        }

        public String getTitle() {
            return title;
        }

        public int getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }
}

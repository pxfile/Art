package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/2.
 */
public class InfoCollectionData {

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
         * fid : 211
         * zx_img :
         * context : 深耕土地，方能期待收获。当热闹的春拍人潮散去，二级市
         * comment : 0
         * id : 68
         * type : 1
         */
        private String fid;
        private String zx_img;
        private String context;
        private String comment;
        private String id;
        private String type;
        private String name;

        public void setFid(String fid) {
            this.fid = fid;
        }

        public void setZx_img(String zx_img) {
            this.zx_img = zx_img;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFid() {
            return fid;
        }

        public String getZx_img() {
            return zx_img;
        }

        public String getContext() {
            return context;
        }

        public String getComment() {
            return comment;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

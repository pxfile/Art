package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/2.
 */
public class AttentionsData {

    /**
     * data : [{"star_level":"特聘","org":"6","kind":"杂项","head_img":"","name":"戴志强","id":"39","honors":"中国钱币博物馆馆长"}]
     * error : false
     * message : 16
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
         * star_level : 特聘
         * org : 6
         * kind : 杂项
         * head_img :
         * name : 戴志强
         * id : 39
         * honors : 中国钱币博物馆馆长
         */
        private String star_level;
        private String org;
        private String kind;
        private String head_img;
        private String name;
        private String id;
        private String honors;

        public void setStar_level(String star_level) {
            this.star_level = star_level;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setHonors(String honors) {
            this.honors = honors;
        }

        public String getStar_level() {
            return star_level;
        }

        public String getOrg() {
            return org;
        }

        public String getKind() {
            return kind;
        }

        public String getHead_img() {
            return head_img;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public String getHonors() {
            return honors;
        }
    }
}

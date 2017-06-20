package com.bobaoo.xiaobao.domain;

import java.util.List;

/**
 * Created by you on 2015/6/2.
 */
public class TreasureCollectionData {
    /**
     * data : [{"fid":"493430","user_name":"。。。。。!","price":"4000-5500","head_img":"","report":"就图看，材质脉络清晰，色泽自然，天然砚石，非传统砚石，地方料子类，现代工艺，人物山水造型，工艺精细，无包浆，新东西，喜欢可藏.","photo":"http://c2c.ig365.cn/data/files/old_shop/gh_55/store_886055/jianbao/20150702/1435798370504.jpg@400w_400h","id":"3637","state":"真品","type":"0"}]
     * error : false
     * message : 1
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
         * fid : 493430
         * user_name : 。。。。。!
         * price : 4000-5500
         * head_img :
         * report : 就图看，材质脉络清晰，色泽自然，天然砚石，非传统砚石，地方料子类，现代工艺，人物山水造型，工艺精细，无包浆，新东西，喜欢可藏.
         * photo : http://c2c.ig365.cn/data/files/old_shop/gh_55/store_886055/jianbao/20150702/1435798370504.jpg@400w_400h
         * id : 3637
         * state : 真品
         * type : 0
         */
        private String fid;
        private String user_name;
        private String price;
        private String head_img;
        private String report;
        private String photo;
        private String id;
        private String state;
        private String type;

        public void setFid(String fid) {
            this.fid = fid;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public void setState(String state) {
            this.state = state;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFid() {
            return fid;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getPrice() {
            return price;
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

        public String getState() {
            return state;
        }

        public String getType() {
            return type;
        }
    }
}

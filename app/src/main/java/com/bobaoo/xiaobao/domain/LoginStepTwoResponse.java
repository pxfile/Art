package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 15/6/29.
 */
public class LoginStepTwoResponse {
    /**
     * data : {"headimg":"http://c2c.ig365.cn/data/files/old_shop/gh_722/store_883722/jianbao/20150625/201506251623155658.png@92w_92h","user_id":883722,"user_name":"一缕星光","name":"user","mobile":"13018019891","desc":"用户组","token":"dda1jUbj0iqDxUGNn6n1n8HklC6WDNls8m5ihyuoPA%2BrvRxAgkn9ALhEb%2F0CFhloRrrr"}
     * error : false
     */
    private DataEntity data;
    private boolean error;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DataEntity getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }

    public class DataEntity {
        /**
         * headimg : http://c2c.ig365.cn/data/files/old_shop/gh_722/store_883722/jianbao/20150625/201506251623155658.png@92w_92h
         * user_id : 883722
         * user_name : 一缕星光
         * name : user
         * mobile : 13018019891
         * desc : 用户组
         * token : dda1jUbj0iqDxUGNn6n1n8HklC6WDNls8m5ihyuoPA%2BrvRxAgkn9ALhEb%2F0CFhloRrrr
         */
        private String headimg;
        private int user_id;
        private String user_name;
        private String name;
        private String mobile;
        private String desc;
        private String token;

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHeadimg() {
            return headimg;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getName() {
            return name;
        }

        public String getMobile() {
            return mobile;
        }

        public String getDesc() {
            return desc;
        }

        public String getToken() {
            return token;
        }
    }
}

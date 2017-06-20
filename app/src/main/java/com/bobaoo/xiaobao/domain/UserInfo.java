package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/5/28.
 */
public class UserInfo {
    /**
     * data : {"cost":{"news":10,"balance":1.6,"askprice":"10","jb":4,"comment":"124","nocharg":"130","fans":"15"},"user":{"user_id":"796991","user_name":"Haha","integral":"37","head_img":"http://c2c.ig365.cn/data/files/old_shop/gh_991/store_796991/jianbao/20150629/201506290935022573.png@92w_92h","nikename":"Haha","mobile":"13986045178"}}
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
         * cost : {"news":10,"balance":1.6,"askprice":"10","jb":4,"comment":"124","nocharg":"130","fans":"15"}
         * user : {"user_id":"796991","user_name":"Haha","integral":"37","head_img":"http://c2c.ig365.cn/data/files/old_shop/gh_991/store_796991/jianbao/20150629/201506290935022573.png@92w_92h","nikename":"Haha","mobile":"13986045178"}
         */
        private CostEntity cost;
        private UserEntity user;

        public void setCost(CostEntity cost) {
            this.cost = cost;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public CostEntity getCost() {
            return cost;
        }

        public UserEntity getUser() {
            return user;
        }

        public class CostEntity {
            /**
             * news : 10
             * balance : 1.6
             * askprice : 10
             * jb : 4
             * comment : 124
             * nocharg : 130
             * fans : 15
             * wait: "100",
             * feedback: "100",
             * finish:"0"
             */
            private int news;
            private String balance;
            private String askprice;
            private int jb;
            private String comment;
            private String nocharg;
            private String fans;
            private String wait;
            private String feedback;
            private String finish;

            public String getFinish() {
                return finish;
            }

            public void setFinish(String finish) {
                this.finish = finish;
            }

            public void setWait(String wait) {
                this.wait = wait;
            }

            public void setFeedback(String feedback) {
                this.feedback = feedback;
            }

            public String getWait() {
                return wait;
            }

            public String getFeedback() {
                return feedback;
            }

            public void setNews(int news) {
                this.news = news;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public void setAskprice(String askprice) {
                this.askprice = askprice;
            }

            public void setJb(int jb) {
                this.jb = jb;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public void setNocharg(String nocharg) {
                this.nocharg = nocharg;
            }

            public void setFans(String fans) {
                this.fans = fans;
            }

            public int getNews() {
                return news;
            }

            public String getBalance() {
                return balance;
            }

            public String getAskprice() {
                return askprice;
            }

            public int getJb() {
                return jb;
            }

            public String getComment() {
                return comment;
            }

            public String getNocharg() {
                return nocharg;
            }

            public String getFans() {
                return fans;
            }
        }

        public class UserEntity {
            /**
             * user_id : 796991
             * user_name : Haha
             * integral : 37
             * head_img : http://c2c.ig365.cn/data/files/old_shop/gh_991/store_796991/jianbao/20150629/201506290935022573.png@92w_92h
             * nikename : Haha
             * mobile : 13986045178
             */
            private String user_id;
            private String user_name;
            private String integral;
            private String head_img;
            private String nikename;
            private String mobile;

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public void setHead_img(String head_img) {
                this.head_img = head_img;
            }

            public void setNikename(String nikename) {
                this.nikename = nikename;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public String getIntegral() {
                return integral;
            }

            public String getHead_img() {
                return head_img;
            }

            public String getNikename() {
                return nikename;
            }

            public String getMobile() {
                return mobile;
            }
        }
    }
}

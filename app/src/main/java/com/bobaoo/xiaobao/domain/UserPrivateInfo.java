package com.bobaoo.xiaobao.domain;

/**
 * Created by you on 2015/5/28.
 */
public class UserPrivateInfo {

    /**
     * data : {"free_jbapp_pt":"0","free_jbapp_yy":"0","mail":"a1123@163.com","user_name":"artxun_15946556","head_img":"http","nikename":"","end_time":"0","mobile":"13811341521","channel":"小米应用商店","discount":"1","free_jbapp_sp":"0","start_time":"0","appname":"xiaobao1.4","balance":"0.00","user_id":"796991","addtime":"2014-10","integral":"19","user_level":"0","free_jbapp_js":"0"}
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
         * free_jbapp_pt : 0
         * free_jbapp_yy : 0
         * mail : a1123@163.com
         * user_name : artxun_15946556
         * head_img : http
         * nikename :
         * end_time : 0
         * mobile : 13811341521
         * channel : 小米应用商店
         * discount : 1
         * free_jbapp_sp : 0
         * start_time : 0
         * appname : xiaobao1.4
         * balance : 0.00
         * user_id : 796991
         * addtime : 2014-10
         * integral : 19
         * user_level : 0
         * free_jbapp_js : 0
         */
        private String free_jbapp_pt;
        private String free_jbapp_yy;
        private String mail;
        private String user_name;
        private String head_img;
        private String nikename;
        private String end_time;
        private String mobile;
        private String channel;
        private String discount;
        private String free_jbapp_sp;
        private String start_time;
        private String appname;
        private String balance;
        private String user_id;
        private String addtime;
        private String integral;
        private String user_level;
        private String free_jbapp_js;

        public void setFree_jbapp_pt(String free_jbapp_pt) {
            this.free_jbapp_pt = free_jbapp_pt;
        }

        public void setFree_jbapp_yy(String free_jbapp_yy) {
            this.free_jbapp_yy = free_jbapp_yy;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public void setFree_jbapp_sp(String free_jbapp_sp) {
            this.free_jbapp_sp = free_jbapp_sp;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public void setUser_level(String user_level) {
            this.user_level = user_level;
        }

        public void setFree_jbapp_js(String free_jbapp_js) {
            this.free_jbapp_js = free_jbapp_js;
        }

        public String getFree_jbapp_pt() {
            return free_jbapp_pt;
        }

        public String getFree_jbapp_yy() {
            return free_jbapp_yy;
        }

        public String getMail() {
            return mail;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getHead_img() {
            return head_img;
        }

        public String getNikename() {
            return nikename;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getMobile() {
            return mobile;
        }

        public String getChannel() {
            return channel;
        }

        public String getDiscount() {
            return discount;
        }

        public String getFree_jbapp_sp() {
            return free_jbapp_sp;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getAppname() {
            return appname;
        }

        public String getBalance() {
            return balance;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getIntegral() {
            return integral;
        }

        public String getUser_level() {
            return user_level;
        }

        public String getFree_jbapp_js() {
            return free_jbapp_js;
        }
    }
}
